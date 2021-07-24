package com.tizzone.instantnewsapplication.presentation.ui

import androidx.lifecycle.ViewModel
import com.tizzone.instantnewsapplication.domain.usecases.articles_list.GetAllHeadlines
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticlesListViewModel
@Inject constructor(
    private val getAllHeadlines: GetAllHeadlines,
) : ViewModel() {

}