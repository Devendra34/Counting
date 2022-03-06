package com.devtech.counting.ui

import androidx.lifecycle.ViewModel
import com.devtech.counting.data.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlin.random.Random

class CountingViewModel : ViewModel() {

    private var _totalItems = MutableStateFlow<List<Item>>(emptyList())
    var totalItems = _totalItems.asStateFlow()

    init {
        reStart()
    }

    fun reStart() {
        var size = Random.nextInt(11)
        if (size == 0) {
            size++
        }
        val items = mutableListOf<Item>()
        for (i in 0..size) {
            items.add(Item(i, false))
        }
        _totalItems.value = items
    }

    fun onItemClicked(position: Int) {
        val previousItems = mutableListOf<Item>()
        previousItems.addAll(_totalItems.value)
        previousItems[position] = previousItems[position].copy(isSelected = true)
        _totalItems.value = previousItems
    }
}