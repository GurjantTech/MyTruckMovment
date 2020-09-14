package com.truckmovement.mvvm.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.truckmovement.mvvm.R
import com.truckmovement.mvvm.ui.home.HomeActivity
import java.util.ArrayList

class HomeAdapter(
    var context: HomeActivity,
    var list: ArrayList<String>,
    var iconList: ArrayList<Int>
) : RecyclerView.Adapter<HomeAdapter.Homeviewholde>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Homeviewholde {
        var view = LayoutInflater.from(context).inflate(R.layout.home_adapter_layout, parent, false)
        return Homeviewholde(view)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Homeviewholde, position: Int) {
        holder.field_name.text = list[position]
        holder.icon.setImageResource(iconList[position])
        holder.layout.setOnClickListener {
            when (position) {
                0 -> context.loadTruckDetailFragment()
                1 -> context.loadLoadingInformationFragment()
                2 -> context.loadAWBFragment()
            }
        }
    }


    class Homeviewholde(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var layout = itemview.findViewById<CardView>(R.id.layout)
        var field_name = itemview.findViewById<TextView>(R.id.field_name)
        var icon = itemview.findViewById<ImageView>(R.id.icon)

    }
}