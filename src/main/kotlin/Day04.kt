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
    val inputNumbers = bingo.inputNumbers
    val grids = bingo.grids

    inputNumbers.forEach { inputNumber ->
        grids.markNumber(inputNumber)
        grids.forEach { grid ->
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
    val winningGrids = mutableListOf<Grid>()
    val totalGridCount = grids.size

    bingo.inputNumbers.forEach { inputNumber ->
        grids.markNumber(inputNumber)

        grids.filter { checkIfExistsMarkedLine(it) || checkIfExistsMarkedLine(it.inverseMatrix()) }
            .let {
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
    return Bingo(inputNumbers = parsedNumbers, grids = parsedGrids.windowed(size = 5, step = 5).map { Grid(it) })
}

private fun checkIfExistsMarkedLine(grid: Grid): Boolean = grid.lines.any { line -> line.all { it.marked } }

// Models

data class Bingo(
    val inputNumbers: List<Int>, val grids: List<Grid>
)

data class Grid(val lines: List<List<BingoNumber>>)

data class BingoNumber(
    val number: Int, var marked: Boolean = false
)

// Private extensions

private fun List<Grid>.markNumber(inputNumber: Int) {
    this.forEach { grid ->
        grid.lines.flatten().filter { it.number == inputNumber }.map { it.marked = true }
    }
}

private fun Grid.sumOfAllUnmarkedNumbers(): Int = this.lines.flatten().filter { !it.marked }.sumOf { it.number }

private fun Grid.inverseMatrix(): Grid {
    val newList = mutableListOf<List<BingoNumber>>()
    val size = this.lines.size - 1
    for (i in 0..size) {
        val newLine = mutableListOf<BingoNumber>()
        for (j in 0..size) {
            newLine.add(this.lines[j][i])
        }
        newList.add(newLine)
    }
    return Grid(newList)
}