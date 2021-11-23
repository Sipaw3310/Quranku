package com.sipaw3310.quranku

import android.util.Log
import android.util.Xml
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser

class ReadQuranRecyclerviewAdapter(private val suraData: List<AyaClass>): RecyclerView.Adapter<ReadQuranRecyclerviewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.readquran_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textArabic.text = suraData[position].textArabic
    }

    override fun getItemCount(): Int = suraData.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textArabic: TextView = itemView.findViewById(R.id.readQuran_quranArabic)
    }

}

class AyaClass(val textArabic: String)