package com.jborza.library

import org.jsoup.Jsoup
import org.springframework.stereotype.Service
import java.io.File
import java.net.URL

@Service
class ThumbnailService {

    private val imageDir = "src/main/resources/static/images/"

    fun downloadThumbnail(title: String, author: String, webLink: String): String? {
        return try {
            // Fetch the webpage
            val document = Jsoup.connect(webLink).get()

            // Find the image inside <div class="BookCover">
            val bookCoverDiv = document.selectFirst("div.BookCover")
            val imgTag = bookCoverDiv?.selectFirst("img")
            val imgUrl = imgTag?.attr("src")

            if (imgUrl == null) {
                println("No image found for $title by $author")
                return null
            }

            // Generate a unique filename
            val filename = "${title.replace(" ", "_").replace("/", "_")}-${author.replace(" ", "_").replace("/", "_")}.jpg"
            val filePath = "$imageDir$filename"

            // Download and save the image
            val url = URL(imgUrl)
            File(imageDir).mkdirs() // Ensure the directory exists
            url.openStream().use { input ->
                File(filePath).outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            println("Downloaded thumbnail for $title by $author")
            return "/images/$filename"

        } catch (e: Exception) {
            println("Error processing $title by $author: ${e.message}")
            null
        }
    }
}