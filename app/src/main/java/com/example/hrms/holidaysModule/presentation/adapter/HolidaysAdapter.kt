package com.example.hrms.holidaysModule.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrms.databinding.ItemPublicHolidaysBinding
import com.example.hrms.holidaysModule.data.model.HolidaysItem
import com.example.hrms.holidaysModule.domain.model.response.HolidaysDomainItem


class HolidaysAdapter(private var context: Context, private var list: List<HolidaysDomainItem?>) : RecyclerView.Adapter<HolidaysAdapter.ViewHolder>() {


    fun updateList(updatedList: List<HolidaysDomainItem?>) {
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
        val item: HolidaysDomainItem? = list[position]

        holder.b.txtHolidayName.text = (item?.holidayName).toString()
        holder.b.txtDateHoliday.text = (item?.holidayDate).toString()
    }

    class ViewHolder(var b: ItemPublicHolidaysBinding) : RecyclerView.ViewHolder(b.root)
}