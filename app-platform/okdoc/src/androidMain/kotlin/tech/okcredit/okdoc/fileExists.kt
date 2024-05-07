package tech.okcredit.okdoc

import okio.FileSystem
import okio.Path
import java.io.File

actual fun fileExists(filePath: String): Boolean {
    val file = File(filePath)
    return file.exists()
}

actual fun fileBytes(path: Path): ByteArray {
    return FileSystem.SYSTEM.read(path) {
        this.readByteArray()
    }
}
