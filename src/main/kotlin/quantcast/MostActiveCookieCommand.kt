package quantcast

import com.github.ajalt.clikt.core.CliktCommand

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

    override fun run() {
        try {
            val date = LocalDate.parse(dateStr)
            val mostActiveCookies = CookiesProcessor().findMostActiveCookies(fileName, date)
            mostActiveCookies.forEach {
                echo(it)
            }
        }catch (e: Exception){
            handleException(e)
        }

    }
    private fun handleException(e: Exception) {
        when (e) {
            is FileNotFoundException -> {
                echo(e.localizedMessage)
            }
            else -> {
                echo("An unexpected error occurred: ${e.localizedMessage}")
            }

        }
    }
}
