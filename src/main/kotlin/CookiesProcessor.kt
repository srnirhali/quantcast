import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CookiesProcessor {

    fun findMostActiveCookies(fileName: String, targetDate: LocalDate): List<String> {
        val cookiesMap = mutableMapOf<String, Int>()
        var skipHeader = true
        File(fileName).useLines { lines ->
            lines.forEach { line ->
                if (skipHeader) {
                    skipHeader = false
                    return@forEach
                }
                val (cookie, timestampStr) = line.split(",")
                val timestamp = LocalDateTime.parse(timestampStr,DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                val date = timestamp.toLocalDate()
                if (date == targetDate) {
                    cookiesMap[cookie] = cookiesMap.getOrDefault(cookie, 0) + 1
                } else if (date.isBefore(targetDate)) {
                    return@forEach
                }
            }
        }

        val maxOccurrences = cookiesMap.values.maxOrNull()
        return cookiesMap.filterValues { it == maxOccurrences }.keys.toList()
    }
}

