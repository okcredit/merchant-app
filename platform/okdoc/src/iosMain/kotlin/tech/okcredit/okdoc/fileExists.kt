package tech.okcredit.okdoc

import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath

actual fun fileExists(filePath: String): Boolean {
    return FileSystem.SYSTEM.exists(filePath.toPath())
}

actual fun fileBytes(path: Path): ByteArray {
    return FileSystem.SYSTEM.read(path) {
        this.readByteArray()
    }
}
