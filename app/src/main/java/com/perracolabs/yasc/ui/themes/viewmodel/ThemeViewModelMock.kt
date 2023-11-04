/*
 * Copyright (c) 2023 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perracolabs.yasc.ui.themes.viewmodel

import android.view.View
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import com.perracolabs.yasc.ui.themes.attributes.Theme
import kotlinx.coroutines.flow.MutableStateFlow

class ThemeViewModelMock : IThemeViewModel {
    override val theme = mutableStateOf(Theme.LIGHT)
    override val hasTransition = mutableStateOf(false)
    override val inProgress = mutableStateOf(false)
    override val transitionFactor = MutableStateFlow(0f)
    override suspend fun request(newTheme: Theme, themeLayoutView: View, transitionOrigin: Offset) {}
    override suspend fun start() {}
    override fun getAttributes(): ThemeViewModel.Attributes? = null
}


