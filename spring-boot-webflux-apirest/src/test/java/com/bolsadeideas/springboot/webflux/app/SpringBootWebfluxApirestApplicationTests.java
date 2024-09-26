package com.bolsadeideas.springboot.webflux.app;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.bolsadeideas.springboot.webflux.app.models.documents.Categoria;
import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;
import com.bolsadeideas.springboot.webflux.app.models.services.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringBootWebfluxApirestApplicationTests {
	
	@Autowired
	private WebTestClient client;
	
	@Autowired
	private ProductoService service;
	
	@Value("${config.base.endpoint}")
	private String url;
	
	@Test
	public void listarTest() {
		client.get()
		.uri(url)
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
		.uri(url + "/{id}", Collections.singletonMap("id", producto.getId()))
		.accept(APPLICATION_JSON_UTF8)
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(APPLICATION_JSON_UTF8)
		.expectBody(Producto.class)
		.consumeWith(response -> {
			Producto p = response.getResponseBody();

			Assertions.assertThat(p.getId()).isNotEmpty();
			Assertions.assertThat(p.getId().length()>0).isTrue();
			Assertions.assertThat(p.getNombre()).isEqualTo("TV Panasonic Pantalla LCD");
		});
		/*.jsonPath("$.id").isNotEmpty()
		.jsonPath("$.nombre").isEqualTo("TV Panasonic Pantalla LCD");*/
	}
	
	@Test
	public void crearTest() {
		
		Categoria categoria = service.findCategoriaByNombre("Muebles").block();
		
		Producto producto = new Producto("Mesa Comedor", 100.00, categoria);
		
		client.post().uri(url)
		.contentType(APPLICATION_JSON_UTF8)
		.accept(MediaType.APPLICATION_JSON_UTF8)
		.body(Mono.just(producto), Producto.class)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(APPLICATION_JSON_UTF8)
		.expectBody()
		.jsonPath("$.id").isNotEmpty()
		.jsonPath("$.nombre").isEqualTo("Mesa Comedor")
		.jsonPath("$.categoria.nombre").isEqualTo("Muebles");
	}
		
	@Test
	public void crearTestV1() {
		
		Categoria categoria = service.findCategoriaByNombre("Muebles").block();
		
		Producto producto = new Producto("Mesa Comedor", 100.00, categoria);
		
		client.post().uri("/api/productos")
		.contentType(APPLICATION_JSON_UTF8)
		.accept(MediaType.APPLICATION_JSON_UTF8)
		.body(Mono.just(producto), Producto.class)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(APPLICATION_JSON_UTF8)
		.expectBody()
		.jsonPath("$.producto.id").isNotEmpty()
		.jsonPath("$.producto.nombre").isEqualTo("Mesa Comedor")
		.jsonPath("$.producto.categoria.nombre").isEqualTo("Muebles");
	}
	
	@Test
	public void crear2Test() {
		
		Categoria categoria = service.findCategoriaByNombre("Muebles").block();
		
		Producto producto = new Producto("Mesa Comedor", 100.00, categoria);
		client.post().uri(url)
		.contentType(APPLICATION_JSON_UTF8)
		.accept(MediaType.APPLICATION_JSON_UTF8)
		.body(Mono.just(producto), Producto.class)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(APPLICATION_JSON_UTF8)
		.expectBody(Producto.class)
		.consumeWith(response -> {
			Producto p = response.getResponseBody();
			
			Assertions.assertThat(p.getId()).isNotEmpty();
			Assertions.assertThat(p.getNombre()).isEqualTo("Mesa Comedor");
			Assertions.assertThat(p.getCategoria().getNombre()).isEqualTo("Muebles");		
		});
	}
	
	@Test
	public void crear2TestV1() {
		
		Categoria categoria = service.findCategoriaByNombre("Muebles").block();
		
		Producto producto = new Producto("Mesa Comedor", 100.00, categoria);
		client.post().uri("/api/productos")
		.contentType(APPLICATION_JSON_UTF8)
		.accept(MediaType.APPLICATION_JSON_UTF8)
		.body(Mono.just(producto), Producto.class)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(APPLICATION_JSON_UTF8)
		.expectBody(new ParameterizedTypeReference<LinkedHashMap<String, Object>>(){})
		.consumeWith(response -> {
			Object o = response.getResponseBody().get("producto");
			
			Producto p = new ObjectMapper().convertValue(o, Producto.class);
			
			Assertions.assertThat(p.getId()).isNotEmpty();
			Assertions.assertThat(p.getNombre()).isEqualTo("Mesa Comedor");
			Assertions.assertThat(p.getCategoria().getNombre()).isEqualTo("Muebles");		
		});
	}
	
	@Test
	public void editarTest() {
		Producto producto = service.findByNombre("Sony Notebook").block();
		Categoria categoria = service.findCategoriaByNombre("Electrónico").block();
		
		Producto productoEditado = new Producto("Asus Notebook", 700.00, categoria);
		
		client.put().uri(url + "/{id}", Collections.singletonMap("id", producto.getId()))
		.contentType(APPLICATION_JSON_UTF8)
		.accept(MediaType.APPLICATION_JSON_UTF8)
		.body(Mono.just(productoEditado), Producto.class)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(APPLICATION_JSON_UTF8)
		.expectBody()
		.jsonPath("$.id").isNotEmpty()
		.jsonPath("$.nombre").isEqualTo("Asus Notebook")
		.jsonPath("$.categoria.nombre").isEqualTo("Electrónico");
		
	}
	
	@Test
	public void eliminarTest() {
		Producto producto = service.findByNombre("Mica Cómoda 5 Cajones").block();
		client.delete()
		.uri(url + "/{id}", Collections.singletonMap("id", producto.getId()))
		.exchange()
		.expectStatus().isNoContent()
		.expectBody()
		.isEmpty();
		
		client.get()
		.uri(url + "/{id}", Collections.singletonMap("id", producto.getId()))
		.exchange()
		.expectStatus().isNotFound()
		.expectBody()
		.isEmpty();		
	}
	

}
