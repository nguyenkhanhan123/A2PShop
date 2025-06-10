package com.dragnell.a2p_shop.model.res_api

import com.dragnell.a2p_shop.model.User
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserInfoRes(
    @SerializedName("code") val code: Int,
    @SerializedName("user") val user: User?
): Serializable
