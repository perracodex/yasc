/*
 * Copyright (c) 2024 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perraco.yasc.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.perraco.yasc.data.models.Joke

@Composable
fun JokeItem(
    item: Joke,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit
) {
    val backgroundColor = if (isExpanded) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (isExpanded) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .then(
                if (item.type == Joke.Type.TWO_PART) Modifier.clickable { onToggleExpand() }
                else Modifier
            )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            when (item.type) {
                Joke.Type.SINGLE -> {
                    item.joke?.let {
                        Text(
                            text = it,
                            color = textColor,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                Joke.Type.TWO_PART -> {
                    item.setup?.let { setup ->
                        Text(
                            text = setup,
                            color = textColor,
                            style = MaterialTheme.typography.titleMedium
                        )

                        if (isExpanded) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = item.delivery ?: "???",
                                color = textColor,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        } else
                            Text(
                                text = "…",
                                color = textColor,
                                style = MaterialTheme.typography.bodyMedium
                            )
                    }
                }
            }
        }
    }
}
