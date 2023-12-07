package com.lukefire.dummyevent

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class clubViewHolder(var mView: View) : RecyclerView.ViewHolder(
    mView
) {
    init {

        /*itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(itemView.getContext(), , Toast.LENGTH_SHORT).show();
            }
        });*/
        itemView.setOnClickListener { v ->
            val mUID = v.findViewById<TextView>(R.id.rUID)
            val UID = mUID.text.toString()
            val intent = Intent(mView.context, EditEvent::class.java)
            intent.putExtra("message", UID)
            mView.context.startActivity(intent)
        }

        /*itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView mRegistration= v.findViewById(R.id.rRegistration);
                String nam= mRegistration.getText().toString();
                //String nam2= nam.replace("Register Here:","");



                //String pos= String.valueOf(v.getVerticalScrollbarPosition());
                //Toast.makeText(itemView.getContext(), nam, Toast.LENGTH_SHORT).show();
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(nam));
                    itemView.getContext().startActivity(browserIntent);
                } catch (Exception e) {
                    Toast.makeText(itemView.getContext(), "Registration Link is not ready yet...comming soon", Toast.LENGTH_SHORT).show();
                }


            }
        });*/
    }

    fun setDetails(ctx: Context?, title: String?, image: String?, UID: String?) {
        val mTitleTv = mView.findViewById<TextView>(R.id.rTitleTv)
        val mUID = mView.findViewById<TextView>(R.id.rUID)
        val mImageIv = mView.findViewById<ImageView>(R.id.rImageView)
        mTitleTv.text = title
        mUID.text = UID
        Picasso.get().load(image).into(mImageIv)
        //Picasso.with(ctx).load(image).into(mImageIv)
    }
}