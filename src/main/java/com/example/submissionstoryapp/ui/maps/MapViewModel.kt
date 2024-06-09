package com.example.submissionstoryapp.ui.maps

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.submissionstoryapp.response.repository.AppRepository
import com.example.submissionstoryapp.response.repository.Preference

class MapViewModel(private val appRepository: AppRepository): ViewModel() {
    fun getStoriesWithLocation() = appRepository.getStoriesWithLocation()
}