package com.example.cento.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cento.R
import com.example.cento.database.AppDatabase
import com.example.cento.databinding.ActivityMainBinding
import com.example.cento.ui.adapters.ExpenseAdapter
import com.example.cento.ui.fragments.ExpenseBottomSheet
import com.example.cento.utils.SwipeToDeleteCallback
import com.example.cento.viewmodel.ExpenseViewModel
import com.example.cento.viewmodel.ExpenseViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ExpenseViewModel
    private lateinit var adapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setBindings()
        setData()
    }

    private fun setBindings() {
        val expenseDao = AppDatabase.getDatabase(applicationContext).expenseDao()
        val factory = ExpenseViewModelFactory(expenseDao)
        viewModel = ViewModelProvider(this, factory)[ExpenseViewModel::class.java]

        adapter = ExpenseAdapter()
        binding.rvExpenses.layoutManager = LinearLayoutManager(this)
        binding.rvExpenses.adapter = adapter

        val swipeHandler = object : SwipeToDeleteCallback(baseContext) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                super.onSwiped(viewHolder, direction)

                val adapter = binding.rvExpenses.adapter as ExpenseAdapter
                val expense = adapter.removeAt(viewHolder.adapterPosition)
                viewModel.delete(expense)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.rvExpenses)

        val expenseBottomSheet = ExpenseBottomSheet()
        expenseBottomSheet.onExpenseAdded = { expense ->
            viewModel.insert(expense)
        }
        binding.fab.setOnClickListener {
            expenseBottomSheet.show(supportFragmentManager, ExpenseBottomSheet.TAG)
        }
    }

    private fun setData() {
        viewModel.getAllExpenses()
        viewModel.allExpenses.observe(this, { expenses ->
            adapter.setExpensesList(expenses)
        })
    }
}