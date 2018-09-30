import com.example.*
import com.google.gson.GsonBuilder
import io.ktor.application.Application
import io.ktor.http.*
import io.ktor.server.testing.*
import io.netty.handler.codec.http.HttpHeaders.addHeader
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ApplicationTest {
    @Test fun `タスク一覧のテスト`() = withTestApplication(Application::main) {
        val gsonBuilder = GsonBuilder().create()

        with(handleRequest(HttpMethod.Get, "/tasks")) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(gsonBuilder.toJson(listOf(
                    Task(1, "タスク1", false),
                    Task(2, "タスク2", false),
                    Task(3, "タスク2", true)
            )), response.content)
        }

        with(handleRequest(HttpMethod.Get, "/tasks?done=true")) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(gsonBuilder.toJson(listOf(
                    Task(3, "タスク2", true)
            )), response.content)
        }

        with(handleRequest(HttpMethod.Get, "/tasks?done=false")) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(gsonBuilder.toJson(listOf(
                    Task(1, "タスク1", false),
                    Task(2, "タスク2", false)
            )), response.content)
        }
    }

    @Test fun `タスクの作成`() = withTestApplication(Application::main) {

        with(handleRequest(HttpMethod.Post, "/tasks") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("""
                {
                    "name": "タスク4"
                }
            """.trimIndent())8
        }) {
            assertEquals(HttpStatusCode.Created, response.status())
            assertEquals(4.toString(), response.content)
        }
    }

    @Test fun `タスクを完了に`() = withTestApplication(Application::main) {
        val gsonBuilder = GsonBuilder().create()

        with(handleRequest(HttpMethod.Put, "/tasks/1/done") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("""
                {
                    "done": true
                }
            """.trimIndent())
        }) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(gsonBuilder.toJson(mapOf("id" to 1.toString())), response.content)
        }
    }

}