package com.dragnell.a2p_shop.model.res_api

import com.dragnell.a2p_shop.model.RevenueByProduct
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RevenueRes(
    @SerializedName("revenueByProduct") val order: List<RevenueByProduct>
): Serializable
