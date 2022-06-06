package com.example.mydairy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        text_name.text = currentUser?.displayName
        text_email.text = currentUser?.email
        Glide.with(this).load(currentUser?.photoUrl).into(profile_image)

        //Backy BUTTON
        val back_button = findViewById<ImageButton>(R.id.backy)
        back_button.setOnClickListener{
            val intent = Intent(this, ViewActivity::class.java)
            startActivity(intent)
        }
    }
}