package com.bolsadeideas.springboot.webflux.client.app.handler;

import java.net.URI;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.MediaType.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.bolsadeideas.springboot.webflux.client.app.models.Producto;
import com.bolsadeideas.springboot.webflux.client.app.models.services.ProductoService;

import reactor.core.publisher.Mono;

@Component
public class ProductoHandler {

	@Autowired
	private ProductoService service;

	public Mono<ServerResponse> listar(ServerRequest request) {
		return ServerResponse.ok().contentType(APPLICATION_JSON_UTF8)
				.body(service.findAll(), Producto.class);
	}

	public Mono<ServerResponse> ver(ServerRequest request) {
		String id = request.pathVariable("id");
		return service.findById(id)
				.flatMap(p -> ServerResponse.ok()
						.contentType(APPLICATION_JSON_UTF8)
						.syncBody(p))
				.switchIfEmpty(ServerResponse.notFound().build());

	}
	
	public Mono<ServerResponse> crear(ServerRequest request){
		Mono<Producto> producto = request.bodyToMono(Producto.class);
		
		return producto.flatMap(p -> {
			if(p.getCreateAt()==null) {
				p.setCreateAt(new Date());
			}
			return service.save(p);
			}).flatMap(p -> ServerResponse.created(URI.create("/api/client".concat(p.getId())))
					.contentType(APPLICATION_JSON_UTF8)
					.syncBody(p));
	}
	
	public Mono<ServerResponse> editar(ServerRequest request){
		Mono<Producto> producto = request.bodyToMono(Producto.class);
		String id = request.pathVariable("id");
		
		return producto.flatMap(p -> ServerResponse.created(URI.create("/api/client".concat(id)))
				.contentType(APPLICATION_JSON_UTF8)
				.body(service.update(p, id), Producto.class));
	}
	

	
	public Mono<ServerResponse> eliminar(ServerRequest request){
		String id = request.pathVariable("id");		
		return service.delete(id).then(ServerResponse.noContent().build());
	}

}
