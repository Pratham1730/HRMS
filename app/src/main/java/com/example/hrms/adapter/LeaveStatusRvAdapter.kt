package com.example.hrms.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrms.databinding.ItemLeaveBinding
import com.example.hrms.responses.LeaveDataItem

class LeaveStatusRvAdapter(private var context : Context, private var list : List<LeaveDataItem?>)  : RecyclerView.Adapter<LeaveStatusRvAdapter.ViewHolder>() {

    fun updateList(updatedList : List<LeaveDataItem?>){
        this.list = updatedList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLeaveBinding.inflate(LayoutInflater.from(context) , parent , false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item : LeaveDataItem? = list[position]
        holder.b.txtLeaveDate.text = (item?.l_start_date)
        holder.b.txtLeaveType.text = (item?.leave_type)
        holder.b.txtLeaveStatus.text = (item?.leave_status)
        holder.b.txtLeaveReason.text = (item?.l_reason)
    }

    class ViewHolder(var b : ItemLeaveBinding) : RecyclerView.ViewHolder(b.root)

}