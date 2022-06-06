package com.example.mydairy

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.icu.text.SimpleDateFormat
import android.nfc.Tag
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import com.example.mydairy.databinding.ActivityLoginBinding
import com.example.mydairy.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.util.*
import java.util.logging.Logger.global
import kotlin.reflect.typeOf

var no = 0
var flag  = true
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser?.uid
        database = FirebaseDatabase.getInstance("https://my-diary-620b5-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("/Users/"+currentUser)
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (flag == true){
                    if (snapshot.exists()) {
                        for (i in snapshot.children) {
                            if (i.key == "entries"){
                                no = (i.value).toString().toInt()
                                println(no)
                                flag = false
                            }
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        val button = findViewById<Button>(R.id.back)

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        val date = currentDate

        binding.saveButton.setOnClickListener {
            val title = binding.titleInput.text.toString()
            val content = binding.notesInput.text.toString()

            database =
                FirebaseDatabase.getInstance("https://my-diary-620b5-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .getReference("Users")
            val user = User( content, date, (no+1),title)
            if (currentUser != null) {
                no ++
                database.child(currentUser).child("userContent").child(no.toString()).setValue(user)
                    .addOnSuccessListener {
                        binding.titleInput.text?.clear()
                        binding.notesInput.text?.clear()

                        database.child(currentUser).child("entries").setValue(no)

                        Toast.makeText(this, "Successfully saved!", Toast.LENGTH_SHORT).show()

                    }.addOnFailureListener {

                        Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        button.setOnClickListener {
            val intent = Intent(this, ViewActivity::class.java)
            startActivity(intent)
        }
    }
}