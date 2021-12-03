package main.kotlin

import java.io.File

fun main() {
    val lines = File("src/main/input/day3.txt").readLines()

    partOne(lines)
    partTwo(lines)
}

private fun partOne(lines: List<String>) {
    var gamma = ""
    var epsilon = ""

    for (i in 0..11) {
        val digits = lines.map { it.substring(i, i + 1) }
        val zeroCount = digits.count { it == "0" }
        val oneCount = digits.count { it == "1" }

        if (zeroCount > oneCount) {
            gamma += 0
            epsilon += 1
        } else {
            gamma += 1
            epsilon += 0
        }
    }
    println("Result part 1 " + gamma.toInt(2) * epsilon.toInt(2))
}

private fun partTwo(lines: List<String>) {
    val oxygen = computeRating(lines = lines, rating = Rating.OXYGEN)
    val co2 = computeRating(lines = lines, rating = Rating.CO2)
    println("Result oxygen generator rating $oxygen")
    println("Result co2 generator rating $co2")
    println("Result part 2 " + oxygen * co2)
}

enum class Rating {
    OXYGEN, CO2
}

private fun computeRating(lines: List<String>, rating: Rating): Int {
    val reducedLines = lines.toMutableList()

    for (i in 0..11) {
        val digits = reducedLines.map { it.substring(i, i + 1) }
        val zeroCount = digits.count { it == "0" }
        val oneCount = digits.count { it == "1" }

        val charToRemove = when {
            rating == Rating.OXYGEN && oneCount >= zeroCount ||
                    rating == Rating.CO2 && oneCount < zeroCount -> "0"
            else -> "1"
        }
        reducedLines.removeIf { it.substring(i, i + 1) == charToRemove }

        if (reducedLines.size == 1) {
            return reducedLines.first().toInt(2)
        }
    }
    return -1
}
