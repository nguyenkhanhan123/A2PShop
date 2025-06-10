package com.dragnell.a2p_shop.model.res_api

import com.dragnell.a2p_shop.model.Account
import com.dragnell.a2p_shop.model.Order
import com.dragnell.a2p_shop.model.ProductOrder
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetOrderAdminRes(
    @SerializedName("code") val code: Int,
    @SerializedName("orders") val order: List<Order>
): Serializable
