package com.jborza.library

data class ParsedBook(val title: String, val author: String)
class BookParser {
    fun parseNotes(notes: String): List<ParsedBook> {
        val books = mutableListOf<ParsedBook>()
        val lines = notes.lines()

        for (line in lines) {
            // Skip empty lines or lines that don't start with '-'
            if (line.isBlank() || !line.trim().startsWith("-")) continue

            val content = line.trim().substring(1).trim() // Remove the leading '-'
            val parsedBook = when {
                content.contains("(") && content.contains(")") -> {
                    // Format: Title (Author)
                    val title = content.substringBeforeLast("(").trim()
                    val author = content.substringAfterLast("(").removeSuffix(")").trim()
                    ParsedBook(title, author)
                }
                content.contains(" - ") -> {
                    // Format: Title - Author
                    val title = content.substringBeforeLast(" - ").trim()
                    val author = content.substringAfterLast(" - ").trim()
                    ParsedBook(title, author)
                }
                else -> {
                    // Skip invalid lines
                    null
                }
            }

            if (parsedBook != null) {
                books.add(parsedBook)
            }
        }

        return books
    }
}