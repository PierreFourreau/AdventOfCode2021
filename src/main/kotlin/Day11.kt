package main.kotlin

import main.kotlin.model.Point
import java.io.File

fun main() {
    val lines = File("src/main/input/day11.txt").readLines()
    partOne(lines)
    partTwo(lines)
}

private fun partOne(lines: List<String>) {
    val octopuses = parseOctopuses(lines)
    var flashesCount = 0

    repeat(100) {
        flash(octopusArray = octopuses.incrementAll(), flashesCount = flashesCount)
        flashesCount += octopuses.getFlashedCount()
        octopuses.resetFlashed()
    }
    println("Result part 1 $flashesCount")
}

private fun partTwo(lines: List<String>) {
    val octopuses = parseOctopuses(lines)
    val octopusesCount = octopuses.flatten().size
    var step = 0

    while (octopuses.getZeroEnergyCount() != octopusesCount) {
        flash(octopusArray = octopuses.incrementAll())
        octopuses.resetFlashed()
        step++
    }
    println("Result part 2 $step")
}

private fun parseOctopuses(lines: List<String>): Array<Array<Octopus>> =
    lines.map { line -> line.map { Octopus(it.digitToInt()) }.toTypedArray() }.toTypedArray()

private fun flash(octopusArray: Array<Array<Octopus>>, flashesCount: Int = 0) {
    octopusArray.mapIndexed { y, line ->
        line.mapIndexed { x, octopus ->
            if (octopus.needToBeFlashed()) {
                Pair(x, y).getAdjacentPoints().forEach { point ->
                    octopusArray.getOrNull(point.y)?.getOrNull(point.x)?.let { octopus ->
                        octopusArray[point.y][point.x] = octopus.increment()
                    }
                }
                octopus.isFlashed = true
                flash(octopusArray = octopusArray, flashesCount = flashesCount + 1)
            }
        }
    }
}

private fun Array<Array<Octopus>>.incrementAll() = this.map { line -> line.map { it.increment() }.toTypedArray() }.toTypedArray()
private fun Array<Array<Octopus>>.getFlashedCount() = this.flatten().count { it.isFlashed }
private fun Array<Array<Octopus>>.getZeroEnergyCount() = this.flatten().count { it.energy == 0 }
private fun Array<Array<Octopus>>.resetFlashed() = this.flatten().filter { it.isFlashed }.map { octopus ->
    octopus.energy = 0
    octopus.isFlashed = false
}

private fun Pair<Int, Int>.getAdjacentPoints() = listOf(
    Point(first, second - 1),           // Top
    Point(first - 1, second - 1),    // Top left
    Point(first + 1, second - 1),    // Top right
    Point(first, second + 1),           // Bottom
    Point(first - 1, second + 1),    // Bottom left
    Point(first + 1, second + 1),    // Bottom right
    Point(first - 1, second),           // Left
    Point(first + 1, second),           // Right
)

private data class Octopus(var energy: Int, var isFlashed: Boolean = false) {
    fun increment(): Octopus {
        energy += 1
        return this
    }

    fun needToBeFlashed() = this.energy > 9 && !this.isFlashed
}