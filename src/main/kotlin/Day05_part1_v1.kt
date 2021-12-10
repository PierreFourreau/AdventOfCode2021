package main.kotlin

import main.kotlin.model.Point
import java.io.File

private const val COMMA = ","
private const val ARROW = "->"

fun main() {
    val lines = File("src/main/input/day5.txt").readLines()
    partOne(lines)
}

private fun partOne(lines: List<String>) {
    val vents = parseCoordinates(lines)
    val matrixSize = maxOf(
        maxOf(vents.maxOf { it.from.x }, vents.maxOf { it.from.y }),
        maxOf(vents.maxOf { it.to.x }, vents.maxOf { it.to.y })
    )
    println("Init matrix size $matrixSize")
    val matrix = initEmptyMatrix(matrixSize + 1)

    vents.forEach { vent ->
        val from = vent.from
        val to = vent.to

        if (from.x == to.x || from.y == to.y) {

            matrix[from.x][from.y] += 1
            matrix[to.x][to.y] += 1

            // x line to right
            if ((to.x - from.x) > 0) {
                for (i in from.x + 1 until to.x) {
                    matrix[i][to.y] += 1
                }
            }
            // x line to left
            if ((from.x - to.x) > 0) {
                for (i in to.x + 1 until from.x) {
                    matrix[i][from.y] += 1
                }
            }
            // y line to bottom
            if ((to.y - from.y) > 0) {
                for (i in from.y + 1 until to.y) {
                    matrix[to.x][i] += 1
                }
            }
            // y line to top
            if ((from.y - to.y) > 0) {
                for (i in to.y + 1 until from.y) {
                    matrix[from.x][i] += 1
                }
            }
        }
    }
    println("Input : $vents")
    println("Matrix :")
    matrix.forEachIndexed { j, line ->
        line.forEachIndexed { i, _ ->
            val point = matrix[i][j]
            print(if (point == 0) "." else point)
        }
        println()
    }
    println("Result part 1 : " + matrix.flatten().count { it >= 2 })
}

private fun initEmptyMatrix(size: Int) = Array(size) { Array(size) { 0 } }

private fun parseCoordinates(lines: List<String>): List<Vent> {
    val vents = mutableListOf<Vent>()
    lines.forEach { line ->
        val coordinates = line.split(ARROW)
        vents.add(
            Vent(
                from = coordinates[0].trim().split(COMMA).let {
                    Point(x = it[0].toInt(), y = it[1].toInt())
                },
                to = coordinates[1].trim().split(COMMA).let {
                    Point(x = it[0].toInt(), y = it[1].toInt())
                })
        )
    }
    return vents
}