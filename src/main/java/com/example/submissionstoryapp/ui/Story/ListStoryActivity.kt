package com.example.submissionstoryapp.ui.Story


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.paging.PagingSource
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionstoryapp.ui.maps.MapsActivity
import com.example.submissionstoryapp.R
import com.example.submissionstoryapp.databinding.ActivityListStoryBinding
import com.example.submissionstoryapp.response.repository.Preference
import com.example.submissionstoryapp.response.story.StoryResponse
import com.example.submissionstoryapp.response.story.listStory
import com.example.submissionstoryapp.ui.Story.addStory.AddStoryActivity
import com.example.submissionstoryapp.ui.login.MainActivity

import com.example.submissionstoryapp.utils.ViewModelFactory


class ListStoryActivity : AppCompatActivity(){

    private lateinit var binding: ActivityListStoryBinding

    private val listStoryViewModel: ListStoryViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStory.addItemDecoration(itemDecoration)

        val adapter = StoryAdapter()
        binding.rvStory.adapter = adapter
        binding.rvStory.setHasFixedSize(true)

        listStoryViewModel.stories.observe(this) { pagingData ->
            Log.d("heheyyy",pagingData.toString())
            adapter.submitData(lifecycle, pagingData)
        }

        binding.topAppBar.setOnMenuItemClickListener{ menuItem ->
            when(menuItem.itemId){
                R.id.menu1 -> {
                    Preference.logOut(this)
                    val intent = Intent(this, MainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.menu -> {
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        binding.fab.setOnClickListener{
            val intent = Intent(this@ListStoryActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }
}
