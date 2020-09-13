package com.example.prymal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.prymal.R
import com.example.prymal.model.Post
import kotlinx.android.synthetic.main.item_card_image.view.*

class ProfileImagesAdapter(private val posts: List<Post>) : RecyclerView.Adapter<ProfileImagesAdapter.ProfileImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileImageViewHolder {
        return ProfileImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_card_image, parent, false))
    }

    override fun onBindViewHolder(holder: ProfileImageViewHolder, position: Int) {
        val postImage = posts[position].imageUrl
        Glide.with(holder.view.context).load(postImage).into(holder.view.iv_profileImages)
    }

    override fun getItemCount() = posts.size

    class ProfileImageViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}