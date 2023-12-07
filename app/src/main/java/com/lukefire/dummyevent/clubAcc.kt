package com.lukefire.dummyevent

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class clubAcc : AppCompatActivity() {
     lateinit var mRecyclerView: RecyclerView
    var mFirebaseDatabase: FirebaseDatabase? = null
    var mRef: DatabaseReference? = null
    private var logout: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_club_acc)
        val actionBar = supportActionBar
        actionBar!!.setTitle("YOUR EVENTS LISTS")
        mRecyclerView = findViewById(R.id.recyclerview)
        mRecyclerView.setHasFixedSize(true)
        logout = findViewById<View>(R.id.logout) as ImageView
        mRecyclerView.setLayoutManager(LinearLayoutManager(this))
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user!!.uid
        mRef = mFirebaseDatabase!!.getReference("club").child(uid).child("events")
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //.setAction("Action", null).show();
            startActivity(Intent(this@clubAcc, Event::class.java))
        }
        logout!!.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@clubAcc, MainActivity::class.java))
        }
    }
    override fun onStart() {
        super.onStart()

        val options: FirebaseRecyclerOptions<clubModel> = FirebaseRecyclerOptions.Builder<clubModel>()
            .setQuery(mRef!!, clubModel::class.java)
            .build()

        val firebaseRecyclerAdapter: FirebaseRecyclerAdapter<clubModel, clubViewHolder> =
            object : FirebaseRecyclerAdapter<clubModel, clubViewHolder>(options) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): clubViewHolder {
                    val view: View =
                        LayoutInflater.from(parent.context).inflate(R.layout.clubrow, parent, false)
                    return clubViewHolder(view)
                }

                override fun onBindViewHolder(
                    viewHolder: clubViewHolder,
                    position: Int,
                    model: clubModel
                ) {
                    viewHolder.setDetails(
                        applicationContext,
                        model.name,
                        model.event_url,
                        model.eventUID
                    )
                }
            }

        mRecyclerView!!.adapter = firebaseRecyclerAdapter
        firebaseRecyclerAdapter.startListening()
    }

//    override fun onStart() {
//        super.onStart()
//        val firebaseRecyclerAdapter: FirebaseRecyclerAdapter<clubModel, clubViewHolder> =
//            object : FirebaseRecyclerAdapter<clubModel?, clubViewHolder?>(
//                clubModel::class.java,
//                R.layout.clubrow,
//                clubViewHolder::class.java,
//                mRef
//            ) {
//                protected fun populateViewHolder(
//                    viewHolder: clubViewHolder,
//                    model: clubModel,
//                    i: Int
//                ) {
//                    viewHolder.setDetails(
//                        applicationContext,
//                        model.name,
//                        model.event_url,
//                        model.eventUID
//                    )
//                }
//            }
//        mRecyclerView!!.adapter = firebaseRecyclerAdapter
//    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}