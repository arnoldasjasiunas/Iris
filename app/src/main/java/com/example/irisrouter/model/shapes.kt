package com.example.irisrouter.model

import androidx.annotation.ColorRes

sealed class Shape(@ColorRes val color: Int) {
    class Square(color: Int) : Shape(color)
    class Circle(color: Int) : Shape(color)
}


