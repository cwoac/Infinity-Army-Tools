package net.codersoffortune.infinity

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.get
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Paths


data class Decal(val url : String) {
    private val sanitisedUrl : String = url.filter {it.isLetterOrDigit()}
    private val client = HttpClient(CIO)
    private val filePath = Paths.get("cache", String.format("%s.jpg", sanitisedUrl))

    init {
        var cachePath = File("cache")
        if (!cachePath.exists())
            cachePath.mkdir()
    }

    private fun loadImage() {

        var fh = filePath.toFile()
        var response: HttpResponse
        runBlocking {
            response = client.get(url)
            // TODO:: check rc
            var outputStream : OutputStream = fh.outputStream()
            response.content.copyTo(outputStream)
            outputStream.flush()
            outputStream.close()
        }
    }

    fun getImageStream() : InputStream {
        var fh = filePath.toFile()
        if (!fh.exists())
                loadImage()
        return fh.inputStream()
    }

}