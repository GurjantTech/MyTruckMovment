package com.truckmovement.mvvm.ui.awb.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.truckmovement.mvvm.R

class AttachmentAdapter(var context: Context, var list: ArrayList<Uri>) :
    RecyclerView.Adapter<AttachmentAdapter.AttachmentHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentHolder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.attachment_adapter_layout, parent, false)
        return AttachmentHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AttachmentHolder, position: Int) {
        holder.attach_image.setImageURI(list[position])

        holder.attach_image.setOnClickListener {
            list.removeAt(position)
            notifyDataSetChanged()
        }
    }

    class AttachmentHolder(view: View) : RecyclerView.ViewHolder(view) {
        var attach_image = view.findViewById<ImageView>(R.id.attach_image)
        var crossBtn=view.findViewById<ImageView>(R.id.crossBtn)
    }
}