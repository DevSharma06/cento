package com.example.cento.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.cento.R
import com.example.cento.data.entities.Expense
import com.example.cento.databinding.ExpenseBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ExpenseBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: ExpenseBottomSheetBinding

    companion object {
        const val TAG = "ExpenseBottomSheet"
    }

    var onExpenseAdded: ((Expense) -> Unit)? = null

    private val expense = Expense()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.expense_bottom_sheet, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.expense = expense
        binding.fragment = this

        setAutoCompleteDropdownItems(view)
        setDefaultDate()
        showDatePicker()
    }

    fun validateInputs(): Boolean {
        var isValid = true

        val amount = binding.tetAmount.text.toString().trim()
        val category = binding.actCategory.text.toString().trim()

        if (amount.isEmpty()) {
            binding.tfAmount.error = "Amount is required"
            isValid = false
        } else {
            binding.tfAmount.error = null
        }

        if (category.isEmpty() || category == "Select") {
            binding.tfCategory.error = "Please select a category"
            isValid = false
        } else {
            binding.tfCategory.error = null
        }

        if (expense.date.toString().isEmpty()) {
            binding.tfDate.error = "Date is required"
            isValid = false
        }

        return isValid
    }

    fun addExpense() {
        if (validateInputs()) {
            Log.d(TAG, "addExpense: $expense")
            onExpenseAdded?.invoke(expense)
            Toast.makeText(requireContext(), "Expense added!", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }

    private fun setAutoCompleteDropdownItems(view: View) {
        val categories = resources.getStringArray(R.array.categories)
        val arrayAdapter = ArrayAdapter(view.context, R.layout.dropdown_item, categories)
        binding.actCategory.setAdapter(arrayAdapter)
    }

    private fun setDefaultDate() {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val currentDate = sdf.format(Date())
        binding.tetDate.setText(currentDate)
        expense.date = Calendar.getInstance().timeInMillis
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun showDatePicker() {
        binding.apply {
            tetDate.inputType = InputType.TYPE_NULL
            tetDate.setOnTouchListener { view, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_UP) {
                    openDatePicker()
                    true
                }
                false
            }
//            tetDate.setOnClickListener {
//                val builder = MaterialDatePicker.Builder.datePicker()
//                val picker = builder.build()
//                picker.show(parentFragmentManager, "DatePicker")
//            }
        }
    }

    private fun openDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        datePicker.show(parentFragmentManager, "DatePicker")

        datePicker.addOnPositiveButtonClickListener { selectedDate ->
            val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val formattedDate = sdf.format(Date(selectedDate))
            binding.tetDate.setText(formattedDate)
            expense.date = selectedDate
        }
    }

    override fun onResume() {
        super.onResume()
        val categories = resources.getStringArray(R.array.categories)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, categories)
        binding.actCategory.setAdapter(arrayAdapter)

    }
}