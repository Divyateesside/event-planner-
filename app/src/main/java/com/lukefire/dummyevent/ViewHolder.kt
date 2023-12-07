package com.lukefire.dummyevent

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ViewHolder(var mView: View) : RecyclerView.ViewHolder(mView) {
    init {

        /*itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(itemView.getContext(), , Toast.LENGTH_SHORT).show();
            }
        });*/
        /////
        itemView.setOnClickListener { v ->
            val mRegistration = v.findViewById<TextView>(R.id.rRegistration)
            val nam = mRegistration.text.toString()
            //String nam2= nam.replace("Register Here:","");

            /*
                    Intent i = new Intent(itemView.getContext(), test.class);
                    i.putExtra("key", (Serializable) v);
                    itemView.getContext().startActivity(i);
                    */

            //String pos= String.valueOf(v.getVerticalScrollbarPosition());
            //Toast.makeText(itemView.getContext(), nam, Toast.LENGTH_SHORT).show();
            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(nam))
                itemView.context.startActivity(browserIntent)
            } catch (e: Exception) {
                Toast.makeText(
                    itemView.context,
                    "Registration Link is not ready yet...comming soon",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun setDetails(
        ctx: Context?,
        title: String?,
        description: String?,
        image: String?,
        Logo: String?,
        Registration_Link: String?
    ) {
        val mTitleTv = mView.findViewById<TextView>(R.id.rTitleTv)
        val mDetailTv = mView.findViewById<TextView>(R.id.rDescriptionTv)
        val mRegistration = mView.findViewById<TextView>(R.id.rRegistration)
        val mImageIv = mView.findViewById<ImageView>(R.id.rImageView)
        val mLogoTv = mView.findViewById<ImageView>(R.id.rLogoView)
        mTitleTv.text = title
        mDetailTv.text = "About Event:- $description"
        mRegistration.text = Registration_Link
        Picasso.get().load(image).into(mImageIv)
        Picasso.get().load(Logo).into(mLogoTv)
//        Picasso.with(ctx).load(image).into(mImageIv)
//        Picasso.with(ctx).load(Logo).into(mLogoTv)
    }
}