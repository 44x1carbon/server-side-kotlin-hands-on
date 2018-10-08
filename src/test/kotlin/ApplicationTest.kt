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

        val request2 = handleRequest(HttpMethod.Get, "/tasks?done=true")

        assertEquals(HttpStatusCode.OK, request2.response.status())
        assertEquals(gsonBuilder.toJson(listOf(
                Task(3, "タスク3", true)
        )), request2.response.content)

        val request3 = handleRequest(HttpMethod.Get, "/tasks?done=false")

        assertEquals(HttpStatusCode.OK, request3.response.status())
        assertEquals(gsonBuilder.toJson(listOf(
                Task(1, "タスク1", false),
                Task(2, "タスク2", false)
        )), request3.response.content)
    }

    @Test fun `タスクの作成`() = withTestApplication(Application::main) {

        val request1 = handleRequest(HttpMethod.Post, "/tasks") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("""
                {
                    "name": "タスク4"
                }
            """.trimIndent())
        }

        assertEquals(HttpStatusCode.OK, request1.response.status())
        assertEquals(4.toString(), request1.response.content)
    }

    @Test fun `タスクを完了に`() = withTestApplication(Application::main) {

        val request1 = handleRequest(HttpMethod.Put, "/tasks/1/done") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("""
                {
                    "done": true
                }
            """.trimIndent())
        }

        assertEquals(HttpStatusCode.OK, request1.response.status())
    }

}