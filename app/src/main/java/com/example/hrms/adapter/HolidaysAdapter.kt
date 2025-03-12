package com.example.hrms.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hrms.R


class HolidaysAdapter(private var context: Context, private var list: List<String>) : RecyclerView.Adapter<HolidaysAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        val txtDate: TextView = itemView.findViewById(R.id.txtDateHoliday)
        val txtFest: TextView = itemView.findViewById(R.id.txtFestHolidays)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_public_holidays, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
     return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtDate.text = list[position]
        holder.txtFest.text = list[position]
    }
}