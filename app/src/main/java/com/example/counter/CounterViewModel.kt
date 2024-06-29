package com.example.counter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CounterViewModel : ViewModel() {
    private val _counters = MutableLiveData<MutableList<Counter>>()
    val counters: LiveData<MutableList<Counter>> get() = _counters

    init {
        _counters.value = mutableListOf(Counter("New counter", 0))
    }

    fun addCounter(title: String, value: Int) {
        val currentCounters = _counters.value ?: mutableListOf()
        currentCounters.add(Counter(title, value))
        _counters.value = currentCounters
    }

    fun updateCounters(updatedCounters: List<Counter>) {
        _counters.value = updatedCounters.toMutableList()
    }

    fun setCounters(counters: List<Counter>) {
        _counters.value = counters.toMutableList()
    }

    fun saveCounters(sharedPreferencesHelper: SharedPreferencesHelper, key: String) {
        _counters.value?.let {
            sharedPreferencesHelper.saveCounters(key, it)
        }
    }

    fun deleteCounter(index: Int) {
        val currentCounters = _counters.value ?: mutableListOf()
        if (index in currentCounters.indices) {
            currentCounters.removeAt(index)
            _counters.value = currentCounters
        }
    }

    fun resetCounter(index: Int) {
        val currentCounters = _counters.value ?: mutableListOf()
        if (index in currentCounters.indices) {
            currentCounters[index].value = 0
            _counters.value = currentCounters
        }
    }

    fun incrementCounter(index: Int) {
        val currentCounters = _counters.value ?: mutableListOf()
        if (index in currentCounters.indices) {
            currentCounters[index].value += 1
            _counters.value = currentCounters
        }
    }

    fun decrementCounter(index: Int) {
        val currentCounters = _counters.value ?: mutableListOf()
        if (index in currentCounters.indices) {
            currentCounters[index].value -= 1
            _counters.value = currentCounters
        }
    }
}
