package quantcast

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import com.github.ajalt.clikt.testing.test
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertContains

class MostActiveCookieCommandTest {

        @Test
        fun `test MostActiveCookieCommand`() {
            // Create a temporary file with test data
            val tempFile: Path = Files.createTempFile("cookie_log", ".csv")
            Files.write(
                tempFile,
                """
            cookie,timestamp
            cookie1,2022-01-10T12:00:00+00:00
            cookie2,2022-01-10T12:30:00+00:00
            cookie1,2022-01-10T13:00:00+00:00
            cookie3,2022-01-10T13:30:00+00:00
            cookie1,2022-01-10T14:00:00+00:00
            """.trimIndent().lines()
            )

            val command = MostActiveCookieCommand()
            val result = command.test( "-f", tempFile.toAbsolutePath().toString(), "-d", "2022-01-10")
            assertEquals(0, result.statusCode)
            assertEquals("cookie1\n",result.output)
            Files.delete(tempFile)
        }

    @Test
    fun `test MostActiveCookieCommand invalid date value`() {
        val IN_VALID_DATE = "2022.01.10"
        val command = MostActiveCookieCommand()
        val result = command.test( "-f", "filename", "-d", IN_VALID_DATE)
        assertEquals(1, result.statusCode)
        assertContains(result.output,"invalid value for --date")
    }

    @Test
    fun `test MostActiveCookieCommand -d value missing case`() {
        val command = MostActiveCookieCommand()
        val result = command.test("-f", "filename")
        assertEquals(1, result.statusCode)
        assertContains(result.stderr,"missing option --date",)

    }

    @Test
    fun `test MostActiveCookieCommand -f value missing case`() {
        val command = MostActiveCookieCommand()
        val result = command.test("-d", "2022-01-10")
        assertEquals(1, result.statusCode)
        assertContains(result.stderr,"missing option --file")
    }


    @Test
    fun `test MostActiveCookieCommand -f source not found`() {

        val NOT_VALID_FILENAME = "filename"
        val command = MostActiveCookieCommand()
        val result = command.test( "-f", NOT_VALID_FILENAME, "-d", "2022-01-10")
        assertContains(result.output,"$NOT_VALID_FILENAME (No such file or directory)")
    }


}
