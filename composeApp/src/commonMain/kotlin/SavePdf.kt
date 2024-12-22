import io.github.vinceglb.filekit.core.FileKit

suspend fun savePdf(pdfBytes: ByteArray) {
    FileKit.saveFile(
        bytes = pdfBytes,
        extension = "pdf",
    )
}
