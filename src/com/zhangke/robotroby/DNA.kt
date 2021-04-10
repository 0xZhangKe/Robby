package com.zhangke.robotroby

import kotlin.random.Random

data class DNA(
    val genes: MutableList<Int>,
    var score: Int = 0,
    var powScore: Int = 0,
    var probability: Double = 0.0
) {

    init {
        if (genes.size != LENGTH) throw IllegalArgumentException("Genes length(${genes.size}) not illegal!")
    }

    companion object {

        const val LENGTH = 243

        fun randomDNA(): DNA {
            val item = mutableListOf<Int>()
            for (i in 0 until LENGTH) {
                item += randomGene()
            }
            return DNA(item)
        }

        fun randomGene(): Int {
            return Random.nextInt(0, 6)
        }
    }
}