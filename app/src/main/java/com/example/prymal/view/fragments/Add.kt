package com.example.prymal.view.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.prymal.Firebase
import com.example.prymal.viewModel.fragments.AddViewModel
import com.example.prymal.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.add_fragment.*

class Add : Fragment() {

    private lateinit var viewModel: AddViewModel
    private var firebase: Firebase = Firebase()

    private lateinit var imageUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = AddViewModel()

        firebase.auth = FirebaseAuth.getInstance()
        firebase.db = FirebaseDatabase.getInstance()
        firebase.storage = FirebaseStorage.getInstance()

        btn_AddChoose.setOnClickListener {
            chooseImage()
        }

        btn_AddUploadPost.setOnClickListener {
            pb_Upload.visibility = View.VISIBLE
            viewModel.uploadImage(firebase, imageUri, context!!, et_AddDescription.text.toString(), pb_Upload, cb_Location.text.toString())
        }

        btn_AddUploadUser.setOnClickListener {
            pb_Upload.visibility = View.VISIBLE
            viewModel.uploadImage(firebase, imageUri, context!!, pb_Upload, true)
        }

        btn_AddUploadPet.setOnClickListener {
            pb_Upload.visibility = View.VISIBLE
            viewModel.uploadImage(firebase, imageUri, context!!, pb_Upload, false)
        }

        cb_Location.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.handleLocation(activity!!, context!!)
            } else {
                cb_Location.text = getText(R.string.add_your_location)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == -1 && data != null && data.data != null) {
            imageUri = data.data!!
            iv_AddPreview.setImageURI(imageUri)
        }
    }

    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 1)
    }
}