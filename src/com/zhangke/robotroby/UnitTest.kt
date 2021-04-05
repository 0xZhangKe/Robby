package com.zhangke.robotroby

import kotlin.random.Random

fun main() {
    val test = UnitTest()
    test.test()
}

class UnitTest {

    fun test() {
        caseRate1()
        caseRate2()
//        val str =
//            "15245502125644406335514136245335120045145512315160416232426600" +
//                    "4040256060052004316401456203443334225141156451050235106256354245" +
//                    "06314301134042262604435644440030055563132521515543614434516" +
//                    "4455440161256251412661563442063025020602255536510141514365"
//        val items = mutableListOf<Int>()
//        str.forEach { items += Character.getNumericValue(it) }
//        val list = mutableListOf<List<Int>>()
//        for(i in 0 until 10){
//            list += items
//        }
//        println(computeIndex(0, list.size, items.size))
//        println(computeIndex(1, list.size, items.size))
//        println(computeIndex(241, list.size, items.size))
//        println(computeIndex(242, list.size, items.size))
//        println(computeIndex(243, list.size, items.size))
//        println(computeIndex(300, list.size, items.size))
    }

    private fun computeIndex(target: Int, x: Int, y: Int):Pair<Int, Int>{
        val line = target / y
        return Pair(line, target - line * y)
    }

    fun getRuleCase() {
        val str =
            "15245502125644406335514136245335120045145512315160416232426600" +
                    "4040256060052004316401456203443334225141156451050235106256354245" +
                    "06314301134042262604435644440030055563132521515543614434516" +
                    "4455440161256251412661563442063025020602255536510141514365"
        val items = mutableListOf<Int>()
        str.forEach { items += Character.getNumericValue(it) }
        val room = mutableListOf<MutableList<Boolean>>()
        room += mutableListOf(false, false, true, false, true, false, true, false, false, true)
        room += mutableListOf(false, false, false, false, true, false, true, false, false, true)
        room += mutableListOf(false, false, false, false, true, false, true, false, false, true)
        room += mutableListOf(false, false, false, false, true, false, true, false, false, true)
        room += mutableListOf(false, false, false, false, true, false, true, false, false, true)
        room += mutableListOf(false, true, false, false, true, false, true, false, false, true)
        room += mutableListOf(false, false, false, false, true, false, true, false, false, true)
        room += mutableListOf(false, false, false, false, true, false, true, false, false, true)
        room += mutableListOf(false, false, false, false, true, false, true, false, false, true)
        room += mutableListOf(false, false, false, false, true, false, true, false, false, true)

        val instance = CleanThread(listOf(DNA(items)), 1, 0.5F)
        println(instance.getRule(0, 0, items, room) == items["20200".toInt(3)])
        println(instance.getRule(0, 2, items, room) == items["20001".toInt(3)])
        println(instance.getRule(0, 1, items, room) == items["20010".toInt(3)])
        println(instance.getRule(4, 1, items, room) == items["01000".toInt(3)])
    }

    fun caseRate1() {
        val str = "4503512534502562563543562651523542521544544543513514640513534500553566443356" +
                "03132054351253256352104350004552116414256156116103346453105056355052053324226604" +
                "006321154130114255453253352312102112111412414140255154432364654062360530606611264324300"
        val items = mutableListOf<Int>()
        str.forEach { items += Character.getNumericValue(it) }
        val ndaList = listOf(DNA(items))
        Roby().computeScore(ndaList)
        println(ndaList.first().score)
    }

    fun caseRate2() {
        val str =
            "15245502125644406335514136245335120045145512315160416232426600" +
                    "4040256060052004316401456203443334225141156451050235106256354245" +
                    "06314301134042262604435644440030055563132521515543614434516" +
                    "4455440161256251412661563442063025020602255536510141514365"
        val items = mutableListOf<Int>()
        str.forEach { items += Character.getNumericValue(it) }
        val ndaList = listOf(DNA(items))
        Roby().computeScore(ndaList)
        println(ndaList.first().score)
    }
}