package com.lukefire.dummyevent

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.MimeTypeMap
import android.webkit.URLUtil
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.squareup.picasso.Picasso

class EditEvent : AppCompatActivity() {
    private var save: Button? = null
    private var delete: Button? = null
    private var poster: ImageView? = null
    private var desc: EditText? = null
    private var link: EditText? = null
    private var name: EditText? = null
    private var mImageUri: Uri? = null
    private var mUploadTask: StorageTask<*>? = null
    private val sAuth: FirebaseAuth? = null
    private var mDatabase: DatabaseReference? = null
    private var database: FirebaseDatabase? = null
    private var mstorage: FirebaseStorage? = null
    var logou: String? = null
    private var mStorageRef: StorageReference? = null
    private var mStorageRe: StorageReference? = null
    private val mDatabaseRef: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_event)
        val bundle = intent.extras
        val UID = bundle!!.getString("message")
        desc = findViewById<View>(R.id.desc) as EditText
        name = findViewById<View>(R.id.eventName) as EditText
        link = findViewById<View>(R.id.link) as EditText
        save = findViewById<View>(R.id.create) as Button
        poster = findViewById<View>(R.id.poster) as ImageView
        mstorage = FirebaseStorage.getInstance()
        delete = findViewById<View>(R.id.delete) as Button
        poster!!.setOnClickListener { openFileChooser() }
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user!!.uid
        database = FirebaseDatabase.getInstance()
        mDatabase = database!!.reference
        val emid = mDatabase!!.child("club").child(uid).child("events").child(UID!!)
        val emid2 = mDatabase!!.child("club").child("eventdisplay").child(UID)
        emid.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val description = dataSnapshot.child("Description").value.toString()
                    val name1 = dataSnapshot.child("name").value.toString()
                    val link1 = dataSnapshot.child("Registration_Link").value.toString()
                    val imgurl = dataSnapshot.child("event_url").value.toString()
                    //mImageUri=Uri.parse(imgurl);
                    desc!!.setText(description)
                    name!!.setText(name1)
                    link!!.setText(link1)
                    Picasso.get().load(imgurl).into(poster)

                  //  Picasso.with(this@EditEvent).load(imgurl).into(poster)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        save!!.setOnClickListener {
            val description = desc!!.text.toString()
            val googleFormLink = link!!.text.toString()
            val eventName = name!!.text.toString()
            if (TextUtils.isEmpty(description) || TextUtils.isEmpty(googleFormLink) || TextUtils.isEmpty(
                    eventName
                )
            ) {
                Toast.makeText(
                    this@EditEvent,
                    "Fill up the details completely!!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (!URLUtil.isValidUrl(googleFormLink)) {
                    Toast.makeText(
                        this@EditEvent,
                        "Enter the correct registration link with https or http initials.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (mImageUri != null) {
                        mStorageRef = FirebaseStorage.getInstance().getReference(uid)
                        mStorageRe = mStorageRef!!.child("Events")
                        val newEventName = System.currentTimeMillis().toString()
                        val fileReference = mStorageRe!!.child(
                            "$newEventName." + getFileExtension(
                                mImageUri!!
                            )
                        )
                        mUploadTask = fileReference.putFile(mImageUri!!)
                            .addOnSuccessListener { //Toast.makeText(clubSignup.this, "Upload successful", Toast.LENGTH_SHORT).show();
                                ////
                                fileReference.downloadUrl.addOnSuccessListener { uri ->
                                    emid.child("event_url").setValue(uri.toString())
                                    emid2.child("event_url").setValue(uri.toString())
                                }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    this@EditEvent,
                                    e.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                    emid.child("Description").setValue(description)
                    emid.child("Registration_Link").setValue(googleFormLink)
                    emid.child("name").setValue(eventName)
                    emid2.child("Description").setValue(description)
                    emid2.child("Registration_Link").setValue(googleFormLink)
                    emid2.child("name").setValue(eventName)
                    Toast.makeText(
                        this@EditEvent,
                        "Information saved successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@EditEvent, clubAcc::class.java))
                }
            }
        }
        delete!!.setOnClickListener {
            emid.removeValue()
            emid2.removeValue()
            Toast.makeText(this@EditEvent, "Event Deleted Successfully.", Toast.LENGTH_LONG).show()
            startActivity(Intent(this@EditEvent, clubAcc::class.java))
        }
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            mImageUri = data.data
            Picasso.get().load(mImageUri).into(poster)
         //   Picasso.with(this).load(mImageUri).into(poster)
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        val cR = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}