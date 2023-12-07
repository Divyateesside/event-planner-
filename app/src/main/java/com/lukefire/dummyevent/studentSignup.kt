package com.lukefire.dummyevent

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class studentSignup : AppCompatActivity() {
    private var studname: EditText? = null
    private var studreg: EditText? = null
    private var studmail: EditText? = null
    private var studpwd: EditText? = null
    private var confirmpwd: EditText? = null
    private var studroom: EditText? = null
    private var studphno: EditText? = null
    private var sAuth: FirebaseAuth? = null
    private var mDatabase: DatabaseReference? = null
    private var database: FirebaseDatabase? = null
    private var studsignup: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_signup)
        studname = findViewById<View>(R.id.studname) as EditText
        studreg = findViewById<View>(R.id.studreg) as EditText
        studmail = findViewById<View>(R.id.studemail) as EditText
        studpwd = findViewById<View>(R.id.studPwd) as EditText
        confirmpwd = findViewById<View>(R.id.confirmPwd) as EditText
        studroom = findViewById<View>(R.id.studroom) as EditText
        studphno = findViewById<View>(R.id.studPhone) as EditText
        studsignup = findViewById<View>(R.id.studSignup) as Button
        sAuth = FirebaseAuth.getInstance()
        studsignup!!.setOnClickListener { registerstud() }
    }

    private fun registerstud() {
        val mail = studmail!!.text.toString()
        val username = studname!!.text.toString()
        val userreg = studreg!!.text.toString()
        val pwd = studpwd!!.text.toString()
        val cfmpwd = confirmpwd!!.text.toString()
        val room = studroom!!.text.toString()
        val phno = studphno!!.text.toString()
        if (TextUtils.isEmpty(mail) || TextUtils.isEmpty(username) || TextUtils.isEmpty(userreg) || TextUtils.isEmpty(
                pwd
            ) || TextUtils.isEmpty(cfmpwd) || TextUtils.isEmpty(room) || TextUtils.isEmpty(phno)
        ) {
            Toast.makeText(this@studentSignup, "fill up the details completely", Toast.LENGTH_SHORT)
                .show()
        } else {
            if (pwd.length < 6) {
                Toast.makeText(
                    this@studentSignup,
                    "password must contain atleast 6 character",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (pwd == cfmpwd) {
                    sAuth!!.createUserWithEmailAndPassword(mail, pwd)
                        .addOnCompleteListener(this@studentSignup) { task ->
                            if (task.isSuccessful) {
                                val user = FirebaseAuth.getInstance().currentUser
                                val uid = user!!.uid
                                database = FirebaseDatabase.getInstance()
                                mDatabase = database!!.reference
                                //mDatabase.child(uid);
                                val emid = mDatabase!!.child("user").child(uid)
                                val flag = mDatabase!!.child("deptCheck").child(uid)
                                flag.setValue("0")
                                emid.child("email").setValue(mail)
                                emid.child("name").setValue(username)
                                emid.child("reg").setValue(userreg)
                                emid.child("room no").setValue(mail)
                                emid.child("phno").setValue(phno)
                                Toast.makeText(
                                    this@studentSignup,
                                    "SUCCESSFULLY REGISTERED!!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                FirebaseAuth.getInstance().signOut()
                                startActivity(Intent(this@studentSignup, MainActivity::class.java))
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(
                                    this@studentSignup,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        this@studentSignup,
                        "password confirmation failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}