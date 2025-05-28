package com.example.cento.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cento.data.entities.Expense
import com.example.cento.repository.ExpenseRepository
import com.example.cento.utils.DateUtils
import com.example.cento.utils.TimeInterval
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {

    private val _allExpenses = MutableLiveData<List<Expense>>()
    val allExpenses: LiveData<List<Expense>> = _allExpenses

    private val _filteredExpenses = MediatorLiveData<List<Expense>>()
    val filteredExpenses: LiveData<List<Expense>> = _filteredExpenses

    private val _selectedInterval = MutableLiveData(TimeInterval.DAY)
    val selectedInterval: LiveData<TimeInterval> = _selectedInterval

    init {
        _filteredExpenses.addSource(_allExpenses) { updateFilteredExpenses() }
        _filteredExpenses.addSource(_selectedInterval) { updateFilteredExpenses() }
    }

    fun setFilter(timeInterval: TimeInterval) {
        _selectedInterval.value = timeInterval
    }

    private fun updateFilteredExpenses() {
        val all = allExpenses.value ?: emptyList()
        val filter = _selectedInterval.value ?: TimeInterval.DAY
        _filteredExpenses.value = all.filter {
            DateUtils.filterExpenseByTimeInterval(
                filter,
                it.date
            )
        }
        println("All expenses list ${allExpenses.value}")
    }

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