package main.kotlin

import java.io.File

private val openCharsRef = listOf('(', '[', '{', '<')
private val closingCharsRef = listOf(')', ']', '}', '>')
private val pointsCorrupted = hashMapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
private val pointsIncomplete = hashMapOf(')' to 1L, ']' to 2L, '}' to 3L, '>' to 4L)

fun main() {
    val lines = File("src/main/input/day10.txt").readLines()
    partOne(lines)
    partTwo(lines)
}

private fun partOne(lines: List<String>) {
    val result = lines.fold(0) { acc, line ->
        val lineErrorScore = getFirstCorruptedChar(line.toList())?.let { corruptedChar ->
            pointsCorrupted.getOrDefault(corruptedChar, 0)
        } ?: 0
        acc + lineErrorScore
    }
    println("Result part 1 $result")
}

private fun partTwo(lines: List<String>) {
    val incompleteScores = lines.mapNotNull { line ->
        val chars = line.toList()
        if (getFirstCorruptedChar(chars) != null) {
            return@mapNotNull null
        }
        computeClosingCharsResult(missingClosingChars = getIncompleteClosedChars(chars))
    }

    val meanValue = incompleteScores.sorted()[incompleteScores.size / 2]
    println("Result part 2 $meanValue")
}

private fun getFirstCorruptedChar(line: List<Char>): Char? {
    val openChars = mutableListOf<Char>()
    line.forEach { char ->
        when (char) {
            in openCharsRef -> openChars.add(char)
            in closingCharsRef -> {
                if (openChars.last().invertOpenChar() != char) {
                    return char
                }
                openChars.removeLast()
                return@forEach
            }
        }
    }
    return null
}

private fun getIncompleteClosedChars(line: List<Char>): List<Char> {
    val openChars = mutableListOf<Char>()
    line.forEach { char ->
        when (char) {
            in openCharsRef -> openChars.add(char)
            in closingCharsRef -> if (openChars.last().invertOpenChar() == char) {
                openChars.removeLast()
            }
        }
    }
    return openChars.asReversed().map { it.invertOpenChar() }
}

private fun computeClosingCharsResult(missingClosingChars: List<Char>): Long = missingClosingChars
    .map { pointsIncomplete.getOrDefault(it, 0L) }
    .fold(0L) { accIncompleteChars, incompleteCharScore ->
        accIncompleteChars * 5 + incompleteCharScore
    }

private fun Char.invertOpenChar() = when (this) {
    '(' -> ')'
    '[' -> ']'
    '{' -> '}'
    '<' -> '>'
    else -> throw IllegalArgumentException("Couldn't invert open char $this")
}