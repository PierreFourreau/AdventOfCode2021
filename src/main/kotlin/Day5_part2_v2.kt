package main.kotlin

import java.io.File

private const val COMMA = ","
private const val ARROW = "->"

fun main() {
    val lines = File("src/main/input/day5.txt").readLines()
    val vents = parseCoordinates(lines).toMutableList()
    val matrixSize = maxOf(
        maxOf(vents.maxOf { it.from.x }, vents.maxOf { it.from.y }),
        maxOf(vents.maxOf { it.to.x }, vents.maxOf { it.to.y })
    )
    println("Init matrix size $matrixSize")
    val matrix = initEmptyMatrix(matrixSize + 1)

    val straightLines = vents.filter { (it.from.x == it.to.x) || (it.from.y == it.to.y) }
    straightLines.forEach { matrix.drawLine(it) }
    println("Result part 1 : " + matrix.flatten().count { it >= 2 })

    val diagonalLines = vents.filterNot { (it.from.x == it.to.x) || (it.from.y == it.to.y) }
    diagonalLines.forEach { matrix.drawLine(it) }
    println("Result part 2 : " + matrix.flatten().count { it >= 2 })

    println("------------------------------Debugging------------------------------")
    println("Input : $vents")
    println("Matrix :")
    matrix.forEachIndexed { j, line ->
        line.forEachIndexed { i, _ ->
            val point = matrix[i][j]
            print(if (point == 0) "." else point)
        }
        println()
    }
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

// Models

data class Vent(val from: Point, val to: Point)
data class Point(val x: Int, val y: Int)

// Private extensions

private fun Array<Array<Int>>.drawLine(vent: Vent) {
    // Check direction
    val dx = if (vent.from.x < vent.to.x) 1 else if (vent.from.x > vent.to.x) -1 else 0
    val dy = if (vent.from.y < vent.to.y) 1 else if (vent.from.y > vent.to.y) -1 else 0

    var drawPoint = vent.from
    while (drawPoint != vent.to) {
        this[drawPoint.x][drawPoint.y] += 1
        drawPoint = Point(drawPoint.x + dx, drawPoint.y + dy)
    }
    // Draw last point of the line
    this[drawPoint.x][drawPoint.y] += 1
}

