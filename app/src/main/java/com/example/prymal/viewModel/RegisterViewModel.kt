package com.example.prymal.viewModel

import android.app.Activity
import android.util.Patterns
import android.widget.EditText
import android.widget.ProgressBar
import com.example.prymal.Firebase

class RegisterViewModel {

    fun registerUser(firebase: Firebase, activity: Activity, et_RegisterMail: EditText, et_RegisterPassword: EditText, userName: String, userSurname: String, petName: String, pb_Register: ProgressBar) {
        if (et_RegisterMail.text.toString().isEmpty()) {
            et_RegisterMail.error = "Please enter email"
            et_RegisterMail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(et_RegisterMail.text.toString()).matches()) {
            et_RegisterMail.error = "Please enter valid email"
            et_RegisterMail.requestFocus()
            return
        }

        if (et_RegisterPassword.text.toString().isEmpty()) {
            et_RegisterPassword.error = "Please enter password"
            et_RegisterPassword.requestFocus()
            return
        }

        firebase.registration(et_RegisterMail.text.toString(), et_RegisterPassword.text.toString(), activity, userName, userSurname, petName, pb_Register)
    }
}