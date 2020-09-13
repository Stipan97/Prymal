package com.example.prymal.view.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.prymal.Firebase
import com.example.prymal.R
import com.example.prymal.viewModel.fragments.SearchViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.search_fragment.*

class Search : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private var firebase: Firebase = Firebase()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = SearchViewModel()

        firebase.auth = FirebaseAuth.getInstance()
        firebase.db = FirebaseDatabase.getInstance()

        viewModel.fetchUsers(firebase, rv_searchUser, et_searchUser.text.toString(), activity!!)
        et_searchUser.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                viewModel.fetchUsers(firebase, rv_searchUser, s.toString(), activity!!)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.fetchUsers(firebase, rv_searchUser, s.toString(), activity!!)
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.fetchUsers(firebase, rv_searchUser, s.toString(), activity!!)
            }
        })
    }

}