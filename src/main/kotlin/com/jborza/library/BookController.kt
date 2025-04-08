package com.jborza.library

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import com.jborza.library.Book

@Controller
@RequestMapping("/books")
class BookController (private val bookService: BookService) {
    @GetMapping
    fun listBooks(model: Model): String {
        model.addAttribute("books", bookService.getAllBooks())
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