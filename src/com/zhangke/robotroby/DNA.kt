package com.zhangke.robotroby

import kotlin.random.Random

data class DNA(
    val genes: MutableList<Int>,
    var score: Int = 0,
    var powScore: Int = 0,
    var probability: Double = 0.0,
    var increaseProbability: Double = 0.0
) {

    companion object {

        fun generate(size: Int): DNA {
            val item = mutableListOf<Int>()
            for (i in 0 until size) {
                item += Random.nextInt(0, 6)
            }
            return DNA(item)
        }
    }
}