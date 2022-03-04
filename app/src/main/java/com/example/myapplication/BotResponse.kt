package com.example.myapplication

import com.example.myapplication.Constants.OPEN_GOOGLE
import com.example.myapplication.Constants.OPEN_SEARCH
import java.lang.Exception

object BotResponse {

    fun basicResponse(msg: String): String {
        val random = (0..2).random()
        val message = msg.toLowerCase()

        return when {
            message.contains("hello") -> {
                when (random) {
                    0 -> "Hello there!"
                    1 -> "Hey, how you doing"
                    2 -> "Welcome how are you today"
                    else -> "I did not understand please try again"
                }
            }

            message.contains("open") && message.contains("google") -> {
                OPEN_GOOGLE
            }

            message.contains("search") -> {
                OPEN_SEARCH
            }

            message.contains("time") && message.contains("?") -> {
                Time.timeStamp()
            }

            message.contains("flip") && message.contains("coin") -> {
                val r = (0..1).random()
                val result = if (r == 0) "heads" else "tails"
                "I flipped a coin and it landed on $result"
            }

            message.contains("solve") -> {
                val equation: String = message.substringAfter("solve")
                return try {
                    val answer = SolveMath.solveMath(equation ?: "0")
                    "$answer"
                } catch (e: Exception) {
                    "Sorry I can't solve that!"
                }
            }

            else -> {
                when (random) {
                    0 -> "Idk"
                    1 -> "I don't understand"
                    2 -> "Welcome how are you today"
                    else -> "error"
                }
            }
        }
    }
}