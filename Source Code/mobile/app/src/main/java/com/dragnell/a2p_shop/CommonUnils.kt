package com.dragnell.a2p_shop

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CommonUtils private constructor() {
    companion object {
        private var instance: CommonUtils? = null
        private val gson = Gson()

        fun getInstance(): CommonUtils {
            return instance ?: synchronized(this) {
                instance ?: CommonUtils().also { instance = it }
            }
        }
    }

    fun savePref(key: String, value: String) {
        val pre = App.getInstance().getSharedPreferences("An", Context.MODE_PRIVATE)
        with(pre.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getPref(key: String): String? {
        val pre = App.getInstance().getSharedPreferences("An", Context.MODE_PRIVATE)
        return pre.getString(key, "")
    }

    fun clearPref(key: String) {
        val pre = App.getInstance().getSharedPreferences("An", Context.MODE_PRIVATE)
        pre.edit().remove(key).apply()
    }

    private fun saveListPref(key: String, list: List<String>) {
        val json = gson.toJson(list)
        savePref(key, json)
    }

    fun saveUniqueStringToList(key: String, value: String) {
        val currentList = getListPref(key).toMutableList()
        currentList.remove(value)
        currentList.add(0, value)
        if (currentList.size > 7) {
            currentList.removeAt(currentList.size - 1)
        }
        saveListPref(key, currentList)
    }

     fun getListPref(key: String): List<String> {
        val json = getPref(key)
        if (json.isNullOrEmpty()) return emptyList()

        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type)
    }

    fun clearListPref(key: String) {
        clearPref(key)
    }
}
