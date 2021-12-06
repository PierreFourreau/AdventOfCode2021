package main.kotlin

import java.io.File

private const val COMMA = ","

fun main() {
    val lines = File("src/main/input/day6.txt").readLines()
    val fishes = parseFishes(lines)

    println("Initial state ${fishes.print()}")

    // 80 days
    for (i in 1 until 81) {
        fishes.stepDay()
        println("After $i days: ${fishes.print()}")
    }
    println("Result part 1: ${fishes.getCount()}")
}

private fun parseFishes(lines: List<String>) = Fishes(
    fishes = lines.first().split(COMMA).map {
        Fish(timer = it.toInt())
    }.toMutableList()
)

private data class Fish(var timer: Int) {
    fun stepDay() {
        timer -= 1
    }

    fun reborn() {
        timer = 6
    }
}

private data class Fishes(val fishes: MutableList<Fish>) {
    fun print() = fishes.map { it.timer }
    fun getCount() = fishes.count()
    fun create() = Fish(timer = 8)
    fun stepDay() {
        val createdFishes = mutableListOf<Fish>()
        fishes.forEach { fish ->
            fish.stepDay()
            if (fish.timer < 0) {
                fish.reborn()
                createdFishes.add(create())
            }
        }
        fishes.addAll(createdFishes)
    }
}