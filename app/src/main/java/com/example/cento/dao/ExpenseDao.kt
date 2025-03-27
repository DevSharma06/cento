package com.example.cento.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.cento.data.entities.Expense

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insertExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT * FROM expenses WHERE date BETWEEN :startOfDay AND :endOfDay")
    suspend fun getExpenseByDay(startOfDay: Long, endOfDay: Long): List<Expense>

    @Query("SELECT * FROM expenses")
    suspend fun getAllExpenses(): List<Expense>
}