package org.me.crud

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.me.crud.payloads.request.TaskDTORequest
import org.me.crud.payloads.response.TaskDTOResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Profile
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*

@SpringBootTest(
	classes = [CrudApplication::class],
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Profile("test")
class CrudApplicationTests {
	@Autowired
	private lateinit var restTemplate: TestRestTemplate

	@Test
	@DisplayName("Teste consumindo a API desenvolvida - POST")
	fun testSaveTask() {
		val request = TaskDTORequest(
			name = "Eduardo",
			description = "Method POST",
			done = false
		)

		val headers = HttpHeaders()
		headers.contentType = MediaType.APPLICATION_JSON

		val entity = HttpEntity(request, headers)

		val response: ResponseEntity<TaskDTOResponse> = restTemplate.exchange(
			"/tasks/create",
			HttpMethod.POST,
			entity,
			TaskDTOResponse::class.java
		)

		assertTrue(response.statusCode.is2xxSuccessful)

		val responseBody = response.body
		if (responseBody != null) {
			assertNotNull(responseBody)
			assertEquals("Eduardo", responseBody.name)
			assertEquals("Method POST", responseBody.description)
			assertFalse(responseBody.done)
		}

		val locationHeader = response.headers.getFirst(HttpHeaders.LOCATION)
		assertNotNull(locationHeader)
		assertTrue(locationHeader!!.startsWith("http://localhost:"))
	}

	@Test
	@DisplayName("Teste consumindo a API desenvolvida - GET")
	fun testGetAllTasks() {
		val response: ResponseEntity<List<TaskDTOResponse>> = restTemplate.exchange(
			"/tasks",
			HttpMethod.GET,
			HttpEntity.EMPTY,
			object: ParameterizedTypeReference<List<TaskDTOResponse>>() {}
		)

		assertEquals(HttpStatus.OK, response.statusCode)
		assertNotNull(response.body)
		assertTrue(response.body!!.isNotEmpty())
	}
}
