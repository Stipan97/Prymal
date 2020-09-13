package com.example.prymal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.prymal.Firebase
import com.example.prymal.R
import com.example.prymal.model.Post
import com.example.prymal.viewModel.fragments.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.item_post.view.*

class PostAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private lateinit var viewModel: HomeViewModel
    private var firebase: Firebase = Firebase()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false))
    }

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        viewModel = HomeViewModel()

        firebase.auth = FirebaseAuth.getInstance()
        firebase.db = FirebaseDatabase.getInstance()

        val post = posts[position]
        holder.view.tv_postName.text = post.postName
        holder.view.tv_postDescription.text = post.description
        holder.view.tv_postLocation.text = post.location
        var postLikes = ""
        postLikes = if (post.likes == 1.toLong()) {
            post.likes.toString() + " like"
        } else {
            post.likes.toString() + " likes"
        }
        holder.view.tv_postLikes.text = postLikes

        Glide.with(holder.view.context).load(post.imageUrl).into(holder.view.iv_postImage)

        viewModel.isLiked(firebase, post.uploadId, holder.view.btn_postLike, holder.view.context)

        holder.view.btn_postLike.setOnClickListener {
            viewModel.handleLike(firebase, holder.view.btn_postLike, post.uploadId, post.userId, holder.view.context, holder.view)
        }
    }

    class PostViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}