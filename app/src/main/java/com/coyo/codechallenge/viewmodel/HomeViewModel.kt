package com.coyo.codechallenge.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.coyo.codechallenge.data.model.Post
import com.coyo.codechallenge.data.repo.DbPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dbPostRepository: DbPostRepository
) : ViewModel() {
    private val clearListCh = Channel<Unit>(Channel.CONFLATED)

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    fun posts(): Flow<PagingData<Post>> {
        return flowOf(
            clearListCh.receiveAsFlow().map { PagingData.empty() },
            dbPostRepository.fetchPostPage().cachedIn(viewModelScope)
        ).flattenMerge(2)
    }
}