package com.lukefire.dummyevent

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
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

class Event : AppCompatActivity() {
    private var create: Button? = null
    private var poster: ImageView? = null
    private var desc: EditText? = null
    private var link: EditText? = null
    private var name: EditText? = null
    private var mImageUri: Uri? = null
    private var mUploadTask: StorageTask<*>? = null
    private val sAuth: FirebaseAuth? = null
    private var mDatabase: DatabaseReference? = null
    private var database: FirebaseDatabase? = null
    var logou: String? = null
    private var mStorageRef: StorageReference? = null
    private var mStorageRe: StorageReference? = null
    private val mDatabaseRef: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        desc = findViewById<View>(R.id.desc) as EditText
        name = findViewById<View>(R.id.eventName) as EditText
        link = findViewById<View>(R.id.link) as EditText
        create = findViewById<View>(R.id.create) as Button
        poster = findViewById<View>(R.id.poster) as ImageView
        poster!!.setOnClickListener { openFileChooser() }
        create!!.setOnClickListener {
            if (mImageUri != null) {
                createevent()
            } else {
                Toast.makeText(
                    this@Event,
                    "Please select image file for your club's poster",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    private fun createevent() {
        val description = desc!!.text.toString()
        val googleFormLink = link!!.text.toString()
        val eventName = name!!.text.toString()
        if (TextUtils.isEmpty(description) || TextUtils.isEmpty(googleFormLink) || TextUtils.isEmpty(
                eventName
            )
        ) {
            Toast.makeText(this@Event, "fill up the event details completely", Toast.LENGTH_SHORT)
                .show()
        } else {
            if (!URLUtil.isValidUrl(googleFormLink)) {
                Toast.makeText(
                    this@Event,
                    "Enter the correct registration link with https or http initials.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val user = FirebaseAuth.getInstance().currentUser
                val uid = user!!.uid
                database = FirebaseDatabase.getInstance()
                mDatabase = database!!.reference
                val emid = mDatabase!!.child("club").child(uid)
                ///
                val ch1 = mDatabase!!.child("club").child("eventdisplay")
                val ch2 = emid.child("logo_url")
                //String logou;


                ///
                val emid2 = emid.child("events")
                val eventName1 = System.currentTimeMillis().toString()
                val events = emid2.child(eventName1)
                //
                val evedis = ch1.child(eventName1)
                //
                val logo = events.child("event_url")
                val logo1 = evedis.child("event_url")
                events.child("Description").setValue(description)
                //
                evedis.child("Description").setValue(description)
                events.child("Registration_Link").setValue(googleFormLink)
                evedis.child("name").setValue(eventName)
                events.child("name").setValue(eventName)
                evedis.child("EventUID").setValue(eventName1)
                events.child("EventUID").setValue(eventName1)
                ch2.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        logou = dataSnapshot.value.toString()
                        evedis.child("Logo_url").setValue(logou)
                        events.child("Logo_url").setValue(logou)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        logou = "error"
                    }
                })
                evedis.child("Registration_Link").setValue(googleFormLink)
                mStorageRef = FirebaseStorage.getInstance().getReference(uid)
                mStorageRe = mStorageRef!!.child("Events")
                val fileReference =
                    mStorageRe!!.child(eventName + "." + getFileExtension(mImageUri))
                mUploadTask = fileReference.putFile(mImageUri!!)
                    .addOnSuccessListener { //Toast.makeText(clubSignup.this, "Upload successful", Toast.LENGTH_SHORT).show();
                        ////
                        fileReference.downloadUrl.addOnSuccessListener { uri ->
                            logo.setValue(uri.toString())
                            logo1.setValue(uri.toString())
                        }


                        ////

                        //logo.setValue(taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                        Toast.makeText(
                            this@Event,
                            "CLUB EVENT SUCCESSFULLY REGISTERED!!",
                            Toast.LENGTH_LONG
                        ).show()
                        //FirebaseAuth.getInstance().signOut();
                        startActivity(Intent(this@Event, clubAcc::class.java))
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            this@Event,
                            e.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
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
//            Picasso.with(this).load(mImageUri).into(poster)
        }
    }

    private fun getFileExtension(uri: Uri?): String? {
        val cR = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri!!))
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}