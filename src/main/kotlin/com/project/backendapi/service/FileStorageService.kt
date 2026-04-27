package com.project.backendapi.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*

@Service
class FileStorageService(
    @Value("\${file.upload-dir:uploads}")
    private val uploadDir: String
) {
    private val uploadPath: Path = Paths.get(uploadDir).toAbsolutePath().normalize()

    init {
        try {
            Files.createDirectories(uploadPath)
        } catch (e: Exception) {
            throw RuntimeException("Could not create upload directory", e)
        }
    }

    fun storeFile(file: MultipartFile): String {
        val originalFilename = file.originalFilename ?: throw IllegalArgumentException("File must have a name")
        
        val contentType = file.contentType
        if (contentType == null || !contentType.startsWith("image/")) {
            throw IllegalArgumentException("Only image files are allowed")
        }

        val fileExtension = originalFilename.substringAfterLast(".", "")
        val uniqueFilename = "${UUID.randomUUID()}.${fileExtension}"

        try {
            val targetLocation = uploadPath.resolve(uniqueFilename)
            Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
            return uniqueFilename
        } catch (e: IOException) {
            throw RuntimeException("Could not store file $originalFilename", e)
        }
    }

    fun deleteFile(filename: String): Boolean {
        return try {
            val filePath = uploadPath.resolve(filename).normalize()
            Files.deleteIfExists(filePath)
        } catch (e: IOException) {
            false
        }
    }

    fun getFilePath(filename: String): Path {
        return uploadPath.resolve(filename).normalize()
    }
}
