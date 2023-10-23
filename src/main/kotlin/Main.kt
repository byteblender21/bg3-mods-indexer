import java.io.File

fun reverseFileSearch(path: String) {
    File(path).walkTopDown().forEach { file: File ->
        if (file.isFile.and(file.path.endsWith(".txt"))) {
            parseDataFile(file)
        }
    }
}

fun main(args: Array<String>) {
    reverseFileSearch(args[0])
}