package com.example.hrms.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrms.adapter.LeaveStatusRvAdapter.ViewHolder
import com.example.hrms.databinding.ItemEmployeeBinding
import com.example.hrms.databinding.ItemLeaveBinding

class DeptEmployeeAdapter(private var context: Context  , private var list : List<String>) : RecyclerView.Adapter<DeptEmployeeAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEmployeeBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    class ViewHolder(var b : ItemEmployeeBinding) : RecyclerView.ViewHolder(b.root)
}