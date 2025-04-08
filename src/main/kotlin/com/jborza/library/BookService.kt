package com.jborza.library

import org.springframework.stereotype.Service

@Service
class BookService(private val bookRepository: BookRepository) {

    fun getAllBooks(): List<Book> = bookRepository.findAll()

    fun getBookById(id: Long): Book? = bookRepository.findById(id).orElse(null)

    fun saveBook(book: Book): Book = bookRepository.save(book)

    fun deleteBook(id: Long) = bookRepository.deleteById(id)
}