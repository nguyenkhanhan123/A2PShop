package com.dragnell.a2p_shop.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

data class ProductOrder(
    @SerializedName("product_id") val id: String?,
    @SerializedName("price") val price: Int?,
    @SerializedName("discountPercentage") val discountPercentage: Int?,
    @SerializedName("quantity") val quantity: Int?
) : Serializable
