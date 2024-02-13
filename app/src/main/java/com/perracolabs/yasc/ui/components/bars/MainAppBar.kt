/*
 * Copyright (c) 2023 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perracolabs.yasc.ui.components.bars

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.perracolabs.yasc.R

/**
 * Composable rendering a top app bar.
 *
 * @param title The text to be displayed as the title in the top app bar.
 * @param leadingAction The primary action set at the leading edge.
 * @param trailingAction An optional secondary action set at the trailing edge.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    title: String,
    leadingAction: AppBarAction,
    trailingAction: AppBarAction?
) {
    val leadingPainter: VectorPainter = rememberVectorPainter(image = ImageVector.vectorResource(leadingAction.iconId))
    val trailingPainter: VectorPainter? = trailingAction?.let {
        rememberVectorPainter(image = ImageVector.vectorResource(it.iconId))
    }

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                leadingAction.callback()
            }) {
                Image(
                    painter = leadingPainter,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier.size(dimensionResource(id = R.dimen.toolbar_button_size))
                )
            }
        },
        actions = {
            trailingAction?.let {
                IconButton(onClick = { it.callback() }) {
                    Image(
                        painter = trailingPainter!!,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                        modifier = Modifier.size(dimensionResource(id = R.dimen.toolbar_button_size))
                    )
                }
            }
        }
    )
}

/**
 * Holds the information for an action in the [MainAppBar].
 *
 * @param iconId Drawable resource ID for the action icon.
 * @param callback Function to execute when the action is triggered.
 */
data class AppBarAction(@DrawableRes val iconId: Int, val callback: () -> Unit)

@Preview
@Composable
private fun PreviewTopAppView() {
    val navigationAction = AppBarAction(iconId = R.drawable.ic_menu, callback = { /* Do Nothing */ })
    val overflowAction = AppBarAction(iconId = R.drawable.ic_overflow, callback = { /* Do Nothing */ })

    MainAppBar(
        title = stringResource(id = R.string.app_name),
        leadingAction = navigationAction,
        trailingAction = overflowAction
    )
}
