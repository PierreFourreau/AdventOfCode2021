package main.kotlin

import java.io.File

fun main() {
    val lines = File("src/main/input/day8.txt").readLines()
    partOne(lines)
    partTwo(lines)
}

private fun partOne(lines: List<String>) {
    val uniqueSegmentsCount = lines
        .map { line -> line.split("|") }
        .flatMap { (_, outputs) -> outputs.trim().split(" ") }
        .count { it.length in arrayOf(2, 3, 4, 7) }

    println("Result part 1 $uniqueSegmentsCount")
}

private fun partTwo(lines: List<String>) {
    val result = lines.sumOf { sumOfDigits(it) }
    println("Result part 2 $result")
}

private fun sumOfDigits(line: String): Int {
    val splittedLine = line.split("|")
    val signalPatterns = splittedLine[0].trim().split(" ")
    val outputs = splittedLine[1].trim().split(" ")

    val signalPatternsBySize = mutableMapOf<Int, List<Char>>()
    signalPatterns.forEach {
        when (it.length) {
            2 -> signalPatternsBySize[1] = it.toList()
            4 -> signalPatternsBySize[4] = it.toList()
            3 -> signalPatternsBySize[7] = it.toList()
            7 -> signalPatternsBySize[8] = it.toList()
        }
    }
    var outputValue = ""
    outputs.forEach { output ->
        outputValue += when (output.length) {
            2 -> 1
            4 -> 4
            3 -> 7
            7 -> 8
            else -> decodePattern(signalPatternsBySize, output)
        }
    }
    return outputValue.toInt()
}

private fun decodePattern(signalPatternsBySize: Map<Int, List<Char>>, output: String): String {
    val outputChars = output.toList()
    val onePattern = signalPatternsBySize[1] ?: throw IllegalStateException("Couldn't decode pattern $output")
    val fourPattern = signalPatternsBySize[4] ?: throw IllegalStateException("Couldn't decode pattern $output")

    when (output.length) {
        // 5 segments -> 2 or 3 or 5
        5 -> {
            if (outputChars.containsAll(onePattern)) {
                return "3"
            }
            if (fourPattern.count { output.contains(it) } == 3) {
                return "5"
            }
            return "2"
        }
        // 6 segments -> 0 or 6 or 9
        6 -> {
            if (outputChars.containsAll(fourPattern)) {
                return "9"
            }
            if (outputChars.containsAll(onePattern)) {
                return "0"
            }

            return "6"
        }
    }
    throw IllegalStateException("Couldn't decode pattern $output")
}