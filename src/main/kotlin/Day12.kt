package main.kotlin

import java.io.File

private const val START = "start"
private const val END = "end"
private const val HYPHEN = "-"

/*
* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
*
* Code inspiration 99,99% from https://github.com/tginsberg/advent-2021-kotlin/blob/master/src/main/kotlin/com/ginsberg/advent2021/Day12.kt
*
* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 */
fun main() {
    val lines = File("src/main/input/day12.txt").readLines()
    val allConnections = parseConnections(lines)
    partOne(allConnections)
    partTwo(allConnections)
}

fun partOne(allConnections: Map<String, List<String>>) {
    val result = traverse(allConnections, ::part1VisitRule).size
    println("Result part 1 $result")
}

fun partTwo(allConnections: Map<String, List<String>>) {
    val result = traverse(allConnections, ::part2VisitRule).size
    println("Result part 2 $result")
}

private fun traverse(
    allConnections: Map<String, List<String>>,
    allowedToVisit: (String, List<String>) -> Boolean,
    path: List<String> = listOf(START)
): List<List<String>> = if (path.last() == END) {
    listOf(path)
} else {
    allConnections.getValue(path.last())           // Get current path
        .filter { allowedToVisit(it, path) }       // Remove caves not allowed to visit
        .flatMap { traverse(allConnections, allowedToVisit, path + it) }
}

private fun part1VisitRule(name: String, path: List<String>): Boolean = !name.isSmallCave() || name !in path

private fun part2VisitRule(name: String, path: List<String>): Boolean = when {
    !name.isSmallCave() -> true
    name == START -> false
    name !in path -> true
    else -> path
        .filter { it.isSmallCave() }
        .groupBy { it }
        .none { it.value.size == 2 }
}

private fun String.isSmallCave(): Boolean = none { it.isUpperCase() }

private fun parseConnections(input: List<String>): Map<String, List<String>> = input.map { it.split(HYPHEN) }
    // Because connections are readable from right and from left
    .flatMap { listOf(it.first() to it.last(), it.last() to it.first()) }
    .groupBy({ it.first }, { it.second })
