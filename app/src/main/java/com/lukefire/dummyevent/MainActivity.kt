package com.lukefire.dummyevent

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private var sign: Button? = null
    private var login: Button? = null
    private var email: EditText? = null
    private var pwd: EditText? = null
    private val toggle: Switch? = null
    private var mAuth: FirebaseAuth? = null
    private var mDatabase: DatabaseReference? = null
    private var database: FirebaseDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sign = findViewById<View>(R.id.signup) as Button
        login = findViewById<View>(R.id.login) as Button
        email = findViewById<View>(R.id.email) as EditText
        pwd = findViewById<View>(R.id.pwd) as EditText
        //toggle = (Switch)findViewById(R.id.toggle);
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        mDatabase = database!!.reference
        login!!.setOnClickListener { start_login() }
        sign!!.setOnClickListener { startActivity(Intent(this@MainActivity, signup::class.java)) }
    }

    fun start_login() {
        val emailid = email!!.text.toString()
        val pswd = pwd!!.text.toString()
        if (TextUtils.isEmpty(emailid) || TextUtils.isEmpty(pswd)) {
            Toast.makeText(this@MainActivity, "incomplete details", Toast.LENGTH_LONG).show()
        } else {
            mAuth!!.signInWithEmailAndPassword(emailid, pswd)
                .addOnCompleteListener(this@MainActivity) { task ->
                    if (task.isSuccessful) {
                        val user = mAuth!!.currentUser
                        val uid = user!!.uid
                        val emid = mDatabase!!.child("deptCheck").child(uid)
                        emid.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val check = dataSnapshot.value.toString()
                                if (check == "0") {
                                    startActivity(
                                        Intent(
                                            this@MainActivity,
                                            StudAccount::class.java
                                        )
                                    )
                                } else {
                                    startActivity(Intent(this@MainActivity, clubAcc::class.java))
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {}
                        })
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth!!.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            val emid = mDatabase!!.child("deptCheck").child(uid)
            emid.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val check = dataSnapshot.value.toString()
                    if (check == "0") {
                        startActivity(Intent(this@MainActivity, StudAccount::class.java))
                    } else {
                        startActivity(Intent(this@MainActivity, clubAcc::class.java))
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}