package com.example.prymal.viewModel.fragments

import android.content.Context
import android.view.View
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.prymal.Firebase

class HomeViewModel : ViewModel() {

    fun fetchPosts(firebase: Firebase, rv_Posts: RecyclerView, activity: FragmentActivity) {
        firebase.fetchPosts(rv_Posts, activity)
    }

    fun handleLike(firebase: Firebase, btnPostLike: Button, uploadId: String, userId: String, context: Context, view: View) {
        firebase.handleLike(uploadId, btnPostLike, context)
        firebase.handleLikeInDB(uploadId, userId, view)
    }

    fun isLiked(firebase: Firebase, uploadId: String, btnPostLike: Button, context: Context) {
        firebase.isLiked(uploadId, btnPostLike, context)
    }
}