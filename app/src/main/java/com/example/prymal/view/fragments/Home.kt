package com.example.prymal.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.prymal.Firebase
import com.example.prymal.viewModel.fragments.HomeViewModel
import com.example.prymal.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.item_post.*

class Home : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private var firebase: Firebase = Firebase()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = HomeViewModel()

        firebase.auth = FirebaseAuth.getInstance()
        firebase.db = FirebaseDatabase.getInstance()

        sr_Home.setOnRefreshListener {
            viewModel.fetchPosts(firebase, rv_Posts, activity!!)

            sr_Home.isRefreshing = false
        }

        viewModel.fetchPosts(firebase, rv_Posts, activity!!)
    }

}