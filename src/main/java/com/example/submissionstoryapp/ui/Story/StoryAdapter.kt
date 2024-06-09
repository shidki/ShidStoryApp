package com.example.submissionstoryapp.ui.Story


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submissionstoryapp.R
import com.example.submissionstoryapp.response.story.listStory

class StoryAdapter : PagingDataAdapter<listStory, StoryAdapter.ListViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<listStory>() {
            override fun areItemsTheSame(oldItem: listStory, newItem: listStory): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: listStory, newItem: listStory): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.nama_story)
        val tvDeskripsi: TextView = view.findViewById(R.id.deskripsi_story)
        val tvImage: ImageView = view.findViewById(R.id.tv_imageUser)
        val cardUser: CardView = view.findViewById(R.id.card_user)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_story, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val storyData = getItem(position)
        storyData?.let {
            holder.tvName.text = it.name
            holder.tvDeskripsi.text = it.description

            Glide.with(holder.itemView.context)
                .load(it.photoUrl)
                .placeholder(R.drawable.baseline_person_2_24)
                .error(R.drawable.baseline_person_2_24)
                .into(holder.tvImage)

            holder.cardUser.setOnClickListener {
                val intent = Intent(holder.itemView.context, DetailStoryActivity::class.java).apply {
                    putExtra("storyId", storyData.id)
                    putExtra("storyName", storyData.name)
                    putExtra("storyDescription", storyData.description)
                    putExtra("storyPhotoUrl", storyData.photoUrl)
                }
                holder.itemView.context.startActivity(intent)
            }
        }
    }
}
