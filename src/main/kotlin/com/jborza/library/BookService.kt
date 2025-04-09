package com.jborza.library

import org.springframework.stereotype.Service

@Service
class BookService(private val bookRepository: BookRepository) {

    fun getAllBooks(): List<Book> = bookRepository.findAll()

    fun getBookById(id: Long): Book? = bookRepository.findById(id).orElse(null)

    fun saveBook(book: Book): Book = bookRepository.save(book)

    fun deleteBook(id: Long) = bookRepository.deleteById(id)

    fun saveBooks(books: List<Book>) {
        bookRepository.saveAll(books)
    }

    fun getBooks(platform: Platform?, status: String?, author: String?): List<Book> {
        return when {
            platform == null && status == null && author == null -> bookRepository.findAll()
            platform != null && status == null && author == null -> bookRepository.findByPlatform(platform)
            platform == null && status != null && author == null -> bookRepository.findByStatus(status)
            platform != null && status != null && author == null -> bookRepository.findByPlatformAndStatus(platform, status)
            platform == null && status == null && author != null -> bookRepository.findByAuthor(author)
            platform != null && status == null && author != null -> bookRepository.findByPlatformAndAuthor(platform, author)
            platform == null && status != null && author != null -> bookRepository.findByStatusAndAuthor(status, author)
            platform != null && status != null && author != null -> bookRepository.findByPlatformAndStatusAndAuthor(platform, status, author)
            else -> emptyList() // This case is logically unreachable
        }
    }

    fun importBooksFromNotes(notes: String) {
        val parser = BookParser()
        val parsedBooks = parser.parseNotes(notes)

        for (parsedBook in parsedBooks) {
            val book = Book(
                title = parsedBook.title,
                author = parsedBook.author,
                platform = Platform.PHYSICAL, // Default platform
                status = "Wishlist",         // Default status
                webLink = ""               // Optional field
            )
            // Avoid duplicates by checking if a book with the same title and author exists
            if (!bookRepository.existsByTitleAndAuthor(book.title, book.author)) {
                bookRepository.save(book)
            }
        }
    }
}