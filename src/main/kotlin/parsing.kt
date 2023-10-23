import java.io.File
import java.nio.file.Files

data class Entry(
    val name: String,
    val data: HashMap<String, String>
)

fun parseDataFile(file: File) {
    val entries = ArrayList<Entry>()

    Files.readAllLines(file.toPath()).forEach { line ->
        if (line.startsWith("new entry")) {
            entries.add(Entry(
                name = line
                    .replace("new entry\"", "")
                    .replace("\"", ""),
                data = HashMap()
            ))
        }
        println(line)
    }

//    println(entries)
}