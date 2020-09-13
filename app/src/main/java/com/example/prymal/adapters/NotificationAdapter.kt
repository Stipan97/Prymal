package com.example.prymal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.prymal.Firebase
import com.example.prymal.R
import com.example.prymal.model.Notification
import com.example.prymal.model.User
import com.example.prymal.viewModel.fragments.NotificationsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.item_notification.view.*

class NotificationAdapter(private val notifs: MutableList<Notification>, private val users: List<User>) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    private lateinit var viewModel: NotificationsViewModel
    private var firebase: Firebase = Firebase()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false))
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        firebase.auth = FirebaseAuth.getInstance()
        firebase.db = FirebaseDatabase.getInstance()

        Glide.with(holder.view.context).load(users[position].imageUrl).into(holder.view.iv_notifImage)
        holder.view.tv_notifUserName.text = users[position].searchName
        holder.view.tv_notifType.text = notifs[position].type

        viewModel = NotificationsViewModel()
        holder.view.btn_notifDelete.setOnClickListener {
            viewModel.removeNotif(firebase, notifs, position, this)
        }
    }

    override fun getItemCount() = notifs.size

    class NotificationViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}