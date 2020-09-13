package com.example.prymal.model

data class Post(var userId: String,
                var uploadId: String,
                var postName: String,
                var imageUrl: String,
                var description: String = "",
                var likes: Long,
                var location: String = "") {
    constructor() : this("", "", "", "", "", -1, "")
}