package com.example.cento.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cento.R
import com.example.cento.database.AppDatabase
import com.example.cento.databinding.FragmentHomeBinding
import com.example.cento.ui.adapters.ExpenseAdapter
import com.example.cento.utils.SwipeToDeleteCallback
import com.example.cento.utils.TimeInterval
import com.example.cento.viewmodel.ExpenseViewModel
import com.example.cento.viewmodel.ExpenseViewModelFactory
import com.google.android.material.button.MaterialButton


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: ExpenseViewModel
    private lateinit var adapter: ExpenseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBindings()
        setData()
    }

    private fun setBindings() {
        val expenseDao = AppDatabase.getDatabase(requireActivity().applicationContext).expenseDao()
        val factory = ExpenseViewModelFactory(expenseDao)
        viewModel = ViewModelProvider(requireActivity(), factory)[ExpenseViewModel::class.java]

        adapter = ExpenseAdapter()
        binding.rvExpenses.layoutManager = LinearLayoutManager(context)
        binding.rvExpenses.adapter = adapter

        val swipeHandler = object : SwipeToDeleteCallback(requireActivity()) {
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
            Log.d(TAG, "Insert data $expense")
            viewModel.insert(expense)
        }
        binding.fab.setOnClickListener {
            expenseBottomSheet.show(
                requireActivity().supportFragmentManager,
                ExpenseBottomSheet.TAG
            )
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
        viewModel.filteredExpenses.observe(requireActivity()) { expenses ->
            adapter.setExpensesList(expenses)
        }
    }

    companion object {
        private const val TAG = "HomeFragment"

        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}