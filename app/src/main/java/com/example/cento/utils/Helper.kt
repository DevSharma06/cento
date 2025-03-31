package com.example.cento.utils

import com.example.cento.R

object Helper {

    fun getCategoryIcon(category: String): Int {
        return when (category) {
            "Food & Dining" -> R.drawable.food
            "Transport" -> R.drawable.transport
            "Shopping" -> R.drawable.shopping
            "Health & Fitness" -> R.drawable.health
            "Entertainment" -> R.drawable.entertainment
            "Housing" -> R.drawable.housing
            "Utilities" -> R.drawable.utilities
            "Insurance" -> R.drawable.insurance
            "Education" -> R.drawable.education
            "Travel" -> R.drawable.travel
            "Personal Care" -> R.drawable.care
            "Gifts & Donations" -> R.drawable.gift
            "Investments" -> R.drawable.investments
            "Miscellaneous" -> R.drawable.misc
            else -> R.drawable.misc
        }
    }

}