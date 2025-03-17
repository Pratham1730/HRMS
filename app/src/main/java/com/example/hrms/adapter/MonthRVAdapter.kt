package com.example.hrms.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.hrms.R

class MonthRVAdapter(private val context: Context,private var list: List<String>) : RecyclerView.Adapter<MonthRVAdapter.ViewHolder>() {


    private var onMonthClickListener : OnMonthClickListener ?= null

    interface OnMonthClickListener{
        fun onClicked(position: Int)
    }

    fun setUpInterface(onMonthClickListener: OnMonthClickListener){
        this.onMonthClickListener = onMonthClickListener
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnMonth: Button = itemView.findViewById(R.id.btnMonth)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_month, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.btnMonth.text = list[position]


        holder.btnMonth.setOnClickListener {
            onMonthClickListener?.onClicked(position + 1)
        }
    }
}
