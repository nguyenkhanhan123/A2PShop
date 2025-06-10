package com.dragnell.a2p_shop.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dragnell.a2p_shop.R
import com.dragnell.a2p_shop.model.Role

class RoleAdapter(
    private var list: List<Role>,
    private var context: Context,
    private val onClick: (Role) -> Unit
) : RecyclerView.Adapter<RoleAdapter.RoleHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoleHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.item_role, parent, false)
        return RoleHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RoleHolder, position: Int) {
        val role: Role = list[position]
        holder.nameRole.text = role.title
        holder.describe.text = role.description
        holder.settings.setOnClickListener {
            onClick(role)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class RoleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameRole: TextView
        var describe: TextView
        var settings: ImageView

        init {
            nameRole = itemView.findViewById(R.id.nameRole)
            describe = itemView.findViewById(R.id.describe)
            settings = itemView.findViewById(R.id.settings)
        }
    }
}
