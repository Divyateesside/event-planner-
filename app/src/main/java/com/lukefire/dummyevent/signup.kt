package com.lukefire.dummyevent

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class signup : AppCompatActivity() {
    private var student: Button? = null
    private var club: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        student = findViewById<View>(R.id.stud) as Button
        club = findViewById<View>(R.id.club) as Button
        student!!.setOnClickListener {
            startActivity(
                Intent(
                    this@signup,
                    studentSignup::class.java
                )
            )
        }
        club!!.setOnClickListener { startActivity(Intent(this@signup, clubSignup::class.java)) }
    }
}