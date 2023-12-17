package com.ghanshyam.tailtrail

import android.media.MicrophoneInfo.Coordinate3F
import kotlin.random.Random

data class SnakeGameState(
    val xAxisGridSize: Int = 20,
    val yAxisGridSize: Int = 30,
    val direction: Direction = Direction.RIGHT,
    val snake: List<coordinate> = listOf(coordinate(x = 5, y = 5)),
    val food: coordinate = generateRandomFoodCoordinate(),
    val isGameOver: Boolean = false,
    val gameState: GameState = GameState.IDLE
) {
    companion object {
        fun generateRandomFoodCoordinate(): coordinate {
            return coordinate(
                x = Random.nextInt(from = 1, until = 19),
                y = Random.nextInt(from = 1, until = 29)
            )
        }
    }
}

enum class GameState {
    IDLE,
    STARTED,
    PAUSED
}

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

data class coordinate(
    val x: Int,
    val y: Int
)