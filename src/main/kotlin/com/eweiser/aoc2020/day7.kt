package main.kotlin.com.eweiser.aoc2020

fun main(args:Array<String>) {
    val input = {}.javaClass.enclosingClass.getResource("/day7.txt").readText().lines()
    val bagGraph = buildBagGraph(input)
    println(getChildNodes(bagGraph, "shiny gold").size)
}

data class BagNode(val color: String, val containerBags: MutableList<Pair<BagNode, Int>>)

fun buildBagGraph(input: List<String>): Map<String, BagNode> {
    val nodeMap = mutableMapOf<String, BagNode>()
    val childBagDescriptorPattern = Regex("(\\d+) ([a-zA-Z\\s]+) bags?\\.?")
    for (line in input) {
        val partitionedLine = line.split("bags contain")
        val parentBagColor = partitionedLine[0].trim()
        val parentBagNode = nodeMap.getOrPut(parentBagColor) { BagNode(parentBagColor, mutableListOf()) }

        if ("contain no other bags" in line) {
            continue
        }
        for (childBagDescriptor in partitionedLine[1].split(", ")) {
            if (!childBagDescriptorPattern.matches(childBagDescriptor.trim())) {
                throw IllegalArgumentException("invalid line: $line, chunk $childBagDescriptor")
            }
            val (_, num, childBagColor) = childBagDescriptorPattern.find(childBagDescriptor)!!.groupValues
            val childBagNode = nodeMap.getOrPut(childBagColor) { BagNode(childBagColor, mutableListOf()) }
            childBagNode.containerBags += Pair(parentBagNode, num.toInt())
        }
    }

    return nodeMap
}

fun getChildNodes(nodeMap: Map<String, BagNode>, rootColor: String): Set<String> {
    val root = nodeMap[rootColor]
    val visited = mutableSetOf(rootColor)
    traverseChildNodes(nodeMap, root!!, visited)
    return visited.toSet().minus(rootColor)
}

fun traverseChildNodes(nodeMap: Map<String, BagNode>, root: BagNode, visited: MutableSet<String>) {
    visited.addAll(root.containerBags.map { it.first.color })
    root.containerBags.map { traverseChildNodes(nodeMap, it.first, visited) }
}