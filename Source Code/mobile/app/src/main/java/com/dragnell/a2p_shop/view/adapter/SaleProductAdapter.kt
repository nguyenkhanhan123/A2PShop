package com.dragnell.a2p_shop.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dragnell.a2p_shop.R
import com.dragnell.a2p_shop.model.Product

class SaleProductAdapter(private var list: List<Product>, private var context: Context, private val onClickFolder: (String) -> Unit) :
    RecyclerView.Adapter<SaleProductAdapter.ProductHolder>() {
        
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.item_sale, parent, false)
        return ProductHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        val product: Product = list[position]
        Glide.with(context).load(product.thumbnail).into(holder.imgProductCart)
        holder.txtProductName.text=product.title
        holder.txtOriginalPrice.text="$ ${product.price}"
        val discountedPrice = (product.price * (1 - product.discountPercentage.toDouble() / 100)).toInt()
        holder.txtProductPrice.text = "$ $discountedPrice"
        holder.cardProduct.setOnClickListener {
            onClickFolder(product.slug.toString())
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ProductHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtProductName: TextView
        var txtProductPrice: TextView
        var txtOriginalPrice: TextView
        var imgProductCart: ImageView
        var cardProduct: CardView

        init {
            txtProductName = itemView.findViewById(R.id.txtProductName)
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice)
            txtOriginalPrice = itemView.findViewById(R.id.txtOriginalPrice)
            imgProductCart = itemView.findViewById(R.id.imgProductCart)
            cardProduct=itemView.findViewById(R.id.cardProduct)
        }
    }
}
