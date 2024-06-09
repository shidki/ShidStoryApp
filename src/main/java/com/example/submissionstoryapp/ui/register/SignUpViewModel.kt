package com.example.submissionstoryapp.ui.register

import androidx.lifecycle.ViewModel
import com.example.submissionstoryapp.response.repository.AppRepository


class SignUpViewModel(private val storyRepository: AppRepository) : ViewModel() {
    fun signUp(name: String, email: String, password: String) = storyRepository.postSignUp(name, email, password)
}