package com.jborza.library

import jakarta.persistence.*

@Entity
data class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val title: String = "",
    val author: String = "",
    @Enumerated(EnumType.STRING)
    val platform: Platform = Platform.PHYSICAL,
    val webLink: String = "",
    val status: String = "",
    var imageUrl: String? = null // Add the URL for the downloaded image
)