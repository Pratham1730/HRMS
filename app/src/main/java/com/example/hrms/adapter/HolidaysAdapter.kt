package com.example.hrms.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hrms.R
import com.example.hrms.adapter.LeaveStatusRvAdapter.ViewHolder
import com.example.hrms.databinding.ItemPublicHolidaysBinding
import com.example.hrms.responses.HolidaysItem
import com.example.hrms.responses.LeaveDataItem


class HolidaysAdapter(private var context: Context, private var list: List<HolidaysItem?>) : RecyclerView.Adapter<HolidaysAdapter.ViewHolder>() {


    fun updateList(updatedList: List<HolidaysItem?>) {
        this.list = updatedList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPublicHolidaysBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)

    }

    override fun getItemCount(): Int {
     return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: HolidaysItem? = list[position]

        holder.b.txtHolidayName.text = (item?.holiday_name).toString()
        holder.b.txtDateHoliday.text = (item?.holiday_date).toString()
    }

    class ViewHolder(var b: ItemPublicHolidaysBinding) : RecyclerView.ViewHolder(b.root)
}