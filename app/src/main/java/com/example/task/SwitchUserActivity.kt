package com.example.task

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.namespace.R
import io.realm.Realm

class SwitchUserActivity : AppCompatActivity() {

    private lateinit var realm: Realm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_switch_user)
        realm = Realm.getDefaultInstance()

        val userListView = findViewById<ListView>(R.id.userListView)

        val users = realm.where(User::class.java).findAll()
        val usernames = users.map { it.username }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, usernames)
        userListView.adapter = adapter

        userListView.setOnItemClickListener { _, _, position, _ ->
            val selectedUser = users[position]
            val intent = Intent(this, LoginActivity::class.java)
            if (selectedUser != null) {
                intent.putExtra("username", selectedUser.username)
            }
            startActivity(intent)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}