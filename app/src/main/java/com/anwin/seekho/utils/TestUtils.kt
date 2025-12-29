package com.anwin.seekho.utils

object TestUtils {

    fun formatAgeRating(rating: String?): String {
        if (rating.isNullOrBlank()) return "NR"

        if (rating.contains("All Ages", true) || rating.startsWith("G")) {
            return "G"
        }

        val match = Regex("(\\d+)").find(rating)
        return match?.let { "${it.value}+" } ?: "NR"
    }

    fun formatCount(value: Int?): String {
        if (value == null || value <= 0) return "0"

        return when {
            value >= 1_000_000_000 -> String.format("%.1fB", value / 1_000_000_000.0)
            value >= 1_000_000 -> String.format("%.1fM", value / 1_000_000.0)
            value >= 1_000 -> String.format("%.1fK", value / 1_000.0)
            else -> value.toString()
        }
    }
}