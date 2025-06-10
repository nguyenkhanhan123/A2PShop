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
import com.dragnell.a2p_shop.model.Account
import com.dragnell.a2p_shop.model.Role

class AccountsAdapter(
    private var list: List<Account>,
    private var context: Context,
    private val getNameRole: (String) -> String,
    private val onClick: (Account) -> Unit
) : RecyclerView.Adapter<AccountsAdapter.AccountHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.item_accounts, parent, false)
        return AccountHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AccountHolder, position: Int) {
        val account: Account = list[position]
        holder.fullName.text = "Họ và tên: ${account.fullName}"
        holder.Email.text = "Email: ${account.email}"
        holder.sđt.text = "SĐT: ${account.phone}"
        holder.nameRole.text = getNameRole(account.roleId)
        holder.settings.setOnClickListener {
            onClick(account)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class AccountHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameRole: TextView
        var fullName: TextView
        var Email: TextView
        var sđt: TextView
        var settings: ImageView

        init {
            nameRole = itemView.findViewById(R.id.nameRole)
            fullName = itemView.findViewById(R.id.fullName)
            Email = itemView.findViewById(R.id.Email)
            sđt = itemView.findViewById(R.id.sđt)
            settings = itemView.findViewById(R.id.settings)
        }
    }
}
