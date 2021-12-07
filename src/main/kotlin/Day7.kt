package main.kotlin

import java.io.File
import kotlin.math.abs

private const val COMMA = ","

fun main() {
    val lines = File("src/main/input/day7.txt").readLines()
    partOne(crabs = parseCrabs(lines))
}

private fun partOne(crabs: Crabs) {
    var position = 0
    val maxPosition = crabs.getMaxPosition()
    val fuelConsumedMap = mutableListOf<Pair<Int, Int>>()   // Pair: Position -> Fuel consumed

    while (position <= maxPosition) {
        val fuelConsumed = crabs.getFuelConsumedForPosition(position)
        fuelConsumedMap.add((position to fuelConsumed))
        position++
    }

    val lessConsumptionPosition = fuelConsumedMap.minOf { it.second }
    println("Result part 1: $lessConsumptionPosition")
}

private fun parseCrabs(lines: List<String>): Crabs =
    Crabs(crabs = lines.first().split(COMMA).map(String::toInt).map { Crab(it) })

private data class Crab(var position: Int)
private data class Crabs(val crabs: List<Crab>) {
    fun getMaxPosition() = crabs.maxOf { it.position }
    fun getFuelConsumedForPosition(positionDestination: Int): Int = crabs.sumOf { abs(it.position - positionDestination) }
}
