import java.io.File
import java.nio.file.Files

data class Entry(
    val name: String,
    val type: String,
    val data: HashMap<String, String>
)

fun parseDataFile(file: File) {
    val entries = ArrayList<Entry>()
    var isReadingBlock = false

    Files.readAllLines(file.toPath()).forEach { line ->
        if (line.startsWith("new ")) {

            val splitLine = line
                .replace("new ", "")
                .split(" ")

            entries.add(Entry(
                name = splitLine[1]
                        .replace("\"", ""),
                type = splitLine[0],
                data = HashMap()
            ))

            isReadingBlock = true
        }

        if (line == "") {
            isReadingBlock = false
        } else if (isReadingBlock) {
            if (line.startsWith("data ")) {
                val split = line
                    .replace("data \"", "")
                    .split("\" \"")
                entries.last().data[split[0]] = split[1].replace("\"", "")
            }
        }
//        println(line)
    }

    println(entries)
}