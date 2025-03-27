package com.example.cento.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cento.data.entities.Expense
import com.example.cento.repository.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {

    private val _allExpenses = MutableLiveData<List<Expense>>()
    val allExpenses: LiveData<List<Expense>> = _allExpenses

    fun getAllExpenses() {
        viewModelScope.launch {
            val expensesList = repository.getAllExpenses()
            _allExpenses.value = expensesList
        }
    }

    fun insert(expense: Expense) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            repository.insertExpense(expense)
            getAllExpenses()
        }
    }

    fun delete(expense: Expense) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            repository.deleteExpense(expense)
        }
    }
}