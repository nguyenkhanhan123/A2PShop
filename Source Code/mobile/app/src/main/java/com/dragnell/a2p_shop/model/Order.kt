package com.dragnell.a2p_shop.model

import com.dragnell.a2p_shop.model.ProductOrder
import com.dragnell.a2p_shop.model.UserInfo
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Order(
    @SerializedName("_id") val _id: String?,
    @SerializedName("user_id") val user_id: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("userInfo") val userInfo: UserInfo?,
    @SerializedName("products") val products: List<ProductOrder>?,
    @SerializedName("createdAt") val createdAt: String?
) : Serializable
