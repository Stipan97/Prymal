package com.example.prymal.viewModel.fragments

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.prymal.Firebase
import com.example.prymal.view.ProfileActivity

class SearchViewModel : ViewModel() {

    fun isFollowing(firebase: Firebase, btn_SearchFollow: Button, btn_SearchUnfollow: Button, userId: String) {
        firebase.isFollow(btn_SearchFollow, btn_SearchUnfollow, userId)
    }

    fun addFollow(firebase: Firebase, btn_SearchFollow: Button, btn_SearchUnfollow: Button, userId: String) {
        if (btn_SearchFollow.visibility == View.VISIBLE) {
            firebase.addFollow(userId)
            btn_SearchFollow.visibility = View.GONE
            btn_SearchUnfollow.visibility = View.VISIBLE
        }
    }

    fun removeFollow(firebase: Firebase, btn_SearchFollow: Button, btn_SearchUnfollow: Button, userId: String) {
        if (btn_SearchUnfollow.visibility == View.VISIBLE) {
            firebase.removeFollow(userId)
            btn_SearchFollow.visibility = View.VISIBLE
            btn_SearchUnfollow.visibility = View.GONE
        }
    }

    fun fetchUsers(firebase: Firebase, rv_SearchUser: RecyclerView, et_searchUser: String, activity: FragmentActivity) {
        firebase.fetchUsers(rv_SearchUser, et_searchUser, activity)
    }

    fun openProfile(userID: String, context: Context) {
        val intent = Intent(context, ProfileActivity::class.java)
        intent.putExtra("userId", userID)
        context.startActivity(intent)
    }
}