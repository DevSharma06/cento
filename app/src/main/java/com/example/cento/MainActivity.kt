package com.example.cento

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.cento.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val expenseBottomSheet = ExpenseBottomSheet()

        binding.fab.setOnClickListener {
            expenseBottomSheet.show(supportFragmentManager, ExpenseBottomSheet.TAG)
        }
    }
}