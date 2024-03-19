/*
 * Copyright (c) 2023-Present Perraco. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perraco.yasc.ui.components.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.perraco.yasc.R

@Composable
fun ImageActionButton(
    icon: Painter,
    showIndication: Boolean = true,
    iconTint: Color? = null,
    onClick: (Offset) -> Unit
) {
    var centerOffset: Offset by remember { mutableStateOf(Offset(x = 0f, y = 0f)) }
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }

    Image(
        painter = icon,
        contentScale = ContentScale.Inside,
        contentDescription = null,
        colorFilter = ColorFilter.tint(iconTint ?: MaterialTheme.colorScheme.surfaceTint),
        modifier = Modifier
            .size(dimensionResource(id = R.dimen.action_icon))
            .onGloballyPositioned { coordinates ->
                coordinates
                    .positionInRoot()
                    .let { offset ->
                        centerOffset = Offset(
                            offset.x + (coordinates.size.width / 2f),
                            offset.y + (coordinates.size.height / 2f)
                        )
                    }
            }
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(
                    bounded = false,
                    color = MaterialTheme.colorScheme.surfaceVariant
                ).takeIf { showIndication },
                onClick = { onClick.invoke(centerOffset) }
            )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewDrawer() {
    val iconPainter = rememberVectorPainter(image = Icons.Default.AccountCircle)
    ImageActionButton(icon = iconPainter, iconTint = Color.Gray) {
        // Do nothing
    }
}
