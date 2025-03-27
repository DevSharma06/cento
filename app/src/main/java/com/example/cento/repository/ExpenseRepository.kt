package com.example.cento.repository

import com.example.cento.data.entities.Expense
import com.example.cento.datasource.ExpenseLocalSource

class ExpenseRepository(private val expenseSource: ExpenseLocalSource) {

    suspend fun insertExpense(expense: Expense) {
        expenseSource.insert(expense)
    }

    suspend fun deleteExpense(expense: Expense) {
        expenseSource.delete(expense)
    }

    suspend fun getAllExpenses(): List<Expense> {
        return expenseSource.getAllExpense()
    }
}