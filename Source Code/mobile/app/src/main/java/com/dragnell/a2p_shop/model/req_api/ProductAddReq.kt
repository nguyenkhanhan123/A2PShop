package com.dragnell.a2p_shop.model.req_api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProductAddReq(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Int,
    @SerializedName("discountPercentage") val discountPercentage: Int,
    @SerializedName("stock") val stock: Int,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("status") val status: String,
    @SerializedName("product_category_id") val productCategoryId: String?,
): Serializable