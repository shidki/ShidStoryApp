package com.example.submissionstoryapp.response.story

import com.google.gson.annotations.SerializedName

data class StoryResponse(
    @field:SerializedName("listStory")
    val listStory: List<listStory>,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)