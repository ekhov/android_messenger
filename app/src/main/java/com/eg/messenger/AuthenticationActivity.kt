package com.eg.messenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AuthenticationActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        auth = Firebase.auth
        database = Firebase.database.reference
    }

    fun signInUser(view: View) {
        val email = findViewById<EditText>(R.id.emailInput).text.toString()
        val password = findViewById<EditText>(R.id.passwordInput).text.toString()

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this)
        { task ->
            if (task.isSuccessful) startActivity(Intent(this, DialogActivity::class.java))
            else Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()
        }
    }

    fun signUpUser(view: View) {
        val email = findViewById<EditText>(R.id.emailInput).text.toString()
        val password = findViewById<EditText>(R.id.passwordInput).text.toString()

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this)
        {
            task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, DialogActivity::class.java))
                    val key = database.child("users").push().key
                    val userId = auth.currentUser?.uid

                    val newUser = User(userId, email, email)
                    database.updateChildren(mutableMapOf<String, Any?>("/users/$key" to newUser.toMap()))
                }
                else Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()
        }
    }
}