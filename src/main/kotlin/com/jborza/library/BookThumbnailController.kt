package com.jborza.library

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class BookThumbnailController(
    private val bookService: BookService,
    private val thumbnailService: ThumbnailService,
    private val bookRepository: BookRepository
) {
    @GetMapping("/books/collect-thumbnails")
    fun collectThumbnails(redirectAttributes: RedirectAttributes): String {
        val booksWithWebLinks = bookRepository.findAllByWebLinkIsNotNull()

        for (book in booksWithWebLinks) {
            val imageUrl = thumbnailService.downloadThumbnail(book.title, book.author, book.webLink!!)
            if (imageUrl != null) {
                book.imageUrl = imageUrl
                bookRepository.save(book)
            }
        }

        redirectAttributes.addFlashAttribute("successMessage", "Thumbnails collected successfully!")
        return "redirect:/books"
    }
}