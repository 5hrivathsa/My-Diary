package com.example.mydairy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.mydairy.databinding.ActivityDeleteBinding
import com.example.mydairy.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class DeleteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeleteBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.delete.setOnClickListener{

            val pgno = binding.pageno.text.toString()
            if (pgno.isNotEmpty()){
                deletedata(pgno)
            }
            else{
                Toast.makeText(this,"Enter a valid page number",Toast.LENGTH_SHORT).show()
            }
        }

        //BACK BUTTON
        val back_button = findViewById<Button>(R.id.back)
        back_button.setOnClickListener{
            val intent = Intent(this, ViewActivity::class.java)
            startActivity(intent)
        }
    }

    private fun deletedata(pgno: String){
        auth = Firebase.auth
        val user_id = auth.currentUser?.uid
        database = FirebaseDatabase.getInstance("https://my-diary-620b5-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("/Users/"+user_id+"/userContent")
        database.child(pgno).removeValue().addOnSuccessListener{
            binding.pageno.text?.clear()
            Toast.makeText(this,"The page has been torn off",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this,"Enter a valid page no",Toast.LENGTH_SHORT).show()
        }
    }
}