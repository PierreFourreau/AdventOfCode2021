package main.kotlin

import main.kotlin.model.Point
import java.io.File

fun main() {
    val lines = File("src/main/input/day9.txt").readLines()
    val parsedLines = parseInput(lines)
    partOne(parsedLines)
    partTwo(parsedLines)
}

private fun partOne(lines: Array<IntArray>) {
    val sumOfMinNumbers = findLowPointsPositions(lines).sumOf { point ->
        lines[point.y][point.x] + 1
    }
    println("Result part 1 $sumOfMinNumbers")
}

private fun partTwo(lines: Array<IntArray>) {
    val largestBasins = findLowPointsPositions(lines).map { point ->
        findBasinSizeByPoint(lines, point)
    }
    val result = largestBasins
        .sortedDescending()
        .take(3)
        .reduce { acc, next -> acc * next }
    println("Result part 2 $result")
}

private fun findLowPointsPositions(lines: Array<IntArray>): List<Point> {
    val lowPoints = mutableListOf<Point>()
    lines.forEachIndexed { y, line ->
        line.map { it.toString().toInt() }
            .forEachIndexed { x, value ->
                val top = lines.getOrNull(y - 1)?.toList()?.getOrNull(x) ?: Int.MAX_VALUE
                val bottom = lines.getOrNull(y + 1)?.toList()?.getOrNull(x) ?: Int.MAX_VALUE
                val left = line.getOrNull(x - 1) ?: Int.MAX_VALUE
                val right = line.getOrNull(x + 1) ?: Int.MAX_VALUE

                if (value < top && value < bottom && value < left && value < right) {
                    lowPoints.add(Point(x, y))
                }
            }
    }
    return lowPoints
}

private fun findBasinSizeByPoint(lines: Array<IntArray>, point: Point): Int {
    val visitedPoints = mutableSetOf(point)
    val pointsToValidate = mutableListOf(point)

    while (pointsToValidate.isNotEmpty()) {
        val newNeighbors = pointsToValidate
            .removeFirst() // Remove lowest central point
            .getNeighbors() // Get potential neighbors
            .filter { it.y in lines.indices && it.x in lines[it.y].indices } // Exclude points out of area
            .filter { it !in visitedPoints }
            .filter { lines.getOrNull(it.y)?.getOrNull(it.x) != 9 } // Exclude points with height = 9 -> not count
        visitedPoints.addAll(newNeighbors)
        pointsToValidate.addAll(newNeighbors)
    }
    return visitedPoints.size
}

private fun parseInput(lines: List<String>): Array<IntArray> = lines
    .map { line -> line.map { it.digitToInt() }.toIntArray() }
    .toTypedArray()

private fun Point.getNeighbors(): List<Point> = listOf(
    Point(x, y + 1), // top
    Point(x, y - 1), // bottom
    Point(x - 1, y), // left
    Point(x + 1, y) // right
)