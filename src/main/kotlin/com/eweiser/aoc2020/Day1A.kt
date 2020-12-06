package main.kotlin.com.eweiser.aoc2020

fun main(args:Array<String>) {
    val input = {}.javaClass.enclosingClass.getResource("/day1.txt").readText().lines().map { line -> line.toInt() }.toSet()
    val partOneAnswer = findProductOfNumbersThatSum(input, 2020, 2)
    val partTwoAnswer = findProductOfNumbersThatSum(input, 2020, 3)
    println(partOneAnswer)
    println(partTwoAnswer)
}

fun findProductOfNumbersThatSum(numbers: Set<Int>, desiredSum: Int, numNumbesToSum: Int): Int? {
    if (numNumbesToSum == 1) {
        return if (desiredSum in numbers) desiredSum else null
    }

    for (number in numbers) {
        val remainderProduct = findProductOfNumbersThatSum(numbers, desiredSum - number, numNumbesToSum - 1)

        if (remainderProduct != null) {
            return number * remainderProduct
        }
    }

    return null
}