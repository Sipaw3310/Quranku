package com.sipaw3310.quranku

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JuzRecyclerviewAdapter(private val juzList: List<JuzListClass>) :
    RecyclerView.Adapter<JuzRecyclerviewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): JuzRecyclerviewAdapter.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.juz_list_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: JuzRecyclerviewAdapter.ViewHolder, pos: Int) {
        holder.textIndex.text = pos.toString()
        holder.textAya.text = juzList[pos].sura
        holder.textSura.text = juzList[pos].aya
    }

    override fun getItemCount(): Int = juzList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textIndex: TextView = itemView.findViewById(R.id.index_juz_recyclerview_list)
        val textAya: TextView = itemView.findViewById(R.id.aya_juz_recyclerview_list)
        val textSura: TextView = itemView.findViewById(R.id.sura_juz_recyclerview_list)
    }

}

class JuzListClass(val sura: String, val aya: String)