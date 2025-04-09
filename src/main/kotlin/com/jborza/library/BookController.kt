package com.jborza.library

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/books")
class BookController (private val bookService: BookService) {


    //@GetMapping("/books")
    @GetMapping
    fun listBooks(
        @RequestParam("platform", required = false) platform: String?,
        @RequestParam("status", required = false) status: String?,
        model: Model
    ): String {
        println("Received GET request for /books with platform=$platform and status=$status")
        // Convert empty strings to null
        val normalizedStatus = status?.takeIf { it.isNotBlank() }
        val enumPlatform = platform?.takeIf { it.isNotBlank() }?.let { Platform.valueOf(it) }

        val books = bookService.getBooks(enumPlatform, normalizedStatus)
        model.addAttribute("books", books)
        model.addAttribute("platform", enumPlatform)
        model.addAttribute("status", status)
        return "bookList"
    }

    @GetMapping("/create")
    fun createBookForm(model: Model): String {
        model.addAttribute("book", Book())
        return "editBook"
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
        if (bookService.getBookById(id) != null) {
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