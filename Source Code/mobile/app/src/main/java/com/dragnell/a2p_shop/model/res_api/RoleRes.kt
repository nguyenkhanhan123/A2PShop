package com.dragnell.a2p_shop.model.res_api

import com.dragnell.a2p_shop.model.Role
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RoleRes(
    @SerializedName("code") val code: Int,
    @SerializedName("records") val records: List<Role>,
    @SerializedName("massgae") val massgae: String,
): Serializable
