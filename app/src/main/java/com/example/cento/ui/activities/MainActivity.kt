package com.example.cento.ui.activities

import android.os.Bundle
import android.view.View
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
import com.example.cento.utils.TimeInterval
import com.example.cento.viewmodel.ExpenseViewModel
import com.example.cento.viewmodel.ExpenseViewModelFactory
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ExpenseViewModel
    private lateinit var adapter: ExpenseAdapter

    private var timeInterval = TimeInterval.DAY

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

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getAllExpenses()
            binding.swipeRefresh.isRefreshing = false
        }

        setTimeIntervalButtonSelection()
    }

    private fun setTimeIntervalButtonSelection() {
        val buttons = mapOf(
            binding.btDay to TimeInterval.DAY,
            binding.btWeek to TimeInterval.WEEK,
            binding.btMonth to TimeInterval.MONTH,
            binding.btYear to TimeInterval.YEAR
        )

        val selectedButton =
            buttons.entries.find { it.value == viewModel.selectedInterval.value }?.key
        if (selectedButton != null) {
            selectButton(selectedButton, buttons)
        }

        buttons.forEach { button ->
            button.key.setOnClickListener {
                selectButton(button.key, buttons)
                timeInterval = buttons.getOrDefault(button.key, TimeInterval.DAY)
                viewModel.setFilter(
                    buttons.getOrDefault(button.key, TimeInterval.DAY)
                )
            }
        }
    }

    private fun selectButton(selected: View, allButtons: Map<MaterialButton, TimeInterval>) {
        allButtons.forEach { it.key.isSelected = (it.key == selected) }
    }

    private fun setData() {
        viewModel.getAllExpenses()
        viewModel.filteredExpenses.observe(this, { expenses ->
            adapter.setExpensesList(expenses)
        })
    }
}