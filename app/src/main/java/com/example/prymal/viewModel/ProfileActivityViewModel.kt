package com.example.prymal.viewModel

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.prymal.Firebase

class ProfileActivityViewModel {
    fun fetchImages(firebase: Firebase, rv_ProfileImages: RecyclerView, activity: Activity, userId: String) {
        firebase.fetchImages(rv_ProfileImages, activity as FragmentActivity, userId)
    }

    fun setupProfileInfo( firebase: Firebase, activity: Activity, userId: String) {
        firebase.fetchProfileInfo(activity, userId)
    }
}