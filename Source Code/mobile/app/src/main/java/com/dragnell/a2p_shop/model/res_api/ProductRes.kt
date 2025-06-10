package com.dragnell.a2p_shop.model.res_api

import com.dragnell.a2p_shop.model.Category
import com.dragnell.a2p_shop.model.Product
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProductRes(
    @SerializedName("newProducts")
    val products: List<Product>,
    @SerializedName("layoutProductCategory")
    val layoutProductCategory: List<Category>
): Serializable



