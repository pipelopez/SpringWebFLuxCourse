package com.bolsadeideas.springboot.reactor.app;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bolsadeideas.springboot.reactor.app.models.Usuario;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {
	
	private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		ejemploFlatMap();
	}
	
public void ejemploFlatMap() throws Exception {
		
		List<String> usuariosList = new ArrayList<>();
		usuariosList.add("Andres Guzman");
		usuariosList.add( "Jaime Fulano");
		usuariosList.add("Pipe Sultano");
		usuariosList.add("Diego Molano");
		usuariosList.add("Pedro Ramirez");
		usuariosList.add("Bruce Lee");
		usuariosList.add("Bruce Willis");
		
		Flux.fromIterable(usuariosList)
				.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.flatMap(usuario -> {
					if(usuario.getNombre().equalsIgnoreCase("bruce")) {
						return Mono.just(usuario);
					} else {
						return Mono.empty();
					}
				})
				.map(usuario -> {
					String nombre =  usuario.getNombre().toLowerCase();
					usuario.setNombre(nombre);
					return usuario;
					})
				.subscribe(u -> log.info(u.toString()));
	}
	
	public void ejemploIterable() throws Exception {
		
		List<String> usuariosList = new ArrayList<>();
		usuariosList.add("Andres Guzman");
		usuariosList.add( "Jaime Fulano");
		usuariosList.add("Pipe Sultano");
		usuariosList.add("Diego Molano");
		usuariosList.add("Pedro Ramirez");
		usuariosList.add("Bruce Lee");
		usuariosList.add("Bruce Willis");
		
		Flux<String> nombres = Flux.fromIterable(usuariosList); /*Flux.just("Andres Guzman", "Jaime Fulano", "Pipe Sultano", "Diego Molano", "Pedro Ramirez", "Bruce Lee", "Bruce Willis");*/
		
		Flux<Usuario> usuarios = nombres.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.filter(usuario -> usuario.getNombre().toLowerCase().equalsIgnoreCase("bruce"))
				.doOnNext(usuario -> {					
					if(usuario == null) {
						throw new RuntimeException("Los elementos no pueden ser vacíos");
					}
					
					System.out.println(usuario.getNombre().concat(" ").concat(usuario.getApellido()));
					
					})
				.map(usuario -> {
					String nombre =  usuario.getNombre().toLowerCase();
					usuario.setNombre(nombre);
					return usuario;
					});
		
		usuarios.subscribe(e -> log.info(e.toString()), 
				error -> log.error(error.getMessage()),
				new Runnable() {
					@Override
					public void run() {
						log.info("Ha finalizado la ejecución del observable con éxito!");
					}
				});
	}

}


