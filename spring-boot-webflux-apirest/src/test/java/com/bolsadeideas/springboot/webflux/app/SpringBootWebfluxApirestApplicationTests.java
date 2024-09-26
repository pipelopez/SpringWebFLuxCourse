package com.bolsadeideas.springboot.webflux.app;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;
import com.bolsadeideas.springboot.webflux.app.models.services.ProductoService;

import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringBootWebfluxApirestApplicationTests {
	
	@Autowired
	private WebTestClient client;
	
	@Autowired
	private ProductoService service;
	
	@Test
	public void listarTest() {
		client.get()
		.uri("/api/v2/productos")
		.accept(APPLICATION_JSON_UTF8)
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(APPLICATION_JSON_UTF8)
		.expectBodyList(Producto.class)
		.consumeWith(response -> {
			List<Producto> productos = response.getResponseBody();
			productos.forEach(p -> {
				System.out.print(p.getNombre());
			});
			
			Assertions.assertThat(productos.size()>0).isTrue();
		});
		//.hasSize(9);
	}
	
	@Test
	public void verTest() {
	Producto producto = service.findByNombre("TV Panasonic Pantalla LCD").block();
		client.get()
		.uri("/api/v2/productos/{id}", Collections.singletonMap("id", producto.getId()))
		.accept(APPLICATION_JSON_UTF8)
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(APPLICATION_JSON_UTF8)
		.expectBody()
		.jsonPath("$.id").isNotEmpty()
		.jsonPath("$.nombre").isEqualTo("TV Panasonic Pantalla LCD");
	}

}
