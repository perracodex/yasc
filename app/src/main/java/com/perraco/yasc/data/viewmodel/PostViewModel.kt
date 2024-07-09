/*
 * Copyright (c) 2024 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perraco.yasc.data.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.perraco.yasc.data.models.Post
import com.perraco.yasc.data.repository.PostRepository
import com.perraco.yasc.system.debug.Tracer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {
    /** Cached content data. */
    private val _content = mutableStateOf<List<Post>>(emptyList())
    val content: State<List<Post>> = _content

    /** Holds the loading state. */
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    /** Holds content related UX messages, i.e., loading errors. */
    private val _hint = mutableStateOf<String?>(null)
    val hint: State<String?> = _hint

    /** Holds the list of items identifiers currently expanded. */
    private val _expandedItemIds = mutableStateOf(setOf<Int>())
    val expandedItemsIds: State<Set<Int>> = _expandedItemIds

    private var currentPage = 0
    private val pageSize = 20
    private var isLastPage = false

    init {
        // Initial load.
        loadMoreContent()
    }

    /**
     * Load content from the repository.
     */
    fun loadMoreContent() {
        if (isLastPage || _isLoading.value)
            return

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val startIndex: Int = currentPage * pageSize

                Tracer.i(TAG, "Loading posts. Index $startIndex. Page: $currentPage. Size: $pageSize.")

                val newContent: List<Post> = repository.getPosts(start = startIndex, limit = pageSize)

                if (newContent.size < pageSize) {
                    isLastPage = true
                }

                _content.value += newContent
                currentPage++

                _hint.value = null
            } catch (e: Exception) {
                _hint.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Clears the hint message, if any.
     */
    fun clearHint() {
        _hint.value = null
    }

    /**
     * Toggles the expanded state of an item.
     *
     * @param itemId The item unique identifier.
     */
    fun toggleExpandedItem(itemId: Int) {
        _expandedItemIds.value = if (_expandedItemIds.value.contains(itemId)) {
            _expandedItemIds.value - itemId
        } else {
            _expandedItemIds.value + itemId
        }
    }

    companion object {
        private const val TAG = "PostsViewModel"
    }
}
