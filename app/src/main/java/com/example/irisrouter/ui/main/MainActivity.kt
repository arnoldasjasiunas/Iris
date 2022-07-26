package com.example.irisrouter.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.GridLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.irisrouter.R
import com.example.irisrouter.databinding.ActivityMainBinding
import com.example.irisrouter.srd.SRDRouterCommunicationService
import com.example.irisrouter.toColorInt
import com.example.irisrouter.ui.CirclePaintable
import com.example.irisrouter.ui.PaintView
import com.example.irisrouter.ui.SquarePaintable
import com.example.irisrouter.ui.main.MainActivityViewModel.UiShape
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        collectUiState()
    }

    override fun onStart() {
        super.onStart()
        startSRDService()
    }

    override fun onStop() {
        super.onStop()
        stopSRDService()
    }

    private fun startSRDService() {
        Intent(this, SRDRouterCommunicationService::class.java).also { intent ->
            startService(intent)
        }
    }

    private fun stopSRDService() {
        stopService(Intent(this, SRDRouterCommunicationService::class.java))
    }

    private fun collectUiState() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiState.collect { state ->
                state.message?.let { messageResId ->
                    Toast.makeText(this@MainActivity, messageResId, Toast.LENGTH_SHORT).show()
                }

                updateShapes(state.shapes)
            }
        }
    }

    private fun updateShapes(shapes: List<UiShape>) {
        // can be optimized
        binding.grid.removeAllViews()
        shapes.forEach(::addShape)
    }

    private fun addShape(shape: UiShape) {
        val paintable = getPaintable(shape)
        val view = PaintView(
            this,
            0,
            resources.getDimension(R.dimen.shape_container_height).toInt(),
            paintable
        )
        view.layoutParams =
            GridLayout.LayoutParams(GridLayout.spec(getRow(), 0F), GridLayout.spec(getColumn(), 1F))
                .apply {
                    height = resources.getDimension(R.dimen.shape_container_height)
                        .toInt()
                    width = 0
                }

        binding.grid.addView(view)
    }

    private fun getPaintable(shape: UiShape) = when (shape) {
        is UiShape.UiCircle -> CirclePaintable(shape.color.toColorInt(this))
        is UiShape.UiSquare -> SquarePaintable(shape.color.toColorInt(this))
    }

    private fun getColumn(): Int {
        val column = binding.grid.childCount % 2
        return column
    }

    private fun getRow(): Int {
        val count = binding.grid.childCount
        ///val column = count % 2
        return count / 2
    }
}