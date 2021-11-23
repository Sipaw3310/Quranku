package com.sipaw3310.quranku

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class SuraRecyclerviewAdapter(
    private val suraList: List<SuraListClass>,
    private val readQuran: (index: Int) -> Unit) :
    RecyclerView.Adapter<SuraRecyclerviewAdapter.ViewHolder>() {

    lateinit var context: Context;

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SuraRecyclerviewAdapter.ViewHolder {
        context = parent.context
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.sura_list_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SuraRecyclerviewAdapter.ViewHolder, pos: Int) {
        holder.textIndex.text = suraList[pos].index.toString()
        holder.textSura.text = suraList[pos].sura
        holder.textArabic.text = suraList[pos].arabic

        holder.parentView.setOnClickListener {
            /*val intent = Intent(context, ReadQuranActivity::class.java)/*.apply {
                val intentSuraList = arrayListOf<String>()
                suraList.forEach {
                    intentSuraList.add(it.sura)
                }

                putStringArrayListExtra("suraList", intentSuraList)
            }*/
            context.startActivity(intent)*/
            readQuran(suraList[pos].index)
        }
    }

    override fun getItemCount(): Int = suraList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textIndex: TextView = itemView.findViewById(R.id.index_sura_recyclerview_list)
        val textSura: TextView = itemView.findViewById(R.id.name_sura_recyclerview_list)
        val textArabic: TextView = itemView.findViewById(R.id.arabic_sura_recyclerview_list)
        val parentView: ConstraintLayout = itemView.findViewById(R.id.parent_sura_recyclerview_list)
    }

}

class SuraListClass(val index: Int, val sura: String, val arabic: String)