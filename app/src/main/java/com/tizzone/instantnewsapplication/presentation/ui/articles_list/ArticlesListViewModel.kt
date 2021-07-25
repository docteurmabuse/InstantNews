package com.tizzone.instantnewsapplication.presentation.ui.articles_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tizzone.instantnewsapplication.domain.data.DataState
import com.tizzone.instantnewsapplication.domain.model.Article
import com.tizzone.instantnewsapplication.domain.usecases.articles_list.GetAllHeadlines
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticlesListViewModel
@Inject constructor(
    private val getAllHeadlines: GetAllHeadlines,
) : ViewModel() {
    private val _stateArticles =
        MutableStateFlow<DataState<PagingData<Article>>>(DataState.loading(null))
    val stateArticles: StateFlow<DataState<PagingData<Article>>> get() = _stateArticles

    init {
        fetchAllHeadlines()
    }

    private fun fetchAllHeadlines() {
        viewModelScope.launch {
            getAllHeadlines.invoke()
                .catch { e ->
                    _stateArticles.value = DataState.error("Error: ${e.toString()}", null)
                }
                .cachedIn(viewModelScope)
                .collectLatest { pagingDataArticles ->
                    _stateArticles.value = DataState.success(pagingDataArticles)
                }
        }
    }
}