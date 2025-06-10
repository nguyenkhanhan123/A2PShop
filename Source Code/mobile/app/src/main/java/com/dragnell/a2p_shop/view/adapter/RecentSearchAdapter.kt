package com.dragnell.a2p_shop.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dragnell.a2p_shop.R

class RecentSearchAdapter(private var list: List<String>, private var context: Context, private val onClick: (String) -> Unit ) :
    RecyclerView.Adapter<RecentSearchAdapter.RecentSearchHolder>() {
        
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.item_recent_search, parent, false)
        return RecentSearchHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecentSearchHolder, position: Int) {
        val text: String = list[position]
        holder.recentSearch.text=text
        holder.lnMain.setOnClickListener { 
            onClick(text)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class RecentSearchHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var recentSearch: TextView
        var lnMain:LinearLayout
        init {
            recentSearch = itemView.findViewById(R.id.recentSearch)
            lnMain=itemView.findViewById(R.id.lnMain)
        }
    }
}
