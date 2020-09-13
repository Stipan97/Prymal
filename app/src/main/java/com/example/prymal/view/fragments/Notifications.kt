package com.example.prymal.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.prymal.Firebase
import com.example.prymal.R
import com.example.prymal.viewModel.fragments.NotificationsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.notifications_fragment.*

class Notifications : Fragment() {

    private lateinit var viewModel: NotificationsViewModel
    private var firebase: Firebase = Firebase()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.notifications_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = NotificationsViewModel()

        firebase.auth = FirebaseAuth.getInstance()
        firebase.db = FirebaseDatabase.getInstance()

        viewModel.fetchNotifs(firebase, rv_notifications, activity!!)
    }

}