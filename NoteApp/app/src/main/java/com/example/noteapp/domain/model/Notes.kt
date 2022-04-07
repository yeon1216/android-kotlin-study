package com.example.noteapp.domain.model


data class Notes(
    val notes: List<Note>
) {
    val allNotes: List<Note> = notes

    companion object {
        fun List<Note>.toNotes(): Notes {
            return Notes(notes = this)
        }
    }

}
