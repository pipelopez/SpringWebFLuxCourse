package com.bolsadeideas.springboot.webflux.app.models.dao;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Mono;

public interface ProductoDao extends ReactiveMongoRepository<Producto, String>{
	
	public Mono<Producto> findByNombre(String nombre); //tiene que tener la palabra findBy para que funcione
	
	@Query("{ 'nombre': ?0 }")
	public Mono<Producto> obtenerPorNombre(String nombre); //con este también funciona pero necesita el query

}
