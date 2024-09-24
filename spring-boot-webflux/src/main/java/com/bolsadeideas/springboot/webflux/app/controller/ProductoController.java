package com.bolsadeideas.springboot.webflux.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.bolsadeideas.springboot.webflux.app.models.dao.ProductoDao;
import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Flux;


@Controller
public class ProductoController {
	
	@Autowired
	private ProductoDao dao;
	
	@GetMapping({"/listar", "/"})
	public String listar(Model model) {
		
		Flux<Producto> productos = dao.findAll() ;
		
		model.addAttribute("productos", productos);
		model.addAttribute("titulo", "Listado de productos");
		return "listar";
		
	}

}
