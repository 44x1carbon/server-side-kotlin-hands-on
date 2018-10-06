import com.example.*
import com.google.gson.GsonBuilder
import io.ktor.application.Application
import io.ktor.http.*
import io.ktor.server.testing.*
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ApplicationTest {
    @Test fun `タスク一覧のテスト`() = withTestApplication(Application::main) {
        val gsonBuilder = GsonBuilder().create()

        val request1 = handleRequest(HttpMethod.Get, "/tasks")

        assertEquals(HttpStatusCode.OK, request1.response.status())
        assertEquals(gsonBuilder.toJson(listOf(
                Task(1, "タスク1", false),
                Task(2, "タスク2", false),
                Task(3, "タスク3", true)
        )), request1.response.content)
    }
}