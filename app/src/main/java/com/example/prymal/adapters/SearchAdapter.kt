package com.example.prymal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.prymal.Firebase
import com.example.prymal.R
import com.example.prymal.model.User
import com.example.prymal.viewModel.fragments.SearchViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.item_user_search.view.*

class SearchAdapter(private val users: List<User>) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private lateinit var viewModel: SearchViewModel
    private var firebase: Firebase = Firebase()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user_search, parent, false))
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val user = users[position]
        val fullName = user.searchName
        holder.view.tv_searchFullName.text = fullName
        Glide.with(holder.view.context).load(user.imageUrl).into(holder.view.iv_searchUser)
        holder.view.btn_searchFollow.visibility = View.VISIBLE

        firebase.auth = FirebaseAuth.getInstance()
        firebase.db = FirebaseDatabase.getInstance()

        viewModel = SearchViewModel()
        viewModel.isFollowing(firebase, holder.view.btn_searchFollow, holder.view.btn_searchUnfollow, user.userID!!)

        holder.itemView.setOnClickListener {
            viewModel.openProfile(user.userID!!, holder.view.context)
        }

        holder.view.btn_searchFollow.setOnClickListener {
            viewModel.addFollow(firebase, holder.view.btn_searchFollow, holder.view.btn_searchUnfollow, user.userID!!)
        }

        holder.view.btn_searchUnfollow.setOnClickListener {
            viewModel.removeFollow(firebase, holder.view.btn_searchFollow, holder.view.btn_searchUnfollow, user.userID!!)
        }
    }

    override fun getItemCount() = users.size

    class SearchViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}