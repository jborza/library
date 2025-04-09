package com.jborza.library

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/books")
class BookController (private val bookService: BookService,
    private val thumbnailService: ThumbnailService,
    private val bookRepository: BookRepository
) {
    @GetMapping
    fun listBooks(
        @RequestParam("platform", required = false) platform: String?,
        @RequestParam("status", required = false) status: String?,
        @RequestParam("author", required = false) author: String?,
        @RequestParam("title", required = false) title: String?,
        model: Model
    ): String {
        println("Received GET request for /books with platform=$platform and status=$status")
        // Convert empty strings to null
        val normalizedStatus = status?.takeIf { it.isNotBlank() }
        val normalizedAuthor = author?.takeIf { it.isNotBlank() }
        val enumPlatform = platform?.takeIf { it.isNotBlank() }?.let { Platform.valueOf(it) }

        val books = bookService.getBooks(enumPlatform, normalizedStatus, normalizedAuthor)

        // do title filtering on the server, not in the database
        val filteredBooks = filterByTitle(books, title)

        // Fetch unique authors for the dropdown
        val authors = filteredBooks.map { it.author }.toSet().sorted()

        model.addAttribute("books", filteredBooks)
        model.addAttribute("platform", enumPlatform)
        model.addAttribute("status", status)
        model.addAttribute("authors", authors) // Pass the list of unique authors
        model.addAttribute("selectedAuthor", normalizedAuthor) // Keep track of the selected author

        return "bookList"
    }

    fun filterByTitle(books : List<Book>, title: String?): List<Book> {
        if(title == null)
            return books
        return books.filter { book -> book.title.contains(title, ignoreCase = true) }
    }

    @GetMapping("/create")
    fun createBookForm(model: Model): String {
        model.addAttribute("book", Book())
        return "createBook"
    }

    @PostMapping
    fun saveBook(@ModelAttribute book: Book): String {
        bookService.saveBook(book)
        return "redirect:/books"
    }

    @GetMapping("/edit/{id}")
    fun editBookForm(@PathVariable id: Long, model: Model): String {
        val book = bookService.getBookById(id)
        model.addAttribute("book", book)
        return "editBook"
    }

    @PostMapping("/update/{id}")
    fun updateBook(@PathVariable id: Long, @ModelAttribute book: Book): String {
        val existingBook = bookService.getBookById(id)
        if (existingBook != null) {
            bookService.saveBook(book.copy(id = id))
        }
        return "redirect:/books"
    }

    @DeleteMapping("/delete/{id}")
    fun deleteBook(@PathVariable id: Long): String {
        bookService.deleteBook(id)
        return "redirect:/books"
    }

}