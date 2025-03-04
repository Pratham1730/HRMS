package com.example.hrms.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

// Custom adapter to ensure spinner text remains black
class CustomSpinnerAdapter(context: Context, items: Array<String>) :
    ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent) as TextView
        view.setTextColor(Color.BLACK) // Set selected item text color to black
        return view
    }

//    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
//        val view = super.getDropDownView(position, convertView, parent) as TextView
//        view.setTextColor(Color.BLACK) // Set dropdown item text color to black
//        return view
//    }


}