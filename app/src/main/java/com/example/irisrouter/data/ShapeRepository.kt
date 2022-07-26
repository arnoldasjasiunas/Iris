package com.example.irisrouter.data

import com.example.irisrouter.R
import com.example.irisrouter.model.Shape
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlin.random.Random

object ShapeRepository {
    private val mutableShapeFlow = MutableStateFlow<List<Shape>>(emptyList())
    val shapeFlow = mutableShapeFlow.asStateFlow()

    suspend fun loadShapes(maxAmount: Int) = withContext(Dispatchers.IO) {
        val shapes = generateShapes(maxAmount)
        mutableShapeFlow.value = shapes
        shapes
    }

    private val random = Random(System.currentTimeMillis())

    private fun generateShapes(maxAmount: Int): List<Shape> {
        val items = mutableListOf<Shape>()
        val itemCount = random.nextInt(maxAmount) + 1
        repeat(itemCount) {
            val shape = if (random.nextBoolean()) {
                Shape.Square(R.color.purple_200)
            } else {
                Shape.Circle(R.color.teal_200)
            }
            items.add(shape)
        }
        return items
    }
}