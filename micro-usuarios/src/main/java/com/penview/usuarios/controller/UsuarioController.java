package com.penview.usuarios.controller;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penview.usuarios.entity.dto.ResponseDTO;

@RestController
@RequestMapping("/api/usuarios/")
public class UsuarioController {

	@GetMapping("/users")
	public ResponseEntity<ResponseDTO> obtenerHoteles() {
		ResponseDTO response = new ResponseDTO();
		
		response.setStatus(200);
		response.setMessage("OK");
		
		return ResponseEntity.status(HttpStatus.SC_OK).body(response);
	}
}
