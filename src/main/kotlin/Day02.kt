package main.kotlin

import java.io.File

private const val FORWARD = "forward"
private const val DOWN = "down"
private const val UP = "up"
private const val BLANK = " "

fun main() {
    val resultPartOne = parseSubmarineMovements(
        fileName = "src/main/input/day2.txt",
        submarine = SubmarineFactory.create(withAim = false)
    )
    println("Result part 1 $resultPartOne")

    val resultPartTwo = parseSubmarineMovements(
        fileName = "src/main/input/day2.txt",
        submarine = SubmarineFactory.create(withAim = true)
    )
    println("Result part 2 $resultPartTwo")
}

class SubmarineFactory {
    companion object {
        fun create(withAim: Boolean): Submarine = if (withAim) {
            SubmarineV2()
        } else {
            SubmarineV1()
        }
    }
}

abstract class Submarine(
    protected var horizontalPosition: Int = 0,
    protected var depth: Int = 0
) {
    abstract fun up(value: Int)
    abstract fun down(value: Int)
    abstract fun forward(value: Int)
    fun computeResult() = horizontalPosition * depth
}

interface Movement {
    fun up(value: Int)
    fun down(value: Int)
    fun forward(value: Int)
}

class SubmarineV1 : Submarine(), Movement {

    override fun up(value: Int) {
        depth -= value
    }

    override fun down(value: Int) {
        depth += value
    }

    override fun forward(value: Int) {
        horizontalPosition += value
    }
}

class SubmarineV2(private var aim: Int = 0) : Submarine(), Movement {

    override fun up(value: Int) {
        aim -= value
    }

    override fun down(value: Int) {
        aim += value
    }

    override fun forward(value: Int) {
        horizontalPosition += value
        depth += aim * value
    }
}

private fun parseSubmarineMovements(
    fileName: String,
    submarine: Submarine
): Int {
    val lines = File(fileName).readLines()

    lines.forEach { line ->
        val splitLine = line.split(BLANK)
        val directionLabel = splitLine[0]
        val directionValue = splitLine[1].toInt()

        when (directionLabel) {
            UP -> submarine.up(directionValue)
            DOWN -> submarine.down(directionValue)
            FORWARD -> submarine.forward(directionValue)
        }
    }
    return submarine.computeResult()
}