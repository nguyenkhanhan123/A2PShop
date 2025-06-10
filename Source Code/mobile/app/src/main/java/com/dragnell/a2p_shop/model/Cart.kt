package com.dragnell.a2p_shop.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

data class Cart(
    @SerializedName("_id") val id: String,
    @SerializedName("products") val products: List<Products>,
    @SerializedName("createdAt") val createdAt: Date,
    @SerializedName("updatedAt") val updatedAt: Date,
    @SerializedName("__v") val version: Int,
    @SerializedName("user_id") val userId: String,
    @SerializedName("totalPrice") val totalPrice: Int
): Serializable

data class Products(
    @SerializedName("product_id") val productId: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("productInfo") val productInfo: Product,
    @SerializedName("_id") val id: String,
    @SerializedName("totalPrice") val totalPrice: Int
): Serializable