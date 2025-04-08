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

    fun getBooks(platform: Platform?, status: String?): List<Book> {
        return when {
            platform == null && status == null -> bookRepository.findAll()
            platform != null && status == null -> bookRepository.findByPlatform(platform)
            platform == null && status != null -> bookRepository.findByStatus(status)
            platform != null && status != null -> bookRepository.findByPlatformAndStatus(platform, status)
            else -> emptyList() // This case is logically unreachable
        }
    }
}