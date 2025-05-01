package com.example.psdmclientapp.enum

enum class DecisionMakingMethod(val id: Long, val displayName: String) {
    AVERAGE_WINNER(1L, "Average winner"),
    WEIGHTED_AVERAGE_WINNER(2L, "Weighted average winner"),
    BORDA_RANKING(3L, "Borda ranking"),
    MAJORITY_RULE(4L, "Majority rule");

    companion object {
        fun fromId(id: Long): DecisionMakingMethod? = DecisionMakingMethod.entries.find { it.id == id }
    }
}
