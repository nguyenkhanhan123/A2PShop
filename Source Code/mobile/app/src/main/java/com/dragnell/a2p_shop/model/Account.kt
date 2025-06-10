package com.dragnell.a2p_shop.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

data class Account(
    @SerializedName("_id") val id: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("token") val token: String,
    @SerializedName("phone") val phone: String?,
    @SerializedName("role_id") val roleId: String,
    @SerializedName("status") val status: String,
    @SerializedName("deleted") val deleted: Boolean,
    @SerializedName("deletedAt") val deletedAt: Date?,
    @SerializedName("createdAt") val createdAt: Date,
    @SerializedName("updatedAt") val updatedAt: Date,
    @SerializedName("__v") val version: Int
) : Serializable
