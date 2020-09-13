package com.example.prymal.viewModel

import android.app.Activity
import android.content.Intent
import android.util.Patterns
import android.widget.EditText
import com.example.prymal.Firebase
import com.example.prymal.view.Register

class LoginViewModel{
    fun login(activity: Activity, et_LoginMail: EditText, et_LoginPassword: EditText, firebase: Firebase) {
        if (et_LoginMail.text.toString().isEmpty()) {
            et_LoginMail.error = "Please enter email"
            et_LoginMail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(et_LoginMail.text.toString()).matches()) {
            et_LoginMail.error = "Please enter valid email"
            et_LoginMail.requestFocus()
            return
        }

        if (et_LoginPassword.text.toString().isEmpty()) {
            et_LoginPassword.error = "Please enter password"
            et_LoginPassword.requestFocus()
            return
        }

        firebase.loginAuth(et_LoginMail.text.toString(), et_LoginPassword.text.toString(), activity)
    }

    fun register(activity: Activity) {
        activity.startActivity(Intent(activity, Register::class.java))
        activity.finish()
    }
}