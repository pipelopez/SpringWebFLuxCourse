package com.bolsadeideas.springboot.webflux.app.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import static org.springframework.web.reactive.function.BodyInserters.*;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;
import com.bolsadeideas.springboot.webflux.app.models.services.ProductoService;

import reactor.core.publisher.Mono;

@Component
public class ProductoHandler {
	
	@Autowired
	private ProductoService service;
	
	public Mono<ServerResponse> listar(ServerRequest request){
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(service.findAll(), Producto.class);
	}
	
	public Mono<ServerResponse> ver(ServerRequest request){
		String id = request.pathVariable("id");
		return service.findById(id).flatMap(p -> ServerResponse
				.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(fromObject(p)))
				.switchIfEmpty(ServerResponse.notFound().build());
	}

}
