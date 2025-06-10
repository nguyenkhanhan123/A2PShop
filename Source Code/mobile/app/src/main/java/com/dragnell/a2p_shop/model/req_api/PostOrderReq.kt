package com.dragnell.a2p_shop.model.req_api

import com.dragnell.a2p_shop.model.ProductOrder
import com.dragnell.a2p_shop.model.UserInfo
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PostOrderReq(
    @SerializedName("status") val status: String,
    @SerializedName("userInfo") val userInfo: UserInfo,
    @SerializedName("products") val products: List<ProductOrder>
) : Serializable
