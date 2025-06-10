package com.dragnell.a2p_shop.model.res_api

import com.dragnell.a2p_shop.model.Product
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SearchRes(
    @SerializedName("code")
    val code: Int,
    @SerializedName("products")
    val products: List<Product>,
    @SerializedName("keyword")
    val keyword: String
): Serializable



