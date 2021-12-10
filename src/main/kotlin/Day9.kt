package main.kotlin

import java.io.File

fun main() {
    val lines = File("src/main/input/day9.txt").readLines()
    partOne(lines)
}

private fun partOne(lines: List<String>) {
    var sumOfMinNumbers = 0
    lines.forEachIndexed { y, line ->
        line.map { it.toString().toInt() }
            .forEachIndexed { x, value ->
                val top = lines.getOrNull(y - 1)?.toList()?.getOrNull(x)?.digitToInt() ?: Int.MAX_VALUE
                val bottom = lines.getOrNull(y + 1)?.toList()?.getOrNull(x)?.digitToInt() ?: Int.MAX_VALUE
                val left = line.getOrNull(x - 1)?.digitToInt() ?: Int.MAX_VALUE
                val right = line.getOrNull(x + 1)?.digitToInt() ?: Int.MAX_VALUE

                if (value < top && value < bottom && value < left && value < right) {
                    sumOfMinNumbers += value + 1
                }
            }
    }
    println("Result part 1 $sumOfMinNumbers")
}