package com.dragnell.a2p_shop.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

data class RevenueByProduct(
    @SerializedName("productId") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("totalRevenue") val totalRevenue: Double,
    @SerializedName("totalSold") val totalSold: Int
): Serializable