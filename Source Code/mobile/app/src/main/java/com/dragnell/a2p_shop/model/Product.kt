package com.dragnell.a2p_shop.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

data class Product(
    @SerializedName("_id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Int,
    @SerializedName("discountPercentage") val discountPercentage: Int,
    @SerializedName("stock") val stock: Int,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("status") val status: String,
    @SerializedName("position") val position: Int,
    @SerializedName("deleted") val deleted: Boolean,
    @SerializedName("product_category_id") val productCategoryId: String?,
    @SerializedName("slug") val slug: String? = null,
    @SerializedName("createdAt") val createdAt: Date? = null,
    @SerializedName("updatedAt") val updatedAt: Date? = null,
    @SerializedName("__v") val version: Int,
    @SerializedName("createBy") val createBy: CreateBy?,
    @SerializedName("deleteBy") val deleteBy: DeleteBy?,
    @SerializedName("updatedBy") val updatedBy: List<UpdatedBy> = emptyList()
): Serializable