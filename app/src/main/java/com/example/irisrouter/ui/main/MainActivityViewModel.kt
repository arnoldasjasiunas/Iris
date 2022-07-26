package com.example.irisrouter.ui.main

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.irisrouter.R
import com.example.irisrouter.data.AnswerRepository
import com.example.irisrouter.data.ShapeRepository
import com.example.irisrouter.model.Shape
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {
    private val answerRepository = AnswerRepository
    private val shapeRepository = ShapeRepository

    private var currentShapes = emptyList<UiShape>()

    private val answerFlow = answerRepository.answerFlow.map { answer ->
        val shapes = currentShapes
        val message = if (answer == shapes.size) {
            R.string.main_message_success
        } else {
            R.string.main_message_failure
        }
        val newShapes = loadShapes()
        UiState(newShapes, message)
    }

    private val initialState = MutableStateFlow(UiState(currentShapes))
    val uiState = merge(answerFlow, initialState)

    init {
        viewModelScope.launch {
            val uiShapes = loadShapes()
            initialState.value = UiState(uiShapes)
        }
    }

    private suspend fun loadShapes(): List<UiShape> {
        val shapes = shapeRepository.loadShapes(MAX_ITEMS).map(Shape::toUi)
        currentShapes = shapes
        return shapes
    }

    data class UiState(
        val shapes: List<UiShape>,
        @StringRes val message: Int? = null,
    )

    sealed class UiShape(@ColorRes val color: Int) {
        class UiSquare(color: Int) : UiShape(color)
        class UiCircle(color: Int) : UiShape(color)
    }

    companion object {
        const val MAX_ITEMS = 10
    }
}

fun Shape.toUi() = when (this) {
    is Shape.Circle -> MainActivityViewModel.UiShape.UiCircle(color)
    is Shape.Square -> MainActivityViewModel.UiShape.UiSquare(color)
}