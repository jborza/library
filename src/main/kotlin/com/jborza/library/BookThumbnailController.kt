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
        val booksWithWebLinks = bookRepository.findAllByWebLinkIsNotNullAndImageUrlIsNull()
        //get rid of books with filled imageUrl
        //val booksWithoutThumbnails = booksWithWebLinks.filter { it.imageUrl == null }
        for (book in booksWithWebLinks) {
            // only do this if imageUrl is empty
            val imageUrl = thumbnailService.downloadThumbnail(book.title, book.author, book.webLink!!)
            if (imageUrl != null) {
                book.imageUrl = imageUrl
                bookRepository.save(book)
            }
            //pause for several seconds
            Thread.sleep(2000);
        }

        redirectAttributes.addFlashAttribute("successMessage", "Thumbnails collected successfully!")
        return "redirect:/books"
    }
}