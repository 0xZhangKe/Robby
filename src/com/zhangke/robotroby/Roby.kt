package com.zhangke.robotroby

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.round
import kotlin.random.Random


fun main() {
    Roby().startEvolution()
}

class Roby {

    private val dnaCount = 200
    private val generationCount = 2000
    private val cleanCount = 100
    private val mutationFactor = 0.078
    private val mutationMaxCount = 10
    private val jarInRoomFactor = 0.5F
    private val crossProbability = 0.85

    private var max: DNA? = null

    fun startEvolution() {
        val firstDNAList = mutableListOf<DNA>()
        for (i in 0 until dnaCount) {
            firstDNAList += DNA.randomDNA()
        }
        var cur = firstDNAList.toList()
        for (i in 0 until generationCount) {
            cur = evolution(cur, i)
        }
        max?.also {
            println("max score is ${it.score}")
            println("${it.genes}")
        }
    }

    private fun evolution(geneList: List<DNA>, count: Int): List<DNA> {
        computeScore(geneList)
        printScore(geneList, count)
        geneList.maxBy { it.score }
            ?.also { if (it.score > (max?.score ?: 0)) max = it }
        return pickChildren(geneList)
    }

    private fun computeScore(dnaList: List<DNA>) {
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

    private fun printScore(dnaList: List<DNA>, generation: Int) {
        val scoreList = dnaList.map { it.score }
        println("$generation -> average: ${round(scoreList.average()).toInt()}, max: ${scoreList.max()}")
    }

    private fun pickChildren(dnaList: List<DNA>): List<DNA> {
        processDNAProbability(dnaList)
        val children = mutableListOf<DNA>()
        val pivot = randomPivot(dnaList[0].genes.size)
        while (children.size < dnaList.size) {
            val father = pickOneDNA(dnaList)
            val mother = pickOneDNA(dnaList)
            val (child1, child2) = makeChildren(father, mother, pivot)
            mutation(child1)
            mutation(child2)
            children += child1
            children += child2
        }
        return children
    }

    private fun processDNAProbability(list: List<DNA>) {
        // 轮盘赌算法
        val minScore = abs(list.map { it.score }.min()!!) + 1
        var total = 0
        list.forEach {
            it.powScore = (it.score + minScore).toDouble().pow(2.0).toInt()
            total += it.powScore
        }
        var totalProbability = 0.0
        list.forEach {
            val weight = it.powScore.toDouble() / total + totalProbability
            it.probability = weight
            totalProbability = weight
        }
    }

    private fun pickOneDNA(dnaList: List<DNA>): DNA {
        val random = Random.nextDouble()
        val result = dnaList.find { random <= it.probability }
            ?: throw java.lang.IllegalStateException("Something wrong when pick one DNA!")
        return DNA(result.genes.toMutableList())
    }

    private fun randomPivot(len: Int): Int {
        var pivot = 0
        while (pivot == 0 || pivot == len) {
            pivot = Random.nextInt(0, len)
        }
        return pivot
    }

    private fun makeChildren(father: DNA, mother: DNA, pivot: Int): Pair<DNA, DNA> {
        val child1: DNA
        val child2: DNA
        val len = father.genes.size
        val crossRate = Random.nextDouble()
        if (crossRate <= crossProbability) {
            child1 = DNA((father.genes.subList(0, pivot) + mother.genes.subList(pivot, len)).toMutableList())
            child2 = DNA((mother.genes.subList(0, pivot) + father.genes.subList(pivot, len)).toMutableList())
        } else {
            child1 = father
            child2 = mother
        }
        return Pair(child1, child2)
    }

    private fun mutation(dna: DNA) {
        val genSize = dna.genes.size
        val factor = Random.nextDouble()
        if (factor <= mutationFactor) {
            val mutationGenCount = Random.nextInt(1, mutationMaxCount)
            for (i in 0 until mutationGenCount) {
                val index = Random.nextInt(0, genSize)
                var newGene = DNA.randomGene()
                while (newGene == dna.genes[index]) {
                    newGene = DNA.randomGene()
                }
                dna.genes[index] = newGene
            }
        }
    }
}