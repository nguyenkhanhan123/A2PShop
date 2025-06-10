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
import com.dragnell.a2p_shop.model.Category

class CategoryAdapter(private var list: List<Category>, private var context: Context, private val onClickFolder: (String,String,String) -> Unit, private val onLongClickFolder: (Category) -> Unit) :
    RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {
        
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false)
        return CategoryHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        val category: Category = list[position]
        Glide.with(context).load(category.thumbnail).into(holder.ivProduct)
        holder.nameCategory.text=category.title
        holder.itemProduct.setOnClickListener {
            onClickFolder(category.id,category.title,category.slug)
        }
        holder.itemProduct.setOnLongClickListener {
            onLongClickFolder(category)
            true
        }
    }

    fun updateData(newList: List<Category>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameCategory: TextView
        var ivProduct: ImageView
        var itemProduct: LinearLayout

        init {
            nameCategory = itemView.findViewById(R.id.nameCategory)
            ivProduct = itemView.findViewById(R.id.ivProduct)
            itemProduct=itemView.findViewById(R.id.itemProduct)
        }
    }
}
