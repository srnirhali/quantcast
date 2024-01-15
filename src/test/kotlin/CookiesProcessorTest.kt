import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDate
import org.junit.jupiter.api.Assertions.*
import java.io.FileNotFoundException

class CookiesProcessorTest {

    private val cookiesProcessor:CookiesProcessor = CookiesProcessor()

    @Test
    fun `test findMostActiveCookies single value return`() {
        val tempFile: Path = Files.createTempFile("cookie_log", ".csv")
        Files.write(
            tempFile,
            """
            cookie,timestamp
            cookie1,2022-01-10T12:00:00+00:00
            cookie2,2022-01-10T12:30:02+00:00
            cookie1,2022-01-10T13:00:00+00:00
            cookie3,2022-01-10T13:30:02+00:00
            cookie1,2022-01-10T14:00:00+00:00
            """.trimIndent().lines()
        )

        // Perform the test
        val result = cookiesProcessor.findMostActiveCookies(tempFile.toAbsolutePath().toString(), LocalDate.parse("2022-01-10"))

        // Assert the expected result
        assertEquals(listOf("cookie1"), result)

        // Clean up the temporary file
        Files.delete(tempFile)
    }

    @Test
    fun `test findMostActiveCookies multiple value return`() {
        // Setup temp File
        val tempFile: Path = Files.createTempFile("cookie_log", ".csv")
        Files.write(
            tempFile,
            """
            cookie,timestamp
            cookie1,2022-01-10T12:00:00+00:00
            cookie2,2022-01-10T12:30:00+00:00
            cookie2,2022-01-10T13:00:00+00:00
            cookie3,2022-01-10T13:30:00+00:00
            cookie1,2022-01-10T14:00:00+00:00
            """.trimIndent().lines()
        )

        // Perform Test
        val result = cookiesProcessor.findMostActiveCookies(tempFile.toAbsolutePath().toString(), LocalDate.parse("2022-01-10"))

        // Assert the expected result
        assertEquals(listOf("cookie1","cookie2"), result)

        // Clean up the temporary file
        Files.delete(tempFile)
    }

    @Test
    fun `test findMostActiveCookies throw file not found exception`() {
        // setup
        val fileNotFound: String = "file_not_found"

        // Assert the expected result
        assertThrows(FileNotFoundException::class.java){
            cookiesProcessor.findMostActiveCookies(fileNotFound, LocalDate.parse("2022-01-10"))
        }
    }
}
