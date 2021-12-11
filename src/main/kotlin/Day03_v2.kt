package main.kotlin

import java.io.File

fun main() {
    val lines = File("src/main/input/day3.txt").readLines()
    partTwo(lines)
}

private fun partTwo(lines: List<String>) {
    val intLines = lines.map { line -> line.map { it.digitToInt() } }

    val oxygen = computeRating(
        lines = intLines,
        index = 0,
        rating = Rating.OXYGEN
    )
    val co2 = computeRating(
        lines = intLines,
        index = 0,
        rating = Rating.CO2
    )

    println("Result part 2 " + oxygen * co2)
}

private fun computeRating(lines: List<List<Int>>, index: Int, rating: Rating): Int {
    if (lines.size == 1) {
        return lines.first().joinToString("").toInt(2)
    }
    val partition = lines.partition {
        it[index] == 0
    }
    val newLines = when (rating) {
        // Careful : maxOf take the first param if values are equals
        Rating.OXYGEN -> maxOf(partition.second, partition.first, compareBy { it.size })
        Rating.CO2 -> minOf(partition.first, partition.second, compareBy { it.size })
    }
    return computeRating(
        lines = newLines,
        index = index + 1,
        rating = rating
    )
}