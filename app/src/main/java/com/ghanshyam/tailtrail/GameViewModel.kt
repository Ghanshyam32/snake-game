package com.ghanshyam.tailtrail

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    val state = MutableStateFlow(SnakeGameState())
    val readState = state.asStateFlow()


    fun onEvent(event: SnakeGameEvent) {
        when (event) {
            SnakeGameEvent.PauseGame -> {
                state.update { it.copy(gameState = GameState.PAUSED) }
            }

            SnakeGameEvent.ResetGame -> {
                state.value = SnakeGameState()
            }

            SnakeGameEvent.StartGame -> {
                state.update { it.copy(gameState = GameState.STARTED) }
                viewModelScope.launch {
                    while (state.value.gameState == GameState.STARTED) {
                        val delayMillis = when (state.value.snake.size) {
                            in 1..5 -> 120L  //speed of snake
                            in 6..10 -> 110L
                            else -> 100L
                        }
                        delay(delayMillis)
                        state.value = updateGame(state.value)
                    }
                }
            }

            is SnakeGameEvent.UpdateDirection -> {
                updateDirection(event.offset, event.canvasWidth)
            }
        }
    }

    private fun updateDirection(offset: Offset, canvasWidth: Int) {
        if (!state.value.isGameOver) {
            val cellSize = canvasWidth / state.value.xAxisGridSize
            val tapX = (offset.x / cellSize).toInt()
            val tapY = (offset.y / cellSize).toInt()
            val head = state.value.snake.first()

            state.update {
                it.copy(
                    direction = when (state.value.direction) {
                        Direction.UP, Direction.DOWN -> {
                            if (tapX < head.x) Direction.LEFT else Direction.RIGHT
                        }

                        Direction.LEFT, Direction.RIGHT -> {
                            if (tapY < head.y) Direction.UP else Direction.DOWN
                        }
                    }
                )
            }
        }
    }

    private fun updateGame(currentGame: SnakeGameState): SnakeGameState {
        if (currentGame.isGameOver) {
            return currentGame
        }

        val head = currentGame.snake.first()
        val xAxisGridSize = currentGame.xAxisGridSize
        val yAxisGridSize = currentGame.yAxisGridSize

        val newHead = when (currentGame.direction) {
            Direction.UP -> {
                coordinate(x = head.x, y = (head.y - 1))
            }

            Direction.DOWN -> {
                coordinate(x = head.x, y = (head.y + 1))
            }

            Direction.LEFT -> {
                coordinate(x = head.x - 1, y = (head.y))
            }

            Direction.RIGHT -> {

                coordinate(x = head.x + 1, y = (head.y))
            }
        }

        //if snake collided with itself or bounds
        if (currentGame.snake.contains(newHead) ||
            !isWithinBounds(newHead, xAxisGridSize, yAxisGridSize)
        ) {
            return currentGame.copy(isGameOver = true)
        }

        //check if the snake eats the food
        var newSnake = mutableListOf(newHead) + currentGame.snake
        val newFood = if (newHead == currentGame.food) SnakeGameState.generateRandomFoodCoordinate()
        else currentGame.food

        //update snake length
        if (newHead != currentGame.food) {
            newSnake = newSnake.toMutableList()
            newSnake.removeAt(newSnake.size - 1)
        }

        return currentGame.copy(snake = newSnake, food = newFood)
    }

    private fun isWithinBounds(
        coordinate: coordinate,
        xAxisGridSize: Int,
        yAxisGridSize: Int
    ): Boolean {
        return coordinate.x in 1 until xAxisGridSize - 1
                && coordinate.y in 1 until yAxisGridSize - 1
    }

}