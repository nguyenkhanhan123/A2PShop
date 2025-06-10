package com.dragnell.a2p_shop.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("_id") val id: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("email") val email: String,
    @SerializedName("tokenUser") val tokenUser: String,
    @SerializedName("status") val status: String,
    @SerializedName("deleted") val deleted: Boolean,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("__v") val version: Int,
    @SerializedName("phone") val phone: Int
): Serializable