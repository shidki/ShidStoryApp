package com.example.submissionstoryapp

import com.example.submissionstoryapp.response.story.StoryResponse
import com.example.submissionstoryapp.response.story.listStory

object DataDummy {

    fun generateDummyListStory(): StoryResponse {
        val listStory2 = ArrayList<listStory>()
        for (i in 1..10) {
            val story = listStory(
                createdAt = "2024-05-30T09:07:01Z",
                description = "deskripsi $i",
                id = "id_$i",
                lat = i.toDouble() * 10,
                lon = i.toDouble() * 10,
                name = "nama $i",
                photoUrl = "https://i.pinimg.com/736x/cb/6f/1f/cb6f1fd2c98e595ae3c8493fbc9eb08b.jpg"
            )
            listStory2.add(story)
        }

        return StoryResponse(
            error = false,
            message = "Stories fetched successfully",
            listStory = listStory2
        )
    }

}