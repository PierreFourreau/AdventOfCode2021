package main.kotlin

import main.kotlin.model.Point
import java.io.File

private const val X = "x"
private const val Y = "y"
private val regexPoint = "(.+),(.+)".toRegex()
private val regexInstruction = "fold along ([a-z])=(.+)".toRegex()

fun main() {
    val lines = File("src/main/input/day13.txt").readLines()
    val instructions = parseInstructions(lines)
    partOne(instructions)
    partTwo(instructions)
}

private fun partOne(instructions: Instructions) {
    println("Result part 1 " + instructions.fold(instructionsCountToTake = 1).distinct().size)
}

private fun partTwo(instructions: Instructions) {
    println("Result part 2 ")
    println(instructions.fold().print())    // Print 'UFRZKAUZ'
}

private fun parseInstructions(input: List<String>) = Instructions(
    points = input.mapNotNull { line -> regexPoint.matchEntire(line)?.destructured }.map { (x, y) -> Point(x.toInt(), y.toInt()) },
    instructions = input.mapNotNull { line -> regexInstruction.matchEntire(line)?.destructured }.map { (axis, value) -> Pair(axis, value.toInt()) }
)

private data class Instructions(val points: List<Point>, val instructions: List<Pair<String, Int>>) {
    fun fold(instructionsCountToTake: Int = instructions.size): List<Point> =
        instructions.take(instructionsCountToTake).fold(points) { accumulator: List<Point>, instruction: Pair<String, Int> ->
            accumulator.map { point ->
                when {
                    instruction.first == X && instruction.second < point.x -> point.copy(x = instruction.second - (point.x - instruction.second))
                    instruction.first == Y && instruction.second < point.y -> point.copy(y = instruction.second - (point.y - instruction.second))
                    else -> point
                }
            }
        }
}

private fun List<Point>.print() {
    val maxX = this.maxOf { it.x }
    val maxY = this.maxOf { it.y }
    (0..maxY).forEach { y ->
        (0..maxX).forEach { x ->
            print(this.find { it.x == x && it.y == y }?.let { "#" } ?: ".")
        }
        println()
    }
}