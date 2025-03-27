package com.example.cento.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "amount") var amount: String = "",
    @ColumnInfo(name = "category") var category: String = "",
    @ColumnInfo(name = "date") var date: Long = 0L,
    @ColumnInfo(name = "description") var description: String = ""
)