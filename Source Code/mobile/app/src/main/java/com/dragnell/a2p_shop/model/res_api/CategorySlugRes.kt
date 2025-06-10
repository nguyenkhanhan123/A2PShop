package com.dragnell.a2p_shop.model.res_api

import com.dragnell.a2p_shop.model.Product
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CategorySlugRes(
    @SerializedName("products")
    val products: List<Product>,
    @SerializedName("currentPage")
    val currentPage: Int
): Serializable



