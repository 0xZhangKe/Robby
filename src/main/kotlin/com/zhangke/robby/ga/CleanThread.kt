package com.zhangke.robby.ga

import kotlin.random.Random

class CleanThread(private val dnaList: List<DNA>,
                  private val cleanCount: Int,
                  private val jarInRoomFactor: Float) : Thread() {

    override fun run() {
        super.run()
        dnaList.forEach { dna ->
            var total = 0
            for (i in 0 until cleanCount) {
                total += rateItem(dna, generateRoom(jarInRoomFactor))
            }
            dna.score = total / cleanCount
        }
    }

    private fun rateItem(dna: DNA, room: List<MutableList<Boolean>>): Int {
        var totalScore = 0
        var x = 5
        var y = 5
        val roomXLen = room.size
        val roomYLen = room[0].size
        val stepMax = 200
        for (i in 0 until stepMax) {
            when (getRule(x, y, dna.genes, room)) {
                0 -> {
                    x--
                    if (x < 0) {
                        x = 0
                        totalScore -= 5
                    }
                }
                1 -> {
                    x++
                    if (x >= roomXLen) {
                        x = roomXLen - 1
                        totalScore -= 5
                    }
                }
                2 -> {
                    y--
                    if (y < 0) {
                        y = 0
                        totalScore -= 5
                    }
                }
                3 -> {
                    y++
                    if (y >= roomYLen) {
                        y = roomYLen - 1
                        totalScore -= 5
                    }
                }
                4 -> {
                    val pair = randomDirection(x, y)
                    x = pair.first
                    y = pair.second
                    var isWall = false
                    if (x < 0) {
                        isWall = true
                        x = 0
                    }
                    if (x >= roomXLen) {
                        isWall = true
                        x = roomXLen - 1
                    }
                    if (y < 0) {
                        isWall = true
                        y = 0
                    }
                    if (y >= roomYLen) {
                        isWall = true
                        y = roomYLen - 1
                    }
                    if (isWall) {
                        totalScore -= 5
                    }
                }
                5 -> {
                    if (room[x][y]) {
                        totalScore += 10
                        room[x][y] = false
                    }else{
                        totalScore --
                    }
                }
            }
        }
        return totalScore
    }

    // public for test
    fun getRule(x: Int, y: Int, genes: List<Int>, room: List<MutableList<Boolean>>): Int {
        val roomSize = room.size
        val top = when {
            x - 1 < 0 -> 2
            room[x - 1][y] -> 1
            else -> 0
        }
        val bottom = when {
            x + 1 >= roomSize -> 2
            room[x + 1][y] -> 1
            else -> 0
        }
        val left = when {
            y - 1 < 0 -> 2
            room[x][y - 1] -> 1
            else -> 0
        }
        val right = when {
            y + 1 >= roomSize -> 2
            room[x][y + 1] -> 1
            else -> 0
        }
        val current = when {
            x < 0 || y < 0 || x >= roomSize || y >= roomSize -> 2
            room[x][y] -> 1
            else -> 0
        }
        val strValue = "$top$bottom$left$right$current"
        strValue.toInt(3)
        return genes[strValue.toInt(3)]
    }

    private fun randomDirection(x: Int, y: Int): Pair<Int, Int> {
        return when (Random.nextInt(0, 4)) {
            0 -> Pair(x - 1, y)
            1 -> Pair(x, y - 1)
            2 -> Pair(x + 1, y)
            else -> Pair(x, y + 1)
        }
    }

    private fun generateRoom(factor: Float): List<MutableList<Boolean>> {
        val roomSize = 10
        val room = mutableListOf<MutableList<Boolean>>()
        val richFactor = factor * 100
        for (i in 0 until roomSize) {
            val item = mutableListOf<Boolean>()
            for (j in 0 until roomSize) {
                item += Random.nextInt(0, 100) < richFactor
            }
            room += item
        }
        return room
    }
}