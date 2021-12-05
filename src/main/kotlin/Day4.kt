package main.kotlin

import java.io.File

private const val COMMA = ","
private val BLANK_REGEX = "\\s+".toRegex()

fun main() {
    val lines = File("src/main/input/day4.txt").readLines()
    partOne(lines)
}

data class Bingo(
    val inputNumbers: List<Int>, val grids: List<List<List<BingoNumber>>>
)

data class BingoNumber(
    val number: Int, var marked: Boolean = false
)

private fun partOne(lines: List<String>) {
    val bingo = parseBingo(lines)

    bingo.inputNumbers.forEach { inputNumber ->
        bingo.grids.flatten().flatten().filter { it.number == inputNumber }.map {
            it.marked = true
        }

        bingo.grids.forEach { grid ->
            if (checkIfThereIsAWin(grid) || checkIfThereIsAWin(grid.inverseMatrix5x5())) {
                val sumOfAllUnmarkedNumbers = grid.flatten().filter { !it.marked }.sumOf { it.number }
                println("Result part 1 " + sumOfAllUnmarkedNumbers * inputNumber)
                return
            }
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

private fun checkIfThereIsAWin(grid: List<List<BingoNumber>>): Boolean {
    grid.forEach { line ->
        if (line.count { it.marked } == 5) {
            return true
        }
    }
    return false
}

fun List<List<BingoNumber>>.inverseMatrix5x5(): List<List<BingoNumber>> {
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