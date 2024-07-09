/*
 * Copyright (c) 2023-Present Perraco. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perraco.yasc.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.perraco.yasc.R
import com.perraco.yasc.data.viewmodel.JokeViewModel
import com.perraco.yasc.ui.LocalNavHostController
import com.perraco.yasc.ui.LocalSnackbarHostState
import com.perraco.yasc.ui.components.bars.AppBarAction
import com.perraco.yasc.ui.components.bars.MainAppBar
import com.perraco.yasc.ui.navigation.Route
import com.perraco.yasc.ui.snack.SystemSnackbar
import com.perraco.yasc.ui.themes.ThemeLayout

@Composable
fun DetailScreen() {
    Scaffold(
        topBar = { TopBar() },
        snackbarHost = { SnackbarHost(hostState = LocalSnackbarHostState.current) },
        content = { innerPadding -> Content(innerPadding) }
    )
}

@Composable
private fun TopBar() {
    val navController: NavController = LocalNavHostController.current

    val navigationAction: AppBarAction = remember {
        AppBarAction(iconId = R.drawable.ic_arrow_back, callback = {
            navController.popBackStack()
        })
    }

    MainAppBar(
        title = stringResource(id = Route.DETAIL.resourceId),
        leadingAction = navigationAction,
        trailingAction = null
    )
}

@Composable
private fun Content(
    innerPadding: PaddingValues,
    viewModel: JokeViewModel = hiltViewModel()
) {
    val content by viewModel.content
    val expandedItemsIds by viewModel.expandedItemsIds
    val isLoading by viewModel.isLoading
    val hint by viewModel.hint

    val newContentThreshold = 10  // How many items from the end to start loading more data.
    val listState: LazyListState = rememberLazyListState()

    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .fillMaxSize()
            .padding(paddingValues = innerPadding),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.matchParentSize()
        ) {
            items(content, key = { item -> item.id }) { item ->
                JokeItem(
                    item = item,
                    isExpanded = expandedItemsIds.contains(item.id),
                    onToggleExpand = { viewModel.toggleExpandedItem(itemId = item.id) }
                )
            }
        }

        // Trigger additional data loading when the user scrolls near the end of the list.
        LaunchedEffect(key1 = listState) {
            // Using snapshotFlow as it provides an efficient way to convert state changes into flow emissions,
            // allowing to react only when the last visible item's index changes, minimizing unnecessary checks.
            snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                .collect { lastIndex ->
                    lastIndex?.let { index ->
                        // Load more content when not currently loading and nearing the end of the list.
                        if (!isLoading && content.size - index <= newContentThreshold) {
                            viewModel.loadMoreContent()
                        }
                    }
                }
        }

        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.inversePrimary
            )
        }

        hint?.let { message ->
            SystemSnackbar(
                snackbarHostState = LocalSnackbarHostState.current,
                message = message,
                onDismiss = { viewModel.clearHint() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDetailScreen() {
    ThemeLayout {
        DetailScreen()
    }
}
