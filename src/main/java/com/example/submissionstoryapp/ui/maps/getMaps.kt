package com.example.submissionstoryapp.ui.maps

import android.app.Application
import android.content.Context

class getMaps : Application(), ContextProvider {
    override fun getContext(): Context = this
}
