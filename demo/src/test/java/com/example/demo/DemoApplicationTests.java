package com.example.demo;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.demo.controller.dto.CarDto;

import reactor.core.publisher.Mono;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class DemoApplicationTests {

	@Autowired
	private WebTestClient client;
	
	@Test
	public void listarTest() {
		client.get()
		.uri("/car/all")
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().isOk()
		.expectBodyList(CarDto.class)
		.consumeWith(response -> {
			List<CarDto> cars = response.getResponseBody();
			cars.forEach(c -> {
				System.out.println(c.brand());
			});
			Assertions.assertThat(cars.size()>0).isTrue();
		});
	}
	
	@Test
	public void getTest() {
		client.get()
		.uri("/car/2")
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody(CarDto.class)
		.consumeWith(response -> {
			CarDto car = response.getResponseBody();
			
			Assertions.assertThat(car.brand()).isEqualTo("Daewo");
			Assertions.assertThat(car.kilowatt()).isEqualTo(3000);
		});
	}
	
	@Test
	public void sendMessage() {
		client.post().uri("/car/message")
		.contentType(MediaType.TEXT_PLAIN)
		.body(Mono.just("Hola"), String.class)
		.exchange()
		.expectBody(String.class)
		.consumeWith(response -> {
			String res =response.getResponseBody();
			Assertions.assertThat(res).isEqualTo("Message published successfully");
		});
	}
	
	@Test
	public void crearTest( ) {
		CarDto car = new CarDto(null, "Ferrari", 1200);
		
		client.post().uri("/car")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.body(Mono.just(car), CarDto.class)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody()
		.jsonPath("$.car.id").isNotEmpty()
		.jsonPath(".car.brand").isEqualTo(car.brand())
		.jsonPath("$.car.kilowatt").isEqualTo(car.kilowatt());
	}
	
	@Test
	public void editarTest() {
		CarDto editedCar = new CarDto(4, "Ferrari", 5000);
		
		client.put().uri("/car/4")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.body(Mono.just(editedCar), CarDto.class)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody()
		.jsonPath("$.id").isEqualTo(editedCar.id())
		.jsonPath("$.brand").isEqualTo(editedCar.brand())
		.jsonPath("$.kilowatt").isEqualTo(editedCar.kilowatt());
		
	}
	
	@Test
	public void eliminarTest() {
		CarDto car = new CarDto(null, "Nissan", 1200);
		
		client.post().uri("/car")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.body(Mono.just(car), CarDto.class)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody()
		.jsonPath("$.car.id").value(id -> {
			Integer carId = (Integer) id;
			client.delete()
			.uri("/car/" + carId)
			.exchange()
			.expectStatus().isNoContent()
			.expectBody()
			.isEmpty();
			
			client.get()
			.uri("/car/" + carId)
			.exchange()
			.expectStatus().isNotFound()
			.expectBody()
			.isEmpty();
		});		
	}

}
