package main.kotlin.com.eweiser.aoc2020

data class Seat(val row: Int, val column: Int)

fun main(args:Array<String>) {
    val input = {}.javaClass.enclosingClass.getResource("/day5.txt").readText().lines()
    val maxSeatID = input.map { computeSeatID(it) }.max()

    println(maxSeatID)

    val occupiedSeatIDs = input.map { computeSeatID(it) }.toSet()
    for (row in (1 until 127)) {
        for (column in (0 until 8)) {
            val seatID = computeSeatID(Seat(row, column))
            if (!occupiedSeatIDs.contains(seatID) && occupiedSeatIDs.contains(seatID + 1) && occupiedSeatIDs.contains(seatID - 1)) {
                println(seatID)
            }
        }
    }
}

fun computeSeat(seatRepr: String): Seat {
    val binarySeat = seatRepr.replace(Regex("[FL]"), "0").replace(Regex("[BR]"), "1")
    val row = binarySeat.substring(0, 7).toInt(2)
    val column = binarySeat.substring(7, seatRepr.length).toInt(2)

    return Seat(row, column)
}

fun computeSeatID(seatRepr: String): Int {
    val seat = computeSeat(seatRepr)
    return computeSeatID(seat)
}

fun computeSeatID(seat: Seat): Int {
    return (seat.row * 8) + seat.column
}