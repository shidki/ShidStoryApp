package com.example.submissionstoryapp.ui.Story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.submissionstoryapp.R
import com.example.submissionstoryapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getDetailNama = intent.getStringExtra("storyName")
        val getDetailDeskripsi = intent.getStringExtra("storyDescription")
        val getDetailDambar = intent.getStringExtra("storyPhotoUrl")

        binding.detailNama.text = getDetailNama
        binding.detailDeskripsi.text = getDetailDeskripsi

        Glide.with(this)
            .load(getDetailDambar)
            .placeholder(R.drawable.yuna)
            .error(R.drawable.yuna)
            .into(binding.detailGambar)
    }
}