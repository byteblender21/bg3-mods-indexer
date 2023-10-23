import java.io.File
import java.nio.file.Files

fun parseDataFile(file: File) {
    Files.readAllLines(file.toPath()).forEach { line ->
        println(line)
    }
}