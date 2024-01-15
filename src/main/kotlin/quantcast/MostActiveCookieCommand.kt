package quantcast

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.CliktError

import com.github.ajalt.clikt.parameters.options.check
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import java.io.FileNotFoundException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class MostActiveCookieCommand : CliktCommand() {
    private val fileName by option("-f", "--file", help = "CSV file name").required()
    private val dateStr by option("-d", "--date", help = "Date in the format YYYY-MM-DD").required()
        .check("Date in the format YYYY-MM-DD") {
            try {
                LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE)
                true
            } catch (e: DateTimeParseException) {
                false
            }
        }
    private val cookiesProcessor = CookiesProcessor()

    override fun run() {
        try {
            val date = LocalDate.parse(dateStr)
            val mostActiveCookies = cookiesProcessor.findMostActiveCookies(fileName, date)
            mostActiveCookies.forEach {
                echo(it)
            }
        }catch (e: Exception){
            handleException(e)
        }

    }
    private fun handleException(e: Exception) {
        val message = when (e) {
            is FileNotFoundException -> {
                "Given file location not found : ${e.localizedMessage}"
            }

            is DateTimeParseException -> {
                "Given file timestamp value error : ${e.localizedMessage}"
            }

            else -> {
                "An unexpected error occurred: ${e.localizedMessage}"
            }

        }
        throw CliktError(message,e)
    }
}
