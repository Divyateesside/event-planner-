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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class StudAccount : AppCompatActivity() {
    lateinit var mRecyclerView: RecyclerView
    var mFirebaseDatabase: FirebaseDatabase? = null
    var mRef: DatabaseReference? = null
    private var logout: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stud_account)
        val actionBar = supportActionBar
        actionBar!!.setTitle("Posts Lists")
        logout = findViewById<View>(R.id.logout) as ImageView
        mRecyclerView = findViewById(R.id.recyclerview)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.setLayoutManager(LinearLayoutManager(this))
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mRef = mFirebaseDatabase!!.getReference("club").child("eventdisplay")
        logout!!.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@StudAccount, MainActivity::class.java))
        }
    }
    override fun onStart() {
        super.onStart()

        val options: FirebaseRecyclerOptions<Model> = FirebaseRecyclerOptions.Builder<Model>()
            .setQuery(mRef!!, Model::class.java)
            .build()

        val firebaseRecyclerAdapter: FirebaseRecyclerAdapter<Model, ViewHolder> =
            object : FirebaseRecyclerAdapter<Model, ViewHolder>(options) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                    val view: View =
                        LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
                    return ViewHolder(view)
                }

                override fun onBindViewHolder(
                    viewHolder: ViewHolder,
                    position: Int,
                    model: Model
                ) {
                    viewHolder.setDetails(
                        applicationContext,
                        model.name,
                        model.description,
                        model.event_url,
                        model.logo_url,
                        model.registration_Link
                    )
                }
            }

        mRecyclerView!!.adapter = firebaseRecyclerAdapter
        firebaseRecyclerAdapter.startListening()
    }
//    override fun onStart() {
//        super.onStart()
//        val firebaseRecyclerAdapter: FirebaseRecyclerAdapter<Model, ViewHolder> =
//            object : FirebaseRecyclerAdapter<Model?, ViewHolder?>(
//                Model::class.java,
//                R.layout.row,
//                ViewHolder::class.java,
//                mRef
//            ) {
//                protected fun populateViewHolder(viewHolder: ViewHolder, model: Model, i: Int) {
//                    viewHolder.setDetails(
//                        applicationContext,
//                        model.name,
//                        model.description,
//                        model.event_url,
//                        model.logo_url,
//                        model.registration_Link
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