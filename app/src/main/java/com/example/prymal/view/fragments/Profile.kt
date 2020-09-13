package com.example.prymal.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.prymal.Firebase
import com.example.prymal.viewModel.fragments.ProfileViewModel
import com.example.prymal.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.profile_fragment.*

class Profile : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    private var firebase: Firebase = Firebase()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ProfileViewModel()

        firebase.auth = FirebaseAuth.getInstance()
        firebase.db = FirebaseDatabase.getInstance()

        viewModel.setupProfileInfo(firebase, activity!!, firebase.auth.currentUser!!.uid)
        viewModel.fetchImages(firebase, rv_profileImages, activity!!, firebase.auth.currentUser!!.uid)
    }

}