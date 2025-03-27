package com.example.cento.datasource

import com.example.cento.dao.ExpenseDao
import com.example.cento.data.entities.Expense

class ExpenseLocalSource(private val expenseDao: ExpenseDao) {

    suspend fun getAllExpense(): List<Expense> {
        return expenseDao.getAllExpenses()
    }

    suspend fun insert(expense: Expense) {
        expenseDao.insertExpense(expense)
    }

    suspend fun delete(expense: Expense) {
        expenseDao.deleteExpense(expense)
    }
}