package com.example.task

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.namespace.R
import io.realm.Realm
import java.util.UUID

class SignUp : AppCompatActivity() {
    private lateinit var realm: Realm

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        realm = Realm.getDefaultInstance()

        val usernameEditText = findViewById<EditText>(R.id.username)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val signUpButton = findViewById<Button>(R.id.signUpButton)

        signUpButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                signUpUser(username, password)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signUpUser(username: String, password: String) {
        Realm.getDefaultInstance().executeTransactionAsync({ realm ->
            val user = realm.where(User::class.java).equalTo("username", username).findFirst()
            if (user == null) {
                val newUser = realm.createObject(User::class.java, UUID.randomUUID().toString())
                newUser.username = username
                newUser.password = password
            } else {
                throw IllegalArgumentException("Username already exists")
            }
        }, {
            Toast.makeText(this, "User signed up successfully", Toast.LENGTH_SHORT).show()
            finish()
        }, { error ->
            if (error is IllegalArgumentException) {
                Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Sign up failed: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

}


