package com.example.prymal

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.prymal.adapters.NotificationAdapter
import com.example.prymal.adapters.PostAdapter
import com.example.prymal.adapters.ProfileImagesAdapter
import com.example.prymal.adapters.SearchAdapter
import com.example.prymal.model.Notification
import com.example.prymal.model.Post
import com.example.prymal.model.User
import com.example.prymal.view.Login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.item_post.view.*
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlin.properties.Delegates


class Firebase {
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseDatabase
    lateinit var storage: FirebaseStorage

    private lateinit var postName: String
    private var liked by Delegates.notNull<Boolean>()

    fun registration(email: String, password: String, activity: Activity, userName: String, userSurname: String, petName: String, pb_Register: ProgressBar) {
        pb_Register.visibility = View.VISIBLE
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    addUserIntoDatabase(userName, userSurname, petName)
                    activity.startActivity(Intent(activity, Login::class.java))
                    pb_Register.visibility = View.INVISIBLE
                    activity.finish()
                } else {
                    Toast.makeText(
                        activity,
                        "Register failed! Please try again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun loginAuth(email: String, password: String, activity: Activity) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user, activity)
                } else {
                    updateUI(null, activity)
                    Toast.makeText(activity, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun checkUser(activity: Activity) {
        val user = auth.currentUser
        updateUI(user, activity)
    }

    fun logout(activity: Activity) {
        auth.signOut()
        activity.startActivity(Intent(activity, Login::class.java))
        activity.finish()
    }

    fun fetchPosts(rv_Posts: RecyclerView, activity: FragmentActivity) {
        val posts: ArrayList<Post> = ArrayList()
        val following: ArrayList<String> = ArrayList()

        val currentUser: String = auth.currentUser!!.uid

        db.getReference("Follow").child(currentUser).child("Following").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                following.clear()
                //ljudi koje prati
                for (snapshot in p0.children) {
                    following.add(snapshot.key!!)
                }

                db.getReference("Post").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        posts.clear()
                        for (snapshot in p0.children) {
                            //vlastite slike
                            if (currentUser == snapshot.key) {
                                for (item in snapshot.children) {
                                    posts.add(item.getValue(Post::class.java)!!)
                                }
                            }
                            //slike ljudi koje prati
                            for (i in 0 until following.size) {
                                if (following[i] == snapshot.key) {
                                    for (item in snapshot.children) {
                                        posts.add(item.getValue(Post::class.java)!!)
                                    }
                                }
                            }
                        }
                        val postAdapter = PostAdapter(posts)
                        rv_Posts.layoutManager = LinearLayoutManager(activity)
                        rv_Posts.adapter = postAdapter
                    }

                    override fun onCancelled(p0: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun isLiked(uploadId: String, btnPostLike: Button, context: Context) {
        val likedUploadId: ArrayList<String> = ArrayList()

        db.getReference("UserLikes").child(auth.currentUser!!.uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                likedUploadId.clear()
                for (snapshot in p0.children) {
                    likedUploadId.add(snapshot.key!!)
                }
                if (likedUploadId.contains(uploadId)) {
                    btnPostLike.background = context.resources.getDrawable(R.drawable.ic_action_liked)
                } else {
                    btnPostLike.background = context.resources.getDrawable(R.drawable.ic_action_unliked)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun fetchImages(rv_ProfileImages: RecyclerView, activity: FragmentActivity, userId: String) {
        val posts: ArrayList<Post> = ArrayList()

        db.getReference("Post").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                posts.clear()
                for (snapshot in p0.children) {
                    posts.add(snapshot.getValue(Post::class.java)!!)
                }
                val profileImagesAdapter = ProfileImagesAdapter(posts)
                rv_ProfileImages.layoutManager = GridLayoutManager(activity, 3)
                rv_ProfileImages.adapter = profileImagesAdapter
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun fetchProfileInfo(activity: Activity, userId: String) {
        db.getReference("users").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val user: User = p0.getValue(User::class.java)!!
                Glide.with(activity).load(user.imageUrl).into(activity.iv_profileUserImage)
                Glide.with(activity).load(user.imageDogUrl).into(activity.iv_profilePetImage)
                activity.tv_profileUserName.text = user.searchName
                activity.tv_profilePetName.text = user.petName
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun fetchUsers(rv_SearchUser: RecyclerView, et_searchUser: String, activity: FragmentActivity) {
        val users: ArrayList<User> = ArrayList()

        db.getReference("users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                users.clear()
                for (snapshot in p0.children) {
                    val user = snapshot.getValue(User::class.java)!!
                    if ((user.searchName.toUpperCase()).contains(et_searchUser.toUpperCase())) {
                        users.add(user)
                    }
                }
                val searchAdapter = SearchAdapter(users)
                rv_SearchUser.layoutManager = LinearLayoutManager(activity)
                rv_SearchUser.adapter = searchAdapter
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun fetchNotifs(rv_notifs: RecyclerView, activity: FragmentActivity) {
        val notifs: ArrayList<Notification> = ArrayList()
        val users: ArrayList<User> = ArrayList()

        val currentUser: String = auth.currentUser!!.uid

        db.getReference("Notification").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                notifs.clear()
                for (snapshot in p0.children) {
                    val notif = snapshot.getValue(Notification::class.java)!!
                    if (currentUser == notif.toUserId) {
                        notifs.add(notif)
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        db.getReference("users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (snapshotUser in p0.children) {
                    for (i in 0 until notifs.size)
                        if (notifs[i].fromUserId == snapshotUser.key) {
                            val user = snapshotUser.getValue(User::class.java)!!
                            users.add(user)
                        }
                }
                val notifAdapter = NotificationAdapter(notifs, users)
                rv_notifs.layoutManager = LinearLayoutManager(activity)
                rv_notifs.adapter = notifAdapter
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun uploadImage(imageUri: Uri, context: Context, description: String, pb_Upload: ProgressBar, location: String) {
        val imgRef = storage.getReference("images").child(System.currentTimeMillis().toString() + "." + getFileExtension(imageUri, context))

        imgRef.putFile(imageUri).addOnSuccessListener {
            imgRef.downloadUrl.addOnSuccessListener {
                val post = Post(auth.uid.toString(), db.reference.push().key!!, postName, it.toString(), description, 0, location)
                db.getReference("Post").child(auth.uid.toString()).child(post.uploadId).setValue(post)
                pb_Upload.visibility = View.INVISIBLE
            }
        }
    }

    fun updateProfileImages(imageUri: Uri, context: Context, pb_Upload: ProgressBar, whichImage: Boolean) {
        val imgRef = storage.getReference("images").child(System.currentTimeMillis().toString() + "." + getFileExtension(imageUri, context))
        val userRef = db.getReference("users").child(auth.uid.toString())

        imgRef.putFile(imageUri).addOnSuccessListener {
            imgRef.downloadUrl.addOnSuccessListener {
                if (whichImage) {
                    userRef.child("imageUrl").setValue(it.toString())
                } else {
                    userRef.child("imageDogUrl").setValue(it.toString())
                }
                pb_Upload.visibility = View.INVISIBLE
            }
        }
    }

    fun getPostName() {
        val currentUser: String = auth.currentUser!!.uid

        db.getReference("users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (snapshot in p0.children) {
                    if (currentUser == snapshot.key) {
                        val user = snapshot.getValue(User::class.java)
                        if (user != null) {
                            postName = user.firstName + " " + user.lastName + " - " + user.petName
                        }
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun isFollow(btn_SearchFollow: Button, btn_SearchUnfollow: Button, userId: String) {
        val currentUser: String = auth.currentUser!!.uid
        val followRef = db.getReference("Follow").child(currentUser).child("Following")

        followRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.child(userId).exists()) {
                    btn_SearchFollow.visibility = View.GONE
                    btn_SearchUnfollow.visibility = View.VISIBLE
                } else {
                    btn_SearchFollow.visibility = View.VISIBLE
                    btn_SearchUnfollow.visibility = View.GONE
                }

                if (userId == currentUser) {
                    btn_SearchFollow.visibility = View.GONE
                    btn_SearchUnfollow.visibility = View.GONE
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun addFollow(userId: String) {
        val currentUser: String = auth.currentUser!!.uid
        val followRef = db.getReference("Follow")
        val uploadId = db.getReference("Notification").push().key
        val notifRef = db.getReference("Notification").child(uploadId!!)

        followRef.child(currentUser).child("Following").child(userId).setValue(true)
        followRef.child(userId).child("Followers").child(currentUser).setValue(true)

        notifRef.setValue(Notification(currentUser, userId, "started following you!", uploadId))

        following(currentUser, true)
        follower(userId, true)
    }

    fun removeFollow(userId: String) {
        val currentUser: String = auth.currentUser!!.uid
        val followRef = db.getReference("Follow")

        followRef.child(currentUser).child("Following").child(userId).removeValue()
        followRef.child(userId).child("Followers").child(currentUser).removeValue()

        following(currentUser, false)
        follower(userId, false)
    }

    fun handleLike(uploadId: String, btnPostLike: Button, context: Context) {
        val likes: ArrayList<String> = ArrayList()

        db.getReference("UserLikes").child(auth.currentUser!!.uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                likes.clear()
                for (snapshot in p0.children) {
                    if (snapshot.key != null) {
                        likes.add(snapshot.key!!)
                    }
                }

                liked = if (likes.contains(uploadId)) {
                    db.getReference("UserLikes").child(auth.currentUser!!.uid).child(uploadId).removeValue()
                    btnPostLike.background = context.resources.getDrawable(R.drawable.ic_action_unliked)
                    true
                } else {
                    db.getReference("UserLikes").child(auth.currentUser!!.uid).child(uploadId).child("liked").setValue(true)
                    btnPostLike.background = context.resources.getDrawable(R.drawable.ic_action_liked)
                    false
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun handleLikeInDB(uploadId: String, postUserId: String, view: View) {
        val likeRef =  db.getReference("Post").child(postUserId)
        val uploadNotifId = db.getReference("Notification").push().key!!
        val notifRef = db.getReference("Notification").child(uploadNotifId)
        val currentUser: String = auth.currentUser!!.uid

        likeRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (snapshot in p0.children) {
                    val post = snapshot.getValue(Post::class.java)!!
                    if (post.uploadId == uploadId) {
                        if(liked) {
                            db.getReference("Post").child(postUserId).child(uploadId).child("likes").setValue(post.likes - 1)
                        } else {
                            db.getReference("Post").child(postUserId).child(uploadId).child("likes").setValue(post.likes + 1)
                            notifRef.setValue(Notification(currentUser, postUserId, "liked your post!", uploadNotifId))
                        }
                    }
                }
                refreshPostInfo(postUserId, uploadId, view)
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun removeNotif(notification: Notification) {
        db.getReference("Notification").child(notification.uploadKey).removeValue()
    }

    private fun getFileExtension(imageUri: Uri, context: Context): String {
        val contentResolver = context.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri))!!
    }

    private fun updateUI(currentUser: FirebaseUser?, activity: Activity) {
        if (currentUser != null) {
            activity.startActivity(Intent(activity, Navigation::class.java))
            activity.finish()
        }
    }

    private fun addUserIntoDatabase(userName: String, userSurname: String, petName: String) {
        val userId: String? = auth.uid
        val user = User(userName, userSurname, petName, "$userName $userSurname", userId)

        if (userId != null) {
            db.getReference("users").child(userId).setValue(user)

            db.getReference("followCounter").child(userId).child("followersCounter").setValue(0)
            db.getReference("followCounter").child(userId).child("followingCounter").setValue(0)
        }
    }

    private fun following(userId: String, state: Boolean) {
        val followCounterRef = db.getReference("users").child(userId)

        followCounterRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                var following = p0.getValue(User::class.java)!!.following
                if (state) {
                    following++
                } else {
                    following--
                }
                followCounterRef.child("following").setValue(following)
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun follower(userId: String, state: Boolean) {
        val followCounterRef = db.getReference("users").child(userId)

        followCounterRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                var followers = p0.getValue(User::class.java)!!.followers
                if (state) {
                    followers++
                } else {
                    followers--
                }
                followCounterRef.child("followers").setValue(followers)
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun refreshPostInfo(userId: String, uploadId: String, view: View) {
        db.getReference("Post").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (snapshot in p0.children) {
                    if (snapshot.key == uploadId) {
                        val post = snapshot.getValue(Post::class.java)!!
                        var postLikes = ""
                        postLikes = if (post.likes == 1.toLong()) {
                            post.likes.toString() + " like"
                        } else {
                            post.likes.toString() + " likes"
                        }
                        view.tv_postLikes.text = postLikes
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}