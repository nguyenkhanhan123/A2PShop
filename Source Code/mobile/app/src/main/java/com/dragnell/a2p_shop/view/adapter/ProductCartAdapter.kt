package com.dragnell.a2p_shop.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dragnell.a2p_shop.R
import com.dragnell.a2p_shop.model.ProductOrder
import com.dragnell.a2p_shop.model.Products

class ProductCartAdapter(
    private var list: List<Products>,
    private var context: Context,
    private val onClickDelete: (String) -> Unit,
    private val onClickUpdate: (String, Int) -> Unit,
    private val onTrueCheckBox: (ProductOrder) -> Unit,
    private val onFalseCheckBox: (ProductOrder) -> Unit
) : RecyclerView.Adapter<ProductCartAdapter.ProductsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsHolder {
        val v: View =
            LayoutInflater.from(context).inflate(R.layout.item_product_cart, parent, false)
        return ProductsHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductsHolder, position: Int) {
        val products: Products = list[position]
        Glide.with(context).load(products.productInfo.thumbnail).into(holder.ivProduct)
        holder.nameProduct.text = products.productInfo.title
        holder.cost.text = "$ ${products.productInfo.price}"
        val discountedPrice = (products.productInfo.price * (1 - products.productInfo.discountPercentage.toDouble() / 100)).toInt()
        holder.totalCost.text = "$ $discountedPrice"
        holder.txtQuantity.text = products.quantity.toString()
        holder.btnUpdate.setOnClickListener {
            onClickUpdate(
                products.productInfo.id,
                holder.txtQuantity.text.toString().toIntOrNull() ?: 1
            )
        }
        holder.btnDelete.setOnClickListener {
            onClickDelete(products.productInfo.id)
        }
        holder.btnDecrease.setOnClickListener {
            val i = holder.txtQuantity.text.toString().toIntOrNull() ?: 1
            holder.txtQuantity.text = (i - 1).coerceAtLeast(1).toString()
        }
        holder.btnIncrease.setOnClickListener {
            val i = holder.txtQuantity.text.toString().toIntOrNull() ?: 1
            holder.txtQuantity.text = (i + 1).coerceAtMost(10).toString()
        }
        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                onTrueCheckBox(ProductOrder(products.productInfo.id,products.productInfo.price,products.productInfo.discountPercentage,products.quantity))
            } else {
                onFalseCheckBox(ProductOrder(products.productInfo.id,products.productInfo.price,products.productInfo.discountPercentage,products.quantity))
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ProductsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameProduct: TextView
        var totalCost: TextView
        var cost: TextView
        var txtQuantity: TextView
        var ivProduct: ImageView
        var btnUpdate: ImageView
        var btnDelete: ImageView
        var btnDecrease: ImageView
        var btnIncrease: ImageView
        var checkbox: CheckBox

        init {
            nameProduct = itemView.findViewById(R.id.txtProductName)
            totalCost = itemView.findViewById(R.id.txtProductPrice)
            cost = itemView.findViewById(R.id.txtOriginalPrice)
            ivProduct = itemView.findViewById(R.id.imgProductCart)
            txtQuantity = itemView.findViewById(R.id.txtQuantity)
            btnUpdate = itemView.findViewById(R.id.btnUpdate)
            btnDelete = itemView.findViewById(R.id.btnDelete)
            btnDecrease = itemView.findViewById(R.id.btnDecrease)
            btnIncrease = itemView.findViewById(R.id.btnIncrease)
            checkbox = itemView.findViewById(R.id.checkbox)
        }
    }
}
