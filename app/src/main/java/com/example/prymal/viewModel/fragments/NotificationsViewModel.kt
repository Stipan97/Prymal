package com.example.prymal.viewModel.fragments

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.prymal.Firebase
import com.example.prymal.adapters.NotificationAdapter
import com.example.prymal.model.Notification

class NotificationsViewModel : ViewModel() {
    fun removeNotif(
        firebase: Firebase,
        notifs: MutableList<Notification>,
        position: Int,
        notificationAdapter: NotificationAdapter
    ) {
        firebase.removeNotif(notifs[position])
        notifs.removeAt(position)
        notificationAdapter.notifyItemRemoved(position)
        notificationAdapter.notifyItemRangeChanged(position, notifs.size)
    }

    fun fetchNotifs(firebase: Firebase, rv_Notifications: RecyclerView, activity: FragmentActivity) {
        firebase.fetchNotifs(rv_Notifications, activity)
    }
}