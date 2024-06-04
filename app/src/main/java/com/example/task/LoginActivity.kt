package com.example.task

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.namespace.R
import io.realm.Realm

class LoginActivity : AppCompatActivity() {
    private lateinit var realm: Realm

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        realm = Realm.getDefaultInstance()

        val usernameEditText = findViewById<EditText>(R.id.username)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val switchUserButton = findViewById<Button>(R.id.switchUserButton)
        val prefillUsername = intent.getStringExtra("username")
        val signUpButton = findViewById<Button>(R.id.signUpButton)

        if (prefillUsername != null) {
            usernameEditText.setText(prefillUsername)
        }

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                loginUser(username, password)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        switchUserButton.setOnClickListener {
            val intent = Intent(this, SwitchUserActivity::class.java)
            startActivity(intent)
        }
        signUpButton.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(username: String, password: String) {
        val user = realm.where(User::class.java)
            .equalTo("username", username)
            .equalTo("password", password)
            .findFirst()
        if (user != null) {
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LocationMainActivity::class.java)
            startActivity(intent)

        } else {
            Toast.makeText(this, "Invalid credentials... Please SignUp", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

}

