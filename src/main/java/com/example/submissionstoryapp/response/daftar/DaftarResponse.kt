package com.example.submissionstoryapp.response.daftar


import com.google.gson.annotations.SerializedName

data class DaftarResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)