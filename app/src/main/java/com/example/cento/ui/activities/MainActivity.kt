package com.example.cento.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cento.R
import com.example.cento.database.AppDatabase
import com.example.cento.databinding.ActivityMainBinding
import com.example.cento.ui.adapters.ExpenseAdapter
import com.example.cento.ui.fragments.ExpenseBottomSheet
import com.example.cento.viewmodel.ExpenseViewModel
import com.example.cento.viewmodel.ExpenseViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ExpenseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val expenseDao = AppDatabase.getDatabase(applicationContext).expenseDao()
        val factory = ExpenseViewModelFactory(expenseDao)
        viewModel = ViewModelProvider(this, factory)[ExpenseViewModel::class.java]

        val adapter = ExpenseAdapter()
        binding.rvExpenses.layoutManager = LinearLayoutManager(this)
        binding.rvExpenses.adapter = adapter

        viewModel.getAllExpenses()
        viewModel.allExpenses.observe(this, { expenses ->
            adapter.setExpensesList(expenses)
        })

        val expenseBottomSheet = ExpenseBottomSheet()

        expenseBottomSheet.onExpenseAdded = { expense ->
            viewModel.insert(expense)
        }

        binding.fab.setOnClickListener {
            expenseBottomSheet.show(supportFragmentManager, ExpenseBottomSheet.TAG)
        }
    }
}