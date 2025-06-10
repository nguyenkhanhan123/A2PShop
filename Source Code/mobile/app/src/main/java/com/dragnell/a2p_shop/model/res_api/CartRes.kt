package com.dragnell.a2p_shop.model.res_api

import com.dragnell.a2p_shop.model.Cart
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CartRes(
    @SerializedName("code")
    val code: Int,
    @SerializedName("cart")
    val cart: Cart,
): Serializable