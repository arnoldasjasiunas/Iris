package com.example.irisrouter.data

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.merge

object AnswerRepository {
    private val mutableAnswerFlow = MutableSharedFlow<Int>()
    val answerFlow get() = mutableAnswerFlow.asSharedFlow()

    suspend fun setAnswer(answer: Int) {
        mutableAnswerFlow.emit(answer)
    }
}