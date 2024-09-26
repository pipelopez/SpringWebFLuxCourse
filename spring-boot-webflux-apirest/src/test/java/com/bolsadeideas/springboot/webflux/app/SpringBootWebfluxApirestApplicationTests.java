package com.bolsadeideas.springboot.webflux.app;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringBootWebfluxApirestApplicationTests {
	
	@Autowired
	private WebTestClient client;
	
	@Test
	public void listarTest() {
		client.get()
		.uri("/api/v2/productos")
		.accept(APPLICATION_JSON_UTF8)
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(APPLICATION_JSON_UTF8)
		.expectBodyList(Producto.class)
		.hasSize(10);
	}

}
