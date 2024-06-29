package com.example.counter

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

    fun saveList(key: String, list: List<Counter>) {
        val editor = sharedPreferences.edit()
        val stringSet = list.map { "${it.title},${it.value}" }.toSet()
        editor.putStringSet(key, stringSet)
        editor.apply()
    }

    fun getList(key: String): List<Counter> {
        val stringSet = sharedPreferences.getStringSet(key, emptySet())
        return stringSet?.map {
            val (text, number) = it.split(",")
            Counter(text, number.toInt())
        } ?: emptyList()
    }


    fun saveCounters(key: String, counters: List<Counter>) {
        val editor = sharedPreferences.edit()
        editor.putInt("${key}_size", counters.size)

        for ((index, counter) in counters.withIndex()) {
            editor.putString("${key}_title_$index", counter.title)
            editor.putInt("${key}_value_$index", counter.value)
        }

        editor.apply()
    }

    fun loadCounters(key: String): List<Counter> {
        val counters = mutableListOf<Counter>()
        val size = sharedPreferences.getInt("${key}_size", 0)

        for (i in 0 until size) {
            val title = sharedPreferences.getString("${key}_title_$i", "") ?: ""
            val value = sharedPreferences.getInt("${key}_value_$i", 0)
            counters.add(Counter(title, value))
        }

        if (counters.isEmpty()) {
            counters.add(Counter("Counter 1", 0)) // Default counter
        }

        return counters
    }
}
