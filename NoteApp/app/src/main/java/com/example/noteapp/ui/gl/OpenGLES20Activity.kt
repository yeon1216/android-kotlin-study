package com.example.noteapp.ui.gl

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.noteapp.util.InjectorUtils
import com.example.noteapp.view_model.LineState
import com.example.noteapp.view_model.OpenGLViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OpenGLES20Activity : AppCompatActivity() {
    private lateinit var gLView: GLSurfaceView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val openGLViewModel: OpenGLViewModel =
            ViewModelProvider(
                this@OpenGLES20Activity,
                InjectorUtils.providerOpenGLViewModelFactory(application = application)
            )[OpenGLViewModel::class.java]
        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        gLView = PencilGLSurfaceView(this@OpenGLES20Activity).also {
            it.setViewModel(openGLViewModel)
        }
//        gLView = MyGLSurfaceView(this@OpenGLES20Activity)
        setContentView(gLView)


        // Start a coroutine in the lifecycle scope
        lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Trigger the flow and start listening for values.
                // Note that this happens when lifecycle is STARTED and stops
                // collecting when the lifecycle is STOPPED
                openGLViewModel.lineState.collectLatest { lineState ->
                    when(lineState) {
                        is LineState.Line -> {
                            (gLView as PencilGLSurfaceView).requestRender()
                        }
                        is LineState.NewLine -> {
                            (gLView as PencilGLSurfaceView).requestRender()
                        }
                    }
                }
            }
        }

    }
}