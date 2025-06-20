package com.dragnell.a2p_shop.model.res_api

import com.dragnell.a2p_shop.model.Account
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AccountInfoRes(
    @SerializedName("code") val code: Int,
    @SerializedName("massage") val massage: String,
    @SerializedName("account") val account: Account?
): Serializable
