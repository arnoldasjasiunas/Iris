package com.example.irisrouter.srd

import com.example.irisrouter.Constants
import com.example.irisrouter.data.AnswerRepository

/**
 * Decouple logic from activity and build testable code
 */
class SRDRouterCommunicationHandler {
    private val answerRepository = AnswerRepository

    suspend fun handleMessage(m: String, p: String) {
        val count = p.toIntOrNull()
        if(m != Constants.CounterApp.MSG_VALUE_COUNT_LABEL || count == null) {
            return
        }

        answerRepository.setAnswer(count)
    }
}