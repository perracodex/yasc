/*
 * Copyright (c) 2023-Present Perraco. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perraco.yasc.ui.themes.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.view.View
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.core.view.drawToBitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.perraco.yasc.system.debug.Tracer
import com.perraco.yasc.system.settings.appSettings
import com.perraco.yasc.ui.themes.attributes.Theme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.yield
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * ViewModel responsible for managing the theme transition animation and data.
 *
 * The transition process works as follows:
 *
 * 1. When the transition is requested, a screenshot of the entire application is captured.
 *
 * 2. The captured screenshot is overlaid on top of the UI.
 *
 * 3. While the screenshot is overlaid, the application theme gets changed.
 *    This happens without affecting the visual representation because of the screenshot overlay.
 *
 * 4. Then an animation starts where a clipping hole gets rendered in the screenshot to
 *    simulate a ripple effect. This clipping hole either shrinks or grows,
 *    depending on the selected theme, to reveal the newly applied theme beneath it.
 *
 * 5. The overlay screenshot is eventually removed, leaving the UI with the new theme visible.
 *
 * @param application Application context for accessing app-wide resources.
 */
class ThemeViewModel(private val application: Application) : AndroidViewModel(application), IThemeViewModel {

    /**
     * Holds and emits the current theme.
     */
    override var theme = mutableStateOf(Theme.DARK)
        private set

    init {
        // Get the persisted theme from the app settings.
        viewModelScope.launch {
            val storageTheme: Theme = application.appSettings.uiSettings.theme.first()
            theme.value = storageTheme
        }
    }

    /**
     * Mutex used for synchronizing theme transition operations
     * to ensure that only one transition occurs at a time.
     */
    private val mutex = Mutex()

    /**
     * Holds the transition attributes, such as the overlay bitmap, etc.
     */
    private var attributes: Attributes? = null

    /**
     * State representing if the theme transition should occur.
     */
    override var hasTransition = mutableStateOf(false)
        private set

    /**
     * State representing if a transition is currently in progress.
     */
    override var inProgress = mutableStateOf(false)
        private set

    /**
     * Flow to emit values representing the progress of the theme transition animation.
     */
    override var transitionFactor = MutableStateFlow(0f)
        private set

    /**
     * Requests a new theme transition.
     * This implies to capture a screenshot of the screen, and prepare the transition attributes.
     */
    override suspend fun request(newTheme: Theme, themeLayoutView: View, transitionOrigin: Offset) {
        mutex.withLock {
            if (newTheme != theme.value && !hasTransition.value) {
                // Take a screenshot of the entire contents within the theme layout.
                val bitmap = themeLayoutView.drawToBitmap(Bitmap.Config.ARGB_8888)

                // Prepare the transition required attributes.
                attributes = Attributes(
                    newTheme = newTheme,
                    overlayBitmap = bitmap,
                    areaWidth = bitmap.width.toFloat(),
                    areaHeight = bitmap.height.toFloat(),
                    origin = transitionOrigin,
                    shrink = (newTheme == Theme.LIGHT)
                )

                // Notify observers there is a new transition ready.
                hasTransition.value = true

                Tracer.i(TAG, "Theme transition ready.")
            }
        }
    }

    /**
     * Starts the theme transition animation.
     * The transition should have been previously prepared.
     */
    override suspend fun start() {
        mutex.withLock {
            if (inProgress.value) return
            val transitionData = attributes ?: return

            inProgress.value = true
            Tracer.i(TAG, "Theme transition started.")

            // Update the theme value, triggering observer notification.
            // This must be done before initiating the animation to ensure
            // that ThemeLayout recomposes with the new theme at the UI level,
            // which will not be visible as it will be already obscured by the
            // transition bitmap overlay before the transition starts to render.
            theme.value = transitionData.newTheme
            yield()

            // Perform the actual transition animation.
            transition(transitionData)

            // Save to the application settings.
            val uiSettings = application.applicationContext.appSettings.uiSettings
            uiSettings.theme(transitionData.newTheme)

            // Complete the transition by releasing and resetting all resources.
            reset()

            Tracer.i(TAG, "Theme transition ended.")
        }
    }

    /**
     * Executes the theme transition based on the supplied attributes.
     * This method will return after the transition has ended.
     *
     * @param transitionData Attributes to use during the transition.
     */
    private suspend fun transition(transitionData: Attributes) {
        // Calculate the distance from the center of the bitmap to its farthest corner.
        // Using the Pythagorean theorem to find the diagonal of the rectangle formed by the bitmap dimensions.
        // This value will be used as the maximum radius for the circular animation.
        val maxFactor = sqrt((transitionData.areaWidth).pow(2) + (transitionData.areaHeight).pow(2))

        val (startFactor, endFactor, easing) = if (transitionData.shrink) {
            Triple(maxFactor, 0f, FastOutSlowInEasing)
        } else {
            Triple(0f, maxFactor, FastOutLinearInEasing)
        }

        Animatable(0f).apply {
            snapTo(startFactor)

            animateTo(
                targetValue = endFactor,
                animationSpec = tween(DURATION_MS, easing = easing)
            ) {
                // Notify to observers the new transition factor.
                transitionFactor.value = value
            }
        }
    }

    /**
     * Resets the states and releases any resources.
     */
    private fun reset() {
        hasTransition.value = false
        val bitmap = attributes?.overlayBitmap
        attributes = null
        inProgress.value = false
        bitmap?.recycle()
    }

    /**
     * Retrieves the current theme attributes.
     *
     * @return Current theme attributes.
     */
    override fun getAttributes(): Attributes? = attributes

    /**
     * Holds the transition attributes
     */
    data class Attributes(
        val newTheme: Theme,
        val overlayBitmap: Bitmap,
        val areaWidth: Float,
        val areaHeight: Float,
        val origin: Offset,
        val shrink: Boolean
    )

    companion object {
        /** The transition duration, in milliseconds.  */
        private const val DURATION_MS = 400

        private const val TAG = "ThemeViewModel"
    }
}

