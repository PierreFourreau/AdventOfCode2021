package main.kotlin

import java.io.File

private const val COMMA = ","
private val BLANK_REGEX = "\\s+".toRegex()

fun main() {
    val lines = File("src/main/input/day4.txt").readLines()
    partOne(lines)
}

data class Bingo(
    val inputNumbers: MutableList<Int>,
    val grids: List<List<List<BingoNumber>>>
)

data class BingoNumber(
    val number: Int,
    var marked: Boolean = false
)

private fun partOne(lines: List<String>) {

    val bingo = parseBingo(lines)
    bingo.grids.map { grid ->
        grid.map { line ->
            println("Debug $line")
        }
        println("Debug _____")
    }


    bingo.inputNumbers.forEach { inputNumber ->
        bingo.grids.forEach { grid ->
            grid.forEach { line ->
                line.forEach {
                    if (it.number == inputNumber) {
                        it.marked = true
                    }
                }
            }
        }
//        bingo.grids.flatten().flatten().filter { it.number == inputNumber }.map {
//            println("Result marked" + it + " inputNumber $inputNumber")
//            it.marked = true
//        }

        // need to find 26

        bingo.grids.flatten().forEach {
            if (it.count { it.marked } == 5) {
                println("Result aaaainputNumber $inputNumber")
            }
        }
        bingo.grids.forEach { grid ->
            grid.forEach { line ->
                if (line.count { it.marked } == 5) {
                    val sumOfAllUnmarkedNumbers = grid
                        .flatten()
                        .filter { !it.marked }
                        .sumOf { it.number }
                    println("Result part 1 sumOfAllUnmarkedNumbers $sumOfAllUnmarkedNumbers")
                    println("Result part 1 inputNumber $inputNumber")
                    println("Result part 1 " + sumOfAllUnmarkedNumbers * inputNumber)
                    return
                }
            }
        }

    }
    println("No winner")
}

private fun parseBingo(lines: List<String>): Bingo {
    val parsedNumbers = mutableListOf<Int>()
    val parsedGrids = mutableListOf<List<BingoNumber>>()

    lines.forEachIndexed { index, line ->
        if (line.isEmpty()) {
            return@forEachIndexed
        }
        if (index == 0) {
            parsedNumbers.addAll(line.split(COMMA).map(String::toInt))
            return@forEachIndexed
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