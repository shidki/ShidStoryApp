package com.example.submissionstoryapp.ui.Story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.submissionstoryapp.response.repository.AppRepository
import com.example.submissionstoryapp.response.story.listStory

class ListStoryViewModel(storyRepository: AppRepository): ViewModel() {
    val stories: LiveData<PagingData<listStory>> = storyRepository.getStories().cachedIn(viewModelScope)
}