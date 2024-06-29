package com.example.counter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


data class Counter(val title: String, var value: Int)

object SharedPreferencesManager {
//    private const val PREF_NAME = "my_app_prefs"
    private const val KEY_LIST_SIZE = "list_size"
    private const val KEY_TITLE_PREFIX = "title_"
    private const val KEY_COUNT_PREFIX = "count_"
    private const val PREF_NAME = "my_app_prefs"
    private const val KEY_COUNTERS = "counters"


    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

//    fun saveList(list: MutableList<Counter>) {
//        val editor = sharedPreferences.edit()
//        editor.putInt(KEY_LIST_SIZE, list.size)
//        for ((index, item) in list.withIndex()) {
//            editor.putString(KEY_TITLE_PREFIX + index, item.title)
//            editor.putInt(KEY_COUNT_PREFIX + index, item.value)
//        }
//        editor.apply()
//    }
//
//    fun getList(): MutableList<Counter> {
//        val size = sharedPreferences.getInt(KEY_LIST_SIZE, 0)
//        val list = mutableListOf<Counter>()
//        for (i in 0 until size) {
//            val title = sharedPreferences.getString(KEY_TITLE_PREFIX + i, "") ?: ""
//            val count = sharedPreferences.getInt(KEY_COUNT_PREFIX + i, 0)
//            list.add(Counter(title, count))
//        }
//        return list
//    }

    fun getList(): MutableList<Counter> {
        val gson = Gson()
        val json = sharedPreferences.getString(KEY_COUNTERS, null)
        val type = object : TypeToken<MutableList<Counter>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }

    fun saveList(list: MutableList<Counter>) {
        val gson = Gson()
        val json = gson.toJson(list)
        Log.d("SharedPreferences", "JSON to be saved: $json")
        sharedPreferences.edit().putString(KEY_COUNTERS, json).apply()
    }
}