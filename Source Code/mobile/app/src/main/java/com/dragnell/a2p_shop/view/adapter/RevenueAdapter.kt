package com.dragnell.a2p_shop.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dragnell.a2p_shop.R
import com.dragnell.a2p_shop.model.RevenueByProduct

class RevenueAdapter(
    private var list: List<RevenueByProduct>,
    private var context: Context
) : RecyclerView.Adapter<RevenueAdapter.RevenueHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RevenueHolder {
        val v: View =
            LayoutInflater.from(context).inflate(R.layout.item_revenue, parent, false)
        return RevenueHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RevenueHolder, position: Int) {
        val products: RevenueByProduct = list[position]
        Glide.with(context).load(products.thumbnail).into(holder.ivProduct)
        holder.nameProduct.text = products.title
        holder.totalCost.text =
            "Doanh thu: ${products.totalRevenue}$"
        holder.txtQuantity.text = "Đã bán: ${products.totalSold}"
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class RevenueHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameProduct: TextView
        var totalCost: TextView
        var txtQuantity: TextView
        var ivProduct: ImageView

        init {
            ivProduct = itemView.findViewById(R.id.imgProductCart)

            nameProduct = itemView.findViewById(R.id.txtProductName)

            totalCost = itemView.findViewById(R.id.txtProductPrice)

            txtQuantity = itemView.findViewById(R.id.txtQuantity)

        }
    }
}
