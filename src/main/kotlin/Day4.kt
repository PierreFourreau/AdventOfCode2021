package main.kotlin

import java.io.File

private const val COMMA = ","
private val BLANK_REGEX = "\\s+".toRegex()

fun main() {
    val lines = File("src/main/input/day4.txt").readLines()
    val bingo = parseBingo(lines)
    partOne(bingo)
    partTwo(bingo)
}

private fun partOne(bingo: Bingo) {
    bingo.inputNumbers.forEach { inputNumber ->
        bingo.grids.markNumber(inputNumber)
        bingo.grids.forEach { grid ->
            if (checkIfExistsMarkedLine(grid) || checkIfExistsMarkedLine(grid.inverseMatrix())) {
                println("Result part 1 " + grid.sumOfAllUnmarkedNumbers() * inputNumber)
                return
            }
        }
    }
    println("No winner")
}

private fun partTwo(bingo: Bingo) {
    val grids = bingo.grids.toMutableList()
    val winningGrids = mutableListOf<List<List<BingoNumber>>>()
    val totalGridCount = grids.size

    bingo.inputNumbers.forEach { inputNumber ->
        grids.markNumber(inputNumber)

        grids.filter {
            checkIfExistsMarkedLine(it) || checkIfExistsMarkedLine(it.inverseMatrix())
        }.let {
            winningGrids.addAll(it)
            grids.removeAll(it)
        }
        if (winningGrids.size == totalGridCount) {
            println("Result part 2 " + winningGrids.last().sumOfAllUnmarkedNumbers() * inputNumber)
            return
        }
    }
    println("No winner")
}

private fun parseBingo(lines: List<String>): Bingo {
    val parsedNumbers = lines.first().split(COMMA).map(String::toInt)
    val parsedGrids = mutableListOf<List<BingoNumber>>()

    lines.drop(2).forEach { line ->
        if (line.isEmpty()) {
            return@forEach
        }
        parsedGrids.add(line.trim().split(BLANK_REGEX).map {
            BingoNumber(number = it.toInt())
        })
    }
    return Bingo(
        inputNumbers = parsedNumbers,
        grids = parsedGrids.windowed(size = 5, step = 5)
    )
}

data class Bingo(
    val inputNumbers: List<Int>,
    val grids: List<List<List<BingoNumber>>>
)

data class BingoNumber(
    val number: Int,
    var marked: Boolean = false
)

private fun checkIfExistsMarkedLine(grid: List<List<BingoNumber>>): Boolean {
    grid.forEach { line ->
        if (line.count { it.marked } == 5) {
            return true
        }
    }
    return false
}

// Private extensions

private fun List<List<List<BingoNumber>>>.markNumber(inputNumber: Int) {
    this.flatten().flatten().filter { it.number == inputNumber }.map {
        it.marked = true
    }
}

private fun List<List<BingoNumber>>.sumOfAllUnmarkedNumbers(): Int =
    this.flatten().filter { !it.marked }.sumOf { it.number }

private fun List<List<BingoNumber>>.inverseMatrix(): List<List<BingoNumber>> {
    val newList = mutableListOf<List<BingoNumber>>()
    for (i in 0..4) {
        val newLine = mutableListOf<BingoNumber>()
        for (j in 0..4) {
            newLine.add(this[j][i])
        }
        newList.add(newLine)
    }
    return newList
}