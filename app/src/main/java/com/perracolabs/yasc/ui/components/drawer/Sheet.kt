/*
 * Copyright (c) 2023 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perracolabs.yasc.ui.components.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.perracolabs.yasc.ui.themes.ThemeLayout

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
