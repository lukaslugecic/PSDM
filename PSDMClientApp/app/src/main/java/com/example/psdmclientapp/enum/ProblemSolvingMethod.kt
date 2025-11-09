package com.example.psdmclientapp.enum

enum class ProblemSolvingMethod(val id: Long, val displayName: String) {
    BRAINSTORMING(1L, "Brainstorming"),
    BRAINWRITING(2L, "Brainwriting"),
    SPEEDSTORMING(3L, "Speedstorming"),
    NOMINAL_GROUP_TECHNIQUE(4L, "Nominal group technique");

    companion object {
        fun fromId(id: Long): ProblemSolvingMethod? = ProblemSolvingMethod.entries.find { it.id == id }
    }
}