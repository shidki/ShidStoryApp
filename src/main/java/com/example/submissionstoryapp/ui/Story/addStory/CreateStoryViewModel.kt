package com.example.submissionstoryapp.ui.Story.addStory

import androidx.lifecycle.ViewModel
import com.example.submissionstoryapp.response.repository.AppRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CreateStoryViewModel(private val storyRepository: AppRepository): ViewModel() {
    fun postStory(file: MultipartBody.Part, description: RequestBody) = storyRepository.postStory(file, description)
}