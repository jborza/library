package com.jborza.library

import org.springframework.data.jpa.repository.JpaRepository

interface BookRepository : JpaRepository<Book, Long> {
    fun findByPlatform(platform: Platform): List<Book>
    fun findByStatus(status: String): List<Book>
    fun findByPlatformAndStatus(platform: Platform, status: String): List<Book>
    fun existsByTitleAndAuthor(title: String, author:String) : Boolean
    fun findAllByWebLinkIsNotNull(): List<Book>
    fun findAllByWebLinkIsNotNullAndImageUrlIsNull(): List<Book>
}