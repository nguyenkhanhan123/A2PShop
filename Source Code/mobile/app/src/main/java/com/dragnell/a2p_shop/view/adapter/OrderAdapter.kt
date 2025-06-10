package com.dragnell.a2p_shop.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dragnell.a2p_shop.R
import com.dragnell.a2p_shop.model.Order
import com.dragnell.a2p_shop.model.ProductOrder

class OrderAdapter(
    private var list: List<Order>,
    private var context: Context,
    private val onClick: (Order) -> Unit
) : RecyclerView.Adapter<OrderAdapter.OrderHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false)
        return OrderHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        val order: Order = list[position]
        holder.tvOrderId.text = "Mã đơn: ${order._id}"
        holder.tvOrderStatus.text = when (order.status) {
            "pending" -> "⏳ Chờ xử lý"
            "shipped" -> "🚚 Đang giao"
            "completed" -> "✅ Hoàn tất"
            "cancelled" -> "❌ Đã hủy"
            else -> "ℹ️ Không rõ"
        }

        val statusColor = when (order.status) {
            "pending" -> Color.parseColor("#FFA500") // cam
            "shipped" -> Color.parseColor("#1E90FF") // xanh biển
            "completed" -> Color.parseColor("#28A745") // xanh lá
            "cancelled" -> Color.parseColor("#DC3545") // đỏ
            else -> Color.GRAY
        }
        holder.tvOrderStatus.setTextColor(statusColor)

        holder.tvCustomerInfo.text =
            "👤 ${order.userInfo!!.fullName}   |   📅 ${order.createdAt!!.take(10)}"
        holder.tvOrderSummary.text =
            "🛒 ${order.products!!.size} sản phẩm   |   💵 ${tinhTongTien(order.products!!)}$"
        holder.tvAddress.text = "📍 ${order.userInfo.address}"

        holder.lnOrder.setOnClickListener {
            onClick(order)
        }
    }

    private fun tinhTongTien(products: List<ProductOrder>): Int {
        return products.sumOf { product ->
            val price = product.price!!
            val discount = product.discountPercentage!!
            val quantity = product.quantity!!

            val giaSauGiam = price * (1 - discount / 100.0)
            (giaSauGiam * quantity).toInt()
        }
    }



    override fun getItemCount(): Int {
        return list.size
    }

    class OrderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvOrderId: TextView
        var tvCustomerInfo: TextView
        var tvOrderSummary: TextView
        var tvAddress: TextView
        var tvOrderStatus: TextView
        var lnOrder: LinearLayout

        init {
            tvOrderId = itemView.findViewById(R.id.tvOrderId)
            tvCustomerInfo = itemView.findViewById(R.id.tvCustomerInfo)
            tvOrderSummary = itemView.findViewById(R.id.tvOrderSummary)
            tvAddress = itemView.findViewById(R.id.tvAddress)
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus)
            lnOrder = itemView.findViewById(R.id.lnOrder)
        }
    }
}
