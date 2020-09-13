package com.example.prymal.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.prymal.Firebase
import com.example.prymal.viewModel.LoginViewModel
import com.example.prymal.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    private var viewModel : LoginViewModel = LoginViewModel()
    private var firebase: Firebase = Firebase()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebase.auth = FirebaseAuth.getInstance()

        btn_LoginLogin.setOnClickListener {
            viewModel.login(this, et_LoginMail, et_LoginPassword, firebase)
        }

        btn_LoginRegister.setOnClickListener {
            viewModel.register(this)
        }
    }

    override fun onStart() {
        super.onStart()
        firebase.checkUser(this)
    }
}