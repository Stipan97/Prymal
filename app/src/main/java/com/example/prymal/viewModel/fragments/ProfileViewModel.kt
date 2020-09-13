package com.example.prymal.viewModel.fragments

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.prymal.Firebase

class ProfileViewModel : ViewModel() {
    fun fetchImages(
        firebase: Firebase,
        rv_ProfileImages: RecyclerView,
        activity: FragmentActivity,
        userId: String
    ) {
        firebase.fetchImages(rv_ProfileImages, activity, userId)
    }

    fun setupProfileInfo( firebase: Firebase, activity: Activity, userId: String) {
        firebase.fetchProfileInfo(activity, userId)
    }
}