package com.example.cento.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cento.dao.ExpenseDao
import com.example.cento.datasource.ExpenseLocalSource
import com.example.cento.repository.ExpenseRepository

class ExpenseViewModelFactory(
    private val expenseDao: ExpenseDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            val expenseLocalSource = ExpenseLocalSource(expenseDao)

            val expenseRepository = ExpenseRepository(expenseLocalSource)

            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(expenseRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}