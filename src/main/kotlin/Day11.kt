package main.kotlin

import java.io.File

fun main() {
    val lines = File("src/main/input/day11.txt").readLines()
    partOne(lines)
}

private fun partOne(lines: List<String>) {
    val octopuses = parseOctopuses(lines)
    var flashesCount = 0
    octopuses.print("Before any steps")

    repeat(100) { index ->
        flash(octopusArray = octopuses.incrementAll(), flashesCount = flashesCount)
        flashesCount += octopuses.getFlashedCount()
        octopuses.resetFlashed()
        octopuses.print("After step ${index + 1} with flashesCount $flashesCount")
    }
    println("Result part 1 $flashesCount")
}

private fun parseOctopuses(lines: List<String>): Array<Array<Octopus>> =
    lines.map { line -> line.map { Octopus(it.digitToInt()) }.toTypedArray() }.toTypedArray()

private fun flash(octopusArray: Array<Array<Octopus>>, flashesCount: Int = 0): Int {
    octopusArray.mapIndexed { y, line ->
        line.mapIndexed { x, octopus ->
            if (octopus.energy > 9 && !octopus.isFlashed) {
                // find adjacent and increase by 1
                val top = octopusArray.getOrNull(y - 1)?.getOrNull(x)
                val topLeft = octopusArray.getOrNull(y - 1)?.getOrNull(x - 1)
                val topRight = octopusArray.getOrNull(y - 1)?.getOrNull(x + 1)
                val bottom = octopusArray.getOrNull(y + 1)?.getOrNull(x)
                val bottomLeft = octopusArray.getOrNull(y + 1)?.getOrNull(x - 1)
                val bottomRight = octopusArray.getOrNull(y + 1)?.getOrNull(x + 1)
                val left = line.getOrNull(x - 1)
                val right = line.getOrNull(x + 1)

                top?.let { octopusArray[y - 1][x] = top.increment() }
                topLeft?.let { octopusArray[y - 1][x - 1] = topLeft.increment() }
                topRight?.let { octopusArray[y - 1][x + 1] = topRight.increment() }
                bottom?.let { octopusArray[y + 1][x] = bottom.increment() }
                bottomLeft?.let { octopusArray[y + 1][x - 1] = bottomLeft.increment() }
                bottomRight?.let { octopusArray[y + 1][x + 1] = bottomRight.increment() }
                left?.let { octopusArray[y][x - 1] = left.increment() }
                right?.let { octopusArray[y][x + 1] = right.increment() }
                octopus.isFlashed = true

                flash(
                    octopusArray = octopusArray,
                    flashesCount = flashesCount + 1
                )
            }
        }
    }
    return flashesCount
}

private fun Array<Array<Octopus>>.incrementAll() = this.map { line -> line.map { it.increment() }.toTypedArray() }.toTypedArray()
private fun Array<Array<Octopus>>.getFlashedCount() = this.flatten().count { it.isFlashed }
private fun Array<Array<Octopus>>.resetFlashed() = this.flatten().filter { it.isFlashed }.map { octopus ->
    octopus.energy = 0
    octopus.isFlashed = false
}

private fun Array<Array<Octopus>>.print(title: String) {
    println(title)
    this.forEach { line ->
        line.forEach { octopus ->
            print(octopus.energy)
        }
        println()
    }
    println("____________________________")
}

private data class Octopus(var energy: Int, var isFlashed: Boolean = false) {
    fun increment(): Octopus {
        energy += 1
        return this
    }
}