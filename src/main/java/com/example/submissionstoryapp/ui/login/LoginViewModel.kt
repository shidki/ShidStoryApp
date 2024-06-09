package com.example.submissionstoryapp.ui.login

import androidx.lifecycle.ViewModel
import com.example.submissionstoryapp.response.repository.AppRepository

class LoginViewModel(private val storyRepository: AppRepository) : ViewModel() {
    fun login(email: String, password: String) = storyRepository.postLogin(email, password)
}