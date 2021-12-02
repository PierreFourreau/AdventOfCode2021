package main.kotlin

import java.io.File

fun main() {
    val lines = File("src/main/input/day1.txt").readLines()
    val intLines = lines.map { it.toInt() }

    partOne(intLines)
    partTwo(intLines)
}

private fun partOne(lines: List<Int>) {
    var increasedLinesCount = 0

    lines.forEachIndexed { index, line ->
        val previousLine = lines.getOrNull(index - 1) ?: return@forEachIndexed
        if (line > previousLine) {
            increasedLinesCount++
        }
    }
    println(increasedLinesCount)
}

private fun partTwo(lines: List<Int>) {
    var increasedLinesCount = 0

    val windowsOfThreeLines = lines.windowed(size = 3, step = 1)
    windowsOfThreeLines.forEachIndexed { index, window ->
        val previousWindow = windowsOfThreeLines.getOrNull(index - 1) ?: return@forEachIndexed
        if (window.sum() > previousWindow.sum()) {
            increasedLinesCount++
        }
    }
    println(increasedLinesCount)
}