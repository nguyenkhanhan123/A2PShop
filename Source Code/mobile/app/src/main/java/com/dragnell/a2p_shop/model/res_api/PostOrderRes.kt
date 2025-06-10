package com.dragnell.a2p_shop.model.res_api

import com.dragnell.a2p_shop.model.Order
import com.dragnell.a2p_shop.model.ProductOrder
import com.dragnell.a2p_shop.model.UserInfo
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PostOrderRes(
    @SerializedName("message")  val message: String
) : Serializable
