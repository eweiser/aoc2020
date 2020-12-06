package main.kotlin.com.eweiser.aoc2020

fun main(args:Array<String>) {
    val input = {}.javaClass.enclosingClass.getResource("/day2.txt").readText().lines()

    val partOnePasswordEvaluations = input.map { line -> isValidPasswordPartOne(line) }
    println(partOnePasswordEvaluations.filter { it }.size)
    val invalidLineNumbers = getFalsyIndexes(partOnePasswordEvaluations)
    println(invalidLineNumbers.joinToString())

    val partTwoPasswordEvaluations = input.map { line -> isValidPasswordPartTwo(line) }
    println(partTwoPasswordEvaluations.filter { it }.size)
    val partTwoInvalidLineNumbers = getFalsyIndexes(partTwoPasswordEvaluations)
    println(partTwoInvalidLineNumbers.joinToString())
}

data class ParsedLine(val min: Int, val max: Int, val char: Char, val password: String)

fun isValidPasswordPartOne(line: String): Boolean {
    val parsedLine = parseLine(line)
    val charCount = parsedLine.password.filter { it == parsedLine.char }.count()
    return IntRange(parsedLine.min, parsedLine.max).contains(charCount)
}

fun isValidPasswordPartTwo(line: String): Boolean {
    val parsedLine = parseLine(line)
    return (parsedLine.password[parsedLine.min - 1] == parsedLine.char) xor (parsedLine.password[parsedLine.max - 1] == parsedLine.char)
}

fun parseLine(line: String): ParsedLine {
    val pattern = Regex("(\\d+)-(\\d+) ([a-zA-Z]): ([a-zA-Z]+)")
    if (!pattern.matches(line)) {
        throw IllegalArgumentException("invalid line: $line")
    }

    val (all, min, max, char, password) = pattern.find(line)!!.groupValues
    return ParsedLine(min.toInt(), max.toInt(), char.first(), password)
}

fun getFalsyIndexes(iter: Iterable<Boolean>): Iterable<Int> {
    return iter
        .mapIndexed { index, isValid -> Pair(index, isValid) }
        .filter { pair -> !pair.second }.map { pair -> pair.first }
}