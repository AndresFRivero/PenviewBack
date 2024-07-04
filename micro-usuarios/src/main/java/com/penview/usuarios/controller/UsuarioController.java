package com.penview.usuarios.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios/")
public class UsuarioController {

	@GetMapping("/obtenerUsuarios")
	public String obtenerHoteles() {
		return "Usuarios";
	}
}
