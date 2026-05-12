package cloud.mallne.geokit.fwi

object CSVUtil {
    fun String.parseCSVWithHeader(delimiter: String = ","): List<Map<String, String>> {
        //split by line
        val r = this.replace("\"", "")
        val lines = r.lines()
        //first line is header -> use it as keys
        val header = lines[0].split(delimiter)
        //remove header from lines
        val data = lines.subList(1, lines.size)
        //create map for each line
        return data.map { line ->
            header.zip(line.split(delimiter)).toMap()
        }
    }
}