package com.example.irisrouter.srd

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * This allows to decouple form Activity and couple only with the components concerned.
 * Supports data-driven architecture
 */
object SRDRouterEventManager {

    private val mutableEventFlow = MutableSharedFlow<Event>()
    val eventFlow get() = mutableEventFlow.asSharedFlow()

    suspend fun onRegisteredWithSRDRouter() {
        mutableEventFlow.emit(Connected)
    }

    suspend fun onDisconnectedFromSRDRouter() {
        mutableEventFlow.emit(Disconnected)
    }

    sealed class Event
    object Disconnected : Event()
    object Connected : Event()
}