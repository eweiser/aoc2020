package main.kotlin.com.eweiser.aoc2020

fun main(args:Array<String>) {
    val input = {}.javaClass.enclosingClass.getResource("/day7.txt").readText().lines()

    val childToParentBagGraph = buildBagGraph(input, false)
    println(getChildNodes(childToParentBagGraph, "shiny gold").size)

    val parentToChildBagGraph = buildBagGraph(input, true)
    println(getNumBagsContained(parentToChildBagGraph, "shiny gold"))
}

data class BagEdge(val bag: BagNode, val num: Int)
data class BagNode(val color: String, val adjacentBags: MutableList<BagEdge>)

fun buildBagGraph(input: List<String>, parentToChild: Boolean): Map<String, BagNode> {
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
            if (parentToChild) {
                parentBagNode.adjacentBags += BagEdge(childBagNode, num.toInt())
            } else {
                childBagNode.adjacentBags += BagEdge(parentBagNode, num.toInt())
            }
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
    visited.addAll(root.adjacentBags.map { it.bag.color })
    root.adjacentBags.map { traverseChildNodes(nodeMap, it.bag, visited) }
}

fun getNumBagsContained(nodeMap: Map<String, BagNode>, rootColor: String): Int {
    val root = nodeMap[rootColor]
    return getNumBagsInvolved(nodeMap, root!!) - 1
}
fun getNumBagsInvolved(nodeMap: Map<String, BagNode>, root: BagNode): Int {
    return 1 + root.adjacentBags.map { it.num * getNumBagsInvolved(nodeMap, it.bag) }.sum()
}
