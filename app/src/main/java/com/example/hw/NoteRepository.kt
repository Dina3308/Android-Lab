package com.example.hw

object NoteRepository {
    fun getNotes() = arrayListOf(
        Note("BTS", "I like BTS", 1),
        Note("Nail", "Nail is the best teacher", 2),
        Note("Semester work", "I hate semester work", 3),
        Note("Semester work", "I hate semester work", 4),
        Note("Semester work", "I hate semester work", 5)
    )
}