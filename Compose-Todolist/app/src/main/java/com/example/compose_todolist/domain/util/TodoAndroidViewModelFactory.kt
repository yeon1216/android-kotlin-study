package com.example.compose_todolist.domain.util

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.compose_todolist.data.respository.RoomTodoRepository
import com.example.compose_todolist.domain.repository.TodoRepository
import com.example.compose_todolist.ui.main.MainViewModel
import java.lang.IllegalArgumentException

class TodoAndroidViewModelFactory(
    private val application: Application,
    private val repository: TodoRepository = RoomTodoRepository(application = application)
) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(application, repository) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
