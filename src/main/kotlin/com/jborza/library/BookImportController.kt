package com.jborza.library

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedReader
import java.io.InputStreamReader

@Controller
class BookImportController(private val bookService: BookService) {

    @GetMapping("/books/import")
    fun importBooksPage(model: Model): String {
        return "importBooks"
    }

    @PostMapping("/books/import")
    fun uploadCsvFile(@RequestParam("file") file: MultipartFile, model: Model): String {
        if (file.isEmpty) {
            model.addAttribute("message", "Please select a CSV file to upload.")
            return "importBooks"
        }

        val books = mutableListOf<Book>()
        BufferedReader(InputStreamReader(file.inputStream)).use { reader ->
            val header = reader.readLine() // Skip the header line
            reader.forEachLine { line ->
                val values = line.split(",").map { it.trim().replace("\"", "") }
                if (values.size >= 24) {
                    val bookId = values[0]
                    val title = values[1]
                    val author = values[2]
                    val bookshelves = values[19] // "Bookshelves" column
                    val status = mapGoodreadsStatusToApp(bookshelves)
                    val webLink = "https://www.goodreads.com/en/book/show/$bookId"

                    val book = Book(
                        title = title,
                        author = author,
                        platform = Platform.ELECTRONIC, // Default to electronic for now
                        webLink = webLink,
                        status = status
                    )
                    books.add(book)
                }
            }
        }

        // Save all books to the database
        bookService.saveBooks(books)

        model.addAttribute("message", "${books.size} books were successfully imported.")
        return "importBooks"
    }

    private fun mapGoodreadsStatusToApp(bookshelves: String): String {
        return when {
            bookshelves.contains("read", ignoreCase = true) -> "read"
            bookshelves.contains("to-read", ignoreCase = true) -> "to-read"
            bookshelves.contains("currently-reading", ignoreCase = true) -> "currently-reading"
            else -> "unknown"
        }
    }
}