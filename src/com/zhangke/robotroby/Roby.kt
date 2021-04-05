package com.zhangke.robotroby

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.round
import kotlin.random.Random


fun main() {
    println("Thread running...")
    Roby().startEvolution()
    println("Thread run finished")
}

class Roby {

    private val genesCount = 243
    private val dnaCount = 200
    private val generationCount = 2000
    private val cleanCount = 100
    private val mutationFactor = 0.01
    private val jarInRoomFactor = 0.5F

    fun startEvolution() {
        val initGeneList = mutableListOf<DNA>()
        for (i in 0 until dnaCount) {
            initGeneList += DNA.generate(genesCount)
        }
        var cur = initGeneList.toList()
        for (i in 0 until generationCount) {
            cur = evolution(cur, i)
        }
    }

    // public for test
    fun evolution(geneList: List<DNA>, count: Int): List<DNA> {
        computeScore(geneList)
        printScoreAverage(geneList, count)
        val parents = pickParents(geneList)
        val children = fuckNeighbor(parents)
        mutation(children)
        return children
    }

    // public for test
    fun computeScore(dnaList: List<DNA>) {
        val threadCount = Runtime.getRuntime().availableProcessors()
        val averageCount = dnaList.size / threadCount
        val threadArray = Array(threadCount) {
            if (it == threadCount - 1) {
                CleanThread(dnaList.subList(averageCount * it, dnaList.size), cleanCount, jarInRoomFactor)
            } else {
                CleanThread(dnaList.subList(averageCount * it, averageCount * (it + 1)), cleanCount, jarInRoomFactor)
            }
        }
        threadArray.forEach { it.start() }
        threadArray.forEach { it.join() }
    }

    private fun printScoreAverage(dnaList: List<DNA>, generation: Int) {
        val scoreList = dnaList.map { it.score }
        println("$generation generation score: ${round(scoreList.average()).toInt()}, min score: ${scoreList.min()}, max score: ${scoreList.max()}")
    }

    private fun pickParents(list: List<DNA>): List<DNA> {
        val dnaList = processDNAProbability(list)
        val parents = mutableListOf<DNA>()
        for (i in dnaList.indices) {
            val randomValue = Random.nextDouble(1.0)
            var index = 0
            while (index < dnaList.size) {
                if (randomValue <= dnaList[index].increaseProbability) {
                    parents += dnaList[index]
                    break
                }
                index++
            }
        }
        println("score:${dnaList.map { it.score }.average()}, picked score:${parents.map { it.score }.average()}")
        println("dna average:${dnaList.map { it.probability }.average()}, parent average:${parents.map { it.probability }.average()}")
        return parents.sortedBy { it.score }
    }

    private fun processDNAProbability(list: List<DNA>): List<DNA> {
        val minScore = abs(list.map { it.score }.min()!!) + 1
        var total = 0
        list.forEach {
            it.powScore = (it.score + minScore).toDouble().pow(2.0).toInt()
            total += it.powScore
        }
        val dnaList = list.sortedBy { it.powScore }
        dnaList.forEach {
            it.probability = it.powScore.toDouble() / total
        }
        var totalProbability = 0.0
        dnaList.forEach {
            val weight = totalProbability + it.probability
            it.increaseProbability = weight
            totalProbability = weight
        }
        return dnaList
    }

    private fun fuckNeighbor(parents: List<DNA>): List<DNA> {
//        println("Picked parent score: ${round(parents.map { it.score }.average()).toInt()}, min score: ${parents.map { it.score }.min()}, max score: ${parents.map { it.score }.max()}")
        val children = mutableListOf<DNA>()
        var index = 1
        val len = parents[0].genes.size
        while (index < parents.size) {
            val pivot = Random.nextInt(0, len)
            val pre = parents[index - 1]
            val motherHead = pre.genes.subList(0, pivot)
            val motherTail = pre.genes.subList(pivot, len)
            val fatherHead = parents[index].genes.subList(0, pivot)
            val fatherTail = parents[index].genes.subList(pivot, len)
            children += DNA((motherHead + fatherTail).toMutableList())
            children += DNA((fatherHead + motherTail).toMutableList())
            index += 2
        }
        if (children.size != parents.size) throw IllegalStateException("parents.size != children.size")
        return children
    }

    private fun mutation(dnaList: List<DNA>) {
        val x = dnaList.size
        val y = dnaList[0].genes.size
        val geneTotal = x * y
        val mutationCount = round(geneTotal * mutationFactor).toInt()
        val indexList = mutableListOf<Int>()
        for (i in 0 until mutationCount) {
            indexList += Random.nextInt(0, mutationCount)
        }
        for (i in indexList) {
            val line = i / y
            dnaList[line].genes[i - line * y] = Random.nextInt(0, 7)
        }
    }
}