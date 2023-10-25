import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.nio.file.Files

data class Entry(
    val name: String,
    val type: String,
    val data: HashMap<String, String>
)

object Entries : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val filePath: Column<String> = varchar("file_path", 1500)
    val name: Column<String> = varchar("name", 150)
    val type: Column<String> = varchar("type", 150)

    override val primaryKey = PrimaryKey(id, name = "PK_Entries_ID")
}

object EntriesData : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val entry: Column<Int> = integer("entry_id")
    val key: Column<String> = varchar("key", 150)
    val value: Column<String> = text("value")

    override val primaryKey = PrimaryKey(id, name = "PK_Entries_Data_ID")
}

fun parseDataFile(basePath: String, file: File) {
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

        println(line)
    }

    Database.connect("jdbc:sqlite:./foo.db", driver = "org.sqlite.JDBC", user = "", password = "")

    transaction {
        SchemaUtils.create(Entries, EntriesData)

        entries.forEach {e ->
            val entryObj = Entries.insert {
                it[filePath] = file.path.replace(basePath, "")
                it[name] = e.name
                it[type] = e.type
            }

            e.data.entries.forEach { data ->
                EntriesData.insert {
                    it[entry] = entryObj[Entries.id]
                    it[key] = data.key
                    it[value] = data.value
                }
            }
        }
    }
}