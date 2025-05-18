package com.example.cento.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.cento.R
import com.example.cento.data.entities.Expense
import com.example.cento.databinding.ExpenseItemBinding
import com.example.cento.utils.Helper

class ExpenseAdapter : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    private var expenses = mutableListOf<Expense>()

    fun setExpensesList(expenseList: List<Expense>) {
        this.expenses.addAll(expenseList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = DataBindingUtil.inflate<ExpenseItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.expense_item,
            parent,
            false
        )
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(expenses[position])
    }

    override fun getItemCount() = expenses.size

    fun removeAt(position: Int): Expense {
        val removedExpense = expenses[position]
        this.expenses.removeAt(position)
        notifyItemRemoved(position)
        return removedExpense
    }


    class ExpenseViewHolder(private val binding: ExpenseItemBinding) : ViewHolder(binding.root) {
        fun bind(expense: Expense) {
            binding.expense = expense

            binding.ivCategory.setImageResource(Helper.getCategoryIcon(expense.category))
        }
    }
}