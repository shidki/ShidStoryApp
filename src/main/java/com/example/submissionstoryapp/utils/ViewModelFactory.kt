package com.example.submissionstoryapp.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.submissionstoryapp.response.repository.Injection
import com.example.submissionstoryapp.ui.Story.ListStoryViewModel
import com.example.submissionstoryapp.ui.Story.addStory.CreateStoryViewModel
import com.example.submissionstoryapp.ui.login.LoginViewModel
import com.example.submissionstoryapp.ui.maps.MapViewModel
import com.example.submissionstoryapp.ui.register.SignUpViewModel


class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ListStoryViewModel::class.java) -> {
                ListStoryViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(MapViewModel::class.java) -> {
                MapViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(CreateStoryViewModel::class.java) -> {
                CreateStoryViewModel(Injection.provideRepository(context)) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}