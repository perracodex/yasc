/*
 * Copyright (c) 2023-Present Perraco. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perraco.yasc.ui.components.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.perraco.yasc.ui.themes.ThemeLayout

/**
 * A Drawer Composable that displays the navigation items.
 */
@Composable
fun DrawerSheet() {
    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.primary
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            DrawerHeader()
            DrawerContent()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDrawer() {
    ThemeLayout {
        DrawerSheet()
    }
}
