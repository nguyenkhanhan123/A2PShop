package com.dragnell.a2p_shop.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dragnell.a2p_shop.R
import com.dragnell.a2p_shop.model.Product

class ProductAdapter(private var list: List<Product>, private var context: Context, private val onClickFolder: (String) -> Unit) :
    RecyclerView.Adapter<ProductAdapter.ProductHolder>() {
        
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)
        return ProductHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        val product: Product = list[position]
        Glide.with(context).load(product.thumbnail).into(holder.ivProduct)
        holder.nameProduct.text=product.title
        holder.cost.text="$ ${product.price}"
        val discountedPrice = (product.price * (1 - product.discountPercentage.toDouble() / 100)).toInt()
        holder.totalCost.text = "$ $discountedPrice"
        holder.itemProduct.setOnClickListener {
            onClickFolder(product.slug.toString())
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ProductHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameProduct: TextView
        var totalCost: TextView
        var cost: TextView
        var ivProduct: ImageView
        var itemProduct: LinearLayout

        init {
            nameProduct = itemView.findViewById(R.id.nameProduct)
            totalCost = itemView.findViewById(R.id.totalCost)
            cost = itemView.findViewById(R.id.cost)
            ivProduct = itemView.findViewById(R.id.ivProduct)
            itemProduct=itemView.findViewById(R.id.itemProduct)
        }
    }
}
