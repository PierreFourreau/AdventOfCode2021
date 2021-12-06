package main.kotlin

import java.io.File

private const val COMMA = ","

fun main() {
    val lines = File("src/main/input/day6.txt").readLines()
    partOne(fishes = parseFishes(lines))
    partTwo(fishes = parseFishes(lines))
}

private fun partOne(fishes: Fishes) {
    repeat(80) {
        fishes.stepDay()
    }
    println("Result part 1: ${fishes.getCount()}")
}

private fun partTwo(fishes: Fishes) {
    repeat(256) { fishes.stepDay() }
    println("Result part 2: ${fishes.getCount()}")
}

private fun parseFishes(lines: List<String>): Fishes {
    val fishesMap = mutableMapOf<Int, Long>()
    lines.first().split(COMMA).forEach { fish ->
        if (fishesMap.containsKey(fish.toInt())) {
            fishesMap[fish.toInt()] = fishesMap[fish.toInt()]?.plus(1L) ?: 0
        } else {
            fishesMap[fish.toInt()] = 1L
        }

    }
    return Fishes(fishes = fishesMap)
}

private data class Fishes(val fishes: MutableMap<Int, Long>) {
    fun getCount() = fishes.values.sum()
    fun stepDay() {
        val newFishes = mutableMapOf<Int, Long>()
        fishes.forEach { (age, quantity) ->
            if (age == 0) {
                newFishes[6] = newFishes[6]?.plus(quantity) ?: quantity  // Reborn
                newFishes[8] = newFishes[8]?.plus(quantity) ?: quantity // New life
                return@forEach
            }
            newFishes[age - 1] = newFishes[age - 1]?.plus(quantity) ?: quantity
        }
        fishes.clear()
        fishes.putAll(newFishes)
    }
}