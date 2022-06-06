package com.example.mydairy

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.tool_main.*

class ViewActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private companion object {
        private const val TAG = "Mainactivity"
    }

    private lateinit var auth: FirebaseAuth

    private lateinit var dbref: DatabaseReference
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<User>
    lateinit var toggle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        //ADD BUTTON
        val add_button = findViewById<Button>(R.id.button)
        add_button.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // FIREBASE
        userRecyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        userRecyclerView.layoutManager = LinearLayoutManager(this)

        userArrayList = arrayListOf<User>()
        getUserData()

        auth = Firebase.auth

        // NAVIGATION BAR

        val drawerLayout: DrawerLayout = findViewById(R.id.nav_menu)
        val navigationView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.Add_notes -> startActivity(Intent(this,MainActivity::class.java))
                R.id.miLogout -> logout()
                R.id.profile -> startActivity(Intent(this, UserActivity::class.java))
                R.id.delete -> startActivity(Intent(this, DeleteActivity::class.java))
                R.id.about -> startActivity(Intent(this, AboutActivity::class.java))
                R.id.github -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/5hrivathsa/My-Diary.git")))
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getUserData() {

        // Add user and setting default values
        auth = Firebase.auth
        val user_id = auth.currentUser?.uid
        dbref =
            FirebaseDatabase.getInstance("https://my-diary-620b5-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("/Users/" + user_id + "/userContent")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(User::class.java)
                        userArrayList.add(user!!)
                    }
                }
                userRecyclerView.adapter = RecyclerAdapter(userArrayList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


    private fun logout() {
        Log.i(TAG, "logout")
        auth.signOut()
        val logoutIntent = Intent(this, LoginActivity::class.java)
        logoutIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(logoutIntent)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }
}