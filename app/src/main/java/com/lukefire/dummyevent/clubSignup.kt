package com.lukefire.dummyevent

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.squareup.picasso.Picasso

class clubSignup : AppCompatActivity() {
    private var chooseImage: Button? = null
    private var uploadImageView: ImageView? = null
    private var clubname: EditText? = null
    private var clubemail: EditText? = null
    private var clubpwd: EditText? = null
    private var clubcfmpwd: EditText? = null
    private var sAuth: FirebaseAuth? = null
    private var mDatabase: DatabaseReference? = null
    private var database: FirebaseDatabase? = null
    private var clubsignup: Button? = null
    private var mUploadTask: StorageTask<*>? = null
    private var mImageUri: Uri? = null
    private var mStorageRef: StorageReference? = null
    private var mStorageRe: StorageReference? = null
    private val mDatabaseRef: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_club_signup)
        clubname = findViewById<View>(R.id.clubname) as EditText
        clubemail = findViewById<View>(R.id.clubemail) as EditText
        clubpwd = findViewById<View>(R.id.clubpwd) as EditText
        clubcfmpwd = findViewById<View>(R.id.clubcfmpwd) as EditText
        clubsignup = findViewById<View>(R.id.clubsignup) as Button
        chooseImage = findViewById<View>(R.id.chooseImage) as Button
        uploadImageView = findViewById<View>(R.id.uploadimg) as ImageView
        sAuth = FirebaseAuth.getInstance()
        mStorageRef = FirebaseStorage.getInstance().getReference("clubs")
        chooseImage!!.setOnClickListener { openFileChooser() }
        clubsignup!!.setOnClickListener {
            if (mImageUri != null) {
                registerclub()
            } else {
                Toast.makeText(
                    this@clubSignup,
                    "Please select image file for your club's logo",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun registerclub() {
        val mail = clubemail!!.text.toString()
        val cluname = clubname!!.text.toString()
        val pwd = clubpwd!!.text.toString()
        val cfmpwd = clubcfmpwd!!.text.toString()
        if (TextUtils.isEmpty(mail) || TextUtils.isEmpty(cluname) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(
                cfmpwd
            )
        ) {
            Toast.makeText(
                this@clubSignup,
                "fill up the club details completely",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            if (pwd.length < 6) {
                Toast.makeText(
                    this@clubSignup,
                    "password must contain atleast 6 character",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (pwd == cfmpwd) {
                    sAuth!!.createUserWithEmailAndPassword(mail, pwd)
                        .addOnCompleteListener(this@clubSignup) { task ->
                            if (task.isSuccessful) {
                                val user = FirebaseAuth.getInstance().currentUser
                                val uid = user!!.uid
                                database = FirebaseDatabase.getInstance()
                                mDatabase = database!!.reference
                                //mDatabase.child(uid);
                                val emid = mDatabase!!.child("club").child(uid)
                                val flag = mDatabase!!.child("deptCheck").child(uid)
                                val logo = emid.child("logo_url")
                                flag.setValue("1")
                                emid.child("Club Email").setValue(mail)
                                emid.child("Club Name").setValue(cluname)
                                mStorageRef = FirebaseStorage.getInstance().getReference(uid)
                                mStorageRe = mStorageRef!!.child("ClubLogo")
                                val fileReference =
                                    mStorageRe!!.child("logo" + "." + getFileExtension(mImageUri))

                                /*mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                // Got the download URL for 'users/me/profile.png'
                                                                Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
                                                                generatedFilePath = downloadUri.toString(); /// The string(file link) that you need
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                // Handle any errors
                                                            }
                                                        });*/mUploadTask = fileReference.putFile(
                                    mImageUri!!
                                )
                                    .addOnSuccessListener { //Toast.makeText(clubSignup.this, "Upload successful", Toast.LENGTH_SHORT).show();
                                        /////////////
                                        fileReference.downloadUrl.addOnSuccessListener { uri ->
                                            logo.setValue(
                                                uri.toString()
                                            )
                                        }

                                        ////////////////


                                        //logo.setValue(taskSnapshot.getStorage().getDownloadUrl().toString());
                                        Toast.makeText(
                                            this@clubSignup,
                                            "CLUB SUCCESSFULLY REGISTERED!!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                      //  FirebaseAuth.getInstance().signOut()
                                        startActivity(
                                            Intent(
                                                this@clubSignup,
                                                MainActivity::class.java
                                            )
                                        )
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(
                                            this@clubSignup,
                                            e.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(
                                    this@clubSignup,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        this@clubSignup,
                        "password confirmation failed",
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
            Picasso.get().load(mImageUri).into(uploadImageView)

           // Picasso.with(this).load(mImageUri).into(uploadImageView)
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