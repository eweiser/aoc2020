package main.kotlin.com.eweiser.aoc2020

data class Slope(var right: Int, var down: Int)

fun main(args:Array<String>) {
    val input = {}.javaClass.enclosingClass.getResource("/day3.txt").readText().lines()

    val partOneAnswer = numTreesForSlope(Slope(3, 1), input)
    println(partOneAnswer)

    val slopes = listOf<Slope>(Slope(1, 1), Slope(3, 1), Slope(5, 1), Slope(7, 1), Slope(1, 2))
    println(slopes.map { numTreesForSlope(it, input) })
    val partTwoAnswer = slopes.map { numTreesForSlope(it, input) }.reduce(Long::times)
    println(partTwoAnswer)
}

fun numTreesForSlope(slope: Slope, terrain: List<String>): Long {
    var numTrees = 0L
    var currentX = 0
    for (rowNumber in (slope.down until terrain.size) step slope.down) {
        val row = terrain[rowNumber]
        currentX += slope.right
        currentX %= row.length
        if (row[currentX] == '#') {
            numTrees += 1
        }
    }
    return numTrees
}