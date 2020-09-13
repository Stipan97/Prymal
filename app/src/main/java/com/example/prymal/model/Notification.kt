package com.example.prymal.model

data class Notification(var fromUserId: String,
                        var toUserId: String,
                        var type: String,
                        var uploadKey: String) {
    constructor() : this("", "", "", "")
}