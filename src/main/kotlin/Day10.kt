package main.kotlin

import java.io.File

private val openCharsRef = listOf('(', '[', '{', '<')
private val closingCharsRef = listOf(')', ']', '}', '>')
private val points = hashMapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137
)

fun main() {
    val lines = File("src/main/input/day10.txt").readLines()
    partOne(lines)
}

private fun partOne(lines: List<String>) {
    val result = lines.fold(0) { acc, line ->
        val lineErrorScore = getFirstCorruptedChar(line.toList())?.let { corruptedChar ->
            points.getOrDefault(corruptedChar, 0)
        } ?: 0
        acc + lineErrorScore
    }
    println("Result part 1 $result")
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

private fun Char.invertOpenChar() = when (this) {
    '(' -> ')'
    '[' -> ']'
    '{' -> '}'
    '<' -> '>'
    else -> throw IllegalArgumentException("Couldn't invert open char $this")
}