package main.kotlin.com.eweiser.aoc2020

fun main(args:Array<String>) {
    val initGroupAnswerSet = "abcdefghijklmnopqrstuvwxyz".toCharArray().toSet()
    val input = {}.javaClass.enclosingClass.getResource("/day6.txt").readText().lines()

    println(getNumGroupYesAnswersUnion(input))
    println(getNumGroupYesAnswersIntersect(input))
}

fun getNumGroupYesAnswersUnion(input: List<String>): Int {
    var numGroupYesAnswers = 0
    var groupAnswerSet = mutableSetOf<Char>()
    for (line in input) {
        if (line == "") {
            numGroupYesAnswers += groupAnswerSet.size
            groupAnswerSet = mutableSetOf()
        } else {
            groupAnswerSet.addAll(line.asIterable())
        }
    }
    numGroupYesAnswers += groupAnswerSet.size

    return numGroupYesAnswers
}

fun getNumGroupYesAnswersIntersect(input: List<String>): Int {
    val initGroupAnswerSet = "abcdefghijklmnopqrstuvwxyz".toCharArray().toSet()
    var numGroupYesAnswers = 0
    var groupAnswerSet = initGroupAnswerSet
    for (line in input) {
        if (line == "") {
            numGroupYesAnswers += groupAnswerSet.size
            groupAnswerSet = initGroupAnswerSet
        } else {
            groupAnswerSet = groupAnswerSet.intersect(line.asIterable())
        }
    }
    numGroupYesAnswers += groupAnswerSet.size

    return numGroupYesAnswers
}