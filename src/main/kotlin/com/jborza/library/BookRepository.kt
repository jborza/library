package com.jborza.library

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BookRepository : JpaRepository<Book, Long> {
    fun findByPlatform(platform: Platform): List<Book>
    fun findByStatus(status: String): List<Book>
    fun findByPlatformAndStatus(platform: Platform, status: String): List<Book>
    fun findByAuthor(author: String): List<Book>
    fun findByPlatformAndAuthor(platform: Platform, author: String): List<Book>
    fun findByStatusAndAuthor(status: String, author: String): List<Book>
    fun findByPlatformAndStatusAndAuthor(platform: Platform, status: String, author: String): List<Book>

    fun existsByTitleAndAuthor(title: String, author:String) : Boolean
    fun findAllByWebLinkIsNotNull(): List<Book>
    fun findAllByWebLinkIsNotNullAndImageUrlIsNull(): List<Book>

    @Query("SELECT DISTINCT b.author FROM Book b ORDER BY b.author ASC")
    fun findAllAuthors(): List<String>
}