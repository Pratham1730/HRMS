package com.example.hrms.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hrms.R
import java.util.Calendar

class MonthRVAdapter(private val context: Context,private var list: List<String>) : RecyclerView.Adapter<MonthRVAdapter.ViewHolder>() {


    private var selectedPosition = Calendar.getInstance().get(Calendar.MONTH)
    private var onMonthClickListener : OnMonthClickListener ?= null

    interface OnMonthClickListener{
        fun onClicked(position: Int)
    }

    fun setUpInterface(onMonthClickListener: OnMonthClickListener){
        this.onMonthClickListener = onMonthClickListener
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnMonth: TextView = itemView.findViewById(R.id.btnMonth)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_month, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.btnMonth.text = list[position]


        holder.btnMonth.setOnClickListener {
            onMonthClickListener?.onClicked(position + 1)
            selectedPosition = position
            notifyDataSetChanged()
        }

        if (position == selectedPosition) {
            holder.btnMonth.setBackgroundResource(R.drawable.border_primary)
        }
        else {
            holder.btnMonth.setBackgroundResource(R.drawable.border)
        }

    }
}
