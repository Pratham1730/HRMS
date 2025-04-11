package com.example.hrms.leaveModule.displayLeaveModule.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrms.databinding.ItemLeaveBinding
import com.example.hrms.leaveModule.displayLeaveModule.data.model.LeaveDataItem
import com.example.hrms.leaveModule.displayLeaveModule.domain.model.response.LeaveDataDomainItem

class LeaveStatusRvAdapter(
    private var context: Context,
    private var list: List<LeaveDataDomainItem?>,
    private val onDeleteClick: (LeaveDataDomainItem) -> Unit
) : RecyclerView.Adapter<LeaveStatusRvAdapter.ViewHolder>() {

    fun updateList(updatedList: List<LeaveDataDomainItem?>) {
        this.list = updatedList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLeaveBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: LeaveDataDomainItem? = list[position]

        holder.b.txtLeaveDate.text = item?.startDate
        holder.b.txtLeaveType.text = item?.leaveType
        holder.b.txtLeaveStatus.text = item?.leaveStatus
        holder.b.txtLeaveReason.text = item?.reason

        if (holder.b.txtLeaveStatus.text.toString() == "Approved" || holder.b.txtLeaveStatus.text.toString() == "Rejected"){
            holder.b.imgDelete.visibility = View.GONE
        }
        else{
            holder.b.imgDelete.visibility = View.VISIBLE
        }

        holder.b.imgDelete.setOnClickListener {
            item?.let { onDeleteClick(it) }
        }
    }

    class ViewHolder(var b: ItemLeaveBinding) : RecyclerView.ViewHolder(b.root)
}
