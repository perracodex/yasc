/*
 * Copyright (c) 2023-Present Perraco. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perraco.yasc.ui.themes.transition

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import com.perraco.yasc.ui.themes.viewmodel.obtainThemeViewModel

/**
 * Performs the theme transition rendering.
 */
@Composable
fun ThemeRenderer() {
    val themeViewModel = obtainThemeViewModel()

    if (!themeViewModel.inProgress.value) {
        return
    }

    val radius by themeViewModel.transitionFactor.collectAsState()
    val paint = remember { Paint().apply { isAntiAlias = true } }
    val combinedPath = remember { Path() }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val attributes = themeViewModel.getAttributes() ?: return@Canvas

            combinedPath.apply {

                // Clear previous path data.
                reset()

                if (attributes.shrink) {
                    // Shrink Transition: Use CW (Clockwise) for the circle path.
                    // CW means the circle will be drawn in a clockwise direction.
                    // In the context of clipping, the bitmap is visible inside the circle,
                    // and the area outside the circle gets clipped or 'erased' as the circle shrinks.
                    addCircle(attributes.origin.x, attributes.origin.y, radius, Path.Direction.CW)
                } else {
                    // Grow Transition: First, define a full rectangle with CW (Clockwise) path.
                    // Then, add a CCW (Counter-Clockwise) circle inside it.
                    // The use of opposite directions (CW and CCW) creates a "hole" in the rectangle.
                    // This "hole" will grow, revealing the new theme underneath.
                    addRect(0f, 0f, size.width, size.height, Path.Direction.CW)
                    addCircle(attributes.origin.x, attributes.origin.y, radius, Path.Direction.CCW)

                    // Define the fill rule as EVEN_ODD. This rule determines the regions that
                    // should be filled vs. left unfilled. So, the overlapping areas between
                    // the circle and rectangle are left empty, creating a clear transition effect.
                    fillType = Path.FillType.EVEN_ODD
                }
            }

            // Draw the actual content onto the Canvas.
            // The clipping path is applied first, ensuring that only
            // the desired portion of the bitmap gets rendered.
            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.clipPath(combinedPath)

                attributes.overlayBitmap.let { bitmap: Bitmap ->
                    if (!bitmap.isRecycled)
                        canvas.nativeCanvas.drawBitmap(bitmap, 0f, 0f, paint)
                }
            }
        }
    }
}
