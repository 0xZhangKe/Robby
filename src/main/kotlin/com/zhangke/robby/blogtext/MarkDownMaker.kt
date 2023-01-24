package com.zhangke.robby.blogtext

import java.io.File

fun main() {
    val file = File("/Users/zhangke/Desktop/z.md")
    val newContent = MarkDownMaker().make(file.readText())
    println(newContent)
    file.writeText(newContent)
}

class MarkDownMaker {

    private val titleRegex = "([0-9]+(\\.)?)+".toRegex()

    fun make(text: String): String {
        val builder = StringBuilder()
        text.reader()
            .readLines()
            .forEach {
                builder.append(it.makeNewTitle().removeParagraphHead())
                builder.appendLine()
            }
        return builder.toString()
    }

    // Remove >> symbol from WeChat Reader
    private fun String.removeParagraphHead(): String {
        val paragraph = this
        if (paragraph.isEmpty() || paragraph.isBlank()) return paragraph
        return if (paragraph.startsWith(">>")) paragraph.removePrefix(">>") else paragraph
    }

    private fun String.makeNewTitle(): String {
        val title = this
        if (!title.startsWith('◆')) return title
        var fixedTitle = title.removePrefix("◆").trim()
        val chapterNumber = titleRegex.find(title)?.value
        if (!chapterNumber.isNullOrEmpty() && !chapterNumber.endsWith('.')) {
            val chapterLevel = chapterNumber.count { it == '.' } + 1
            val header = with(StringBuilder()) {
                repeat(chapterLevel) { append('#') }
                append(' ')
            }.toString()
            fixedTitle = header + fixedTitle
        }
        return fixedTitle
    }
}