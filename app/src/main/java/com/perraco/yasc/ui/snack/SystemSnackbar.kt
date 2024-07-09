/*
 * Copyright (c) 2024 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perraco.yasc.ui.snack

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import com.perraco.yasc.R

/**
 * A system Snackbar that shows a message and an optional action.
 *
 * @param snackbarHostState The SnackbarHostState to show the Snackbar.
 * @param message The message to show. Must not be empty.
 * @param duration The duration for which the Snackbar should be shown. Defaults to indefinite.
 * @param onDismiss Optional callback to invoke when the Snackbar is dismissed.
 * @param onAction Optional callback to invoke when the action is clicked, for example, to retry an operation.
 * @throws IllegalArgumentException If the message is empty.
 */
@Composable
fun SystemSnackbar(
    snackbarHostState: SnackbarHostState,
    message: String,
    duration: SnackbarDuration = SnackbarDuration.Indefinite,
    onDismiss: (() -> Unit)? = null,
    onAction: (() -> Unit)? = null,
) {
    if (message.isBlank())
        throw IllegalArgumentException("System snack bar message cannot be empty.")

    // Determine the label for the action button on the snackbar;
    // default to "Dismiss" if no action is provided.
    val actionLabel: String = onAction?.let {
        stringResource(id = R.string.action_retry)
    } ?: stringResource(id = R.string.action_dismiss)

    // Using a LaunchedEffect to automatically manage snackbar lifecycle in response to message changes.
    // When a new message arrives, the previous snackbar is dismissed, and a new snackbar is shown.
    // This ensures that the snackbar content is always up-to-date with the latest message.
    LaunchedEffect(key1 = message) {
        // Show the snackbar and wait for the result, which could be a dismissal or an action performed.
        val result: SnackbarResult = snackbarHostState.showSnackbar(
            message = message,
            actionLabel = actionLabel,
            duration = duration
        )

        when (result) {
            SnackbarResult.Dismissed -> onDismiss?.invoke()
            SnackbarResult.ActionPerformed -> {
                onAction?.invoke()
                onDismiss?.invoke()
            }
        }
    }
}
