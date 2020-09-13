package com.example.prymal.model

data class User(var firstName: String,
                var lastName: String,
                var petName: String,
                var searchName: String,
                var userID: String?,
                var imageUrl: String = "https://firebasestorage.googleapis.com/v0/b/dogbreedbook-57bd9.appspot.com/o/profileHolder.png?alt=media&token=81e64d5e-df60-42ab-8d2f-7237790d1f3e",
                var imageDogUrl: String = "https://firebasestorage.googleapis.com/v0/b/dogbreedbook-57bd9.appspot.com/o/dogProfileHolder.png?alt=media&token=df44cdab-ac82-4c5f-900d-b4c141cc2599",
                var following: Long = 0,
                var followers: Long = 0) {
    constructor() : this("", "", "", "", "", "", "", 0, 0)
}