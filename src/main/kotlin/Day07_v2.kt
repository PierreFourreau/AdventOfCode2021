package main.kotlin

import java.io.File
import kotlin.math.abs
import kotlin.math.roundToInt

fun main() {
    val lines = File("src/main/input/day7.txt").readLines()
    val positions = lines.flatMap { line -> line.splitToSequence(',').map { it.toInt() } }

    partOne(positions)
    partTwo(positions)
}

private fun partOne(positions: List<Int>) {
    val median = positions.sorted()[positions.size / 2]
    val result = positions.sumOf { abs(it - median) }
    println("Result part 1 $result")
}

private fun partTwo(positions: List<Int>) {
    val mean = positions.sum().toDouble() / positions.size
    val mean0 = mean.roundToInt()
    val mean1 = if (mean0 <= mean) mean0 + 1 else mean0 - 1
    val result = minOf(positions.sumOf { gauss(abs(it - mean0)) }, positions.sumOf { gauss(abs(it - mean1)) })
    println("Result part 2 $result")
}

private fun gauss(x: Int): Int = x * (x + 1) / 2