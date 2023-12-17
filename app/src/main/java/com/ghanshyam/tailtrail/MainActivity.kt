package com.ghanshyam.tailtrail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ghanshyam.tailtrail.ui.theme.TailTrailTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TailTrailTheme {
                val viewModel = viewModel<GameViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()
                SnakeGameScreen(
                    state = state,
                    onEvent = viewModel::onEvent
                )
            }
        }
    }
}

