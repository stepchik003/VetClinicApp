package com.example.vetclinic.presentation.utils

import java.util.Locale

object StringUtils {
    fun getAgeString(age: Int): String {
        return when {
            age % 10 == 1 && age % 100 != 11 -> "$age год"
            age % 10 in 2..4 && age % 100 !in 12..14 -> "$age года"
            else -> "$age лет"
        }
    }

    fun getWeightString(weight: Double): String {
        return String.format(Locale.getDefault(), "%.1f кг", weight)
    }
}