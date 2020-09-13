package com.example.prymal.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.prymal.Firebase
import com.example.prymal.R
import com.example.prymal.viewModel.LoginViewModel
import com.example.prymal.viewModel.ProfileActivityViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.profile_fragment.*

class ProfileActivity : AppCompatActivity() {

    var viewModel: ProfileActivityViewModel = ProfileActivityViewModel()
    var firebase: Firebase = Firebase()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_fragment)

        firebase.auth = FirebaseAuth.getInstance()
        firebase.db = FirebaseDatabase.getInstance()

        val userId = intent.getStringExtra("userId")
        viewModel.setupProfileInfo(firebase, this, userId)
        viewModel.fetchImages(firebase, rv_profileImages, this, userId!!)
    }
}