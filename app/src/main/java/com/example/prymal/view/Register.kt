package com.example.prymal.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.prymal.Firebase
import com.example.prymal.R
import com.example.prymal.viewModel.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {

    private var viewModel: RegisterViewModel = RegisterViewModel()
    private var firebase: Firebase = Firebase()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        firebase.auth = FirebaseAuth.getInstance()
        firebase.db = FirebaseDatabase.getInstance()

        btn_RegisterRegister.setOnClickListener {
            viewModel.registerUser(firebase, this, et_RegisterMail, et_RegisterPassword, et_RegisterUserName.text.toString(), et_RegisterUserSurname.text.toString(), et_RegisterPetName.text.toString(), pb_Register)
        }
    }
}