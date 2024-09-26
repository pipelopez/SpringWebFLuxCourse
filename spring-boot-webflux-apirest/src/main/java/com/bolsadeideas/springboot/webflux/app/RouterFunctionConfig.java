package com.bolsadeideas.springboot.webflux.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.bolsadeideas.springboot.webflux.app.handler.ProductoHandler;
import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;
import com.bolsadeideas.springboot.webflux.app.models.services.ProductoService;


import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouterFunctionConfig {
	
	
	
	@Bean
	public RouterFunction<ServerResponse> routes(ProductoHandler handler){		
		return route(GET("/api/v2/productos").or(GET("/api/v3/productos")), /*request -> handler.listar(request)*/ handler::listar);
	}

}
