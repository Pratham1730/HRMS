package com.example.hrms.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrms.databinding.ItemEmployeeBinding
import com.example.hrms.responses.EmployeesItem

class DeptEmployeeAdapter(private var context: Context, private var list: List<EmployeesItem>) :
    RecyclerView.Adapter<DeptEmployeeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEmployeeBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val employee = list[position]
        holder.b.txtName.text = employee.user_name
        holder.b.txtRole.text = employee.user_position
    }

    class ViewHolder(var b: ItemEmployeeBinding) : RecyclerView.ViewHolder(b.root)
}
