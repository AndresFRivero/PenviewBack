package com.penview.hoteles.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/hoteles/")
public class HotelController {

	@GetMapping("/obtenerHoteles")
	public String obtenerHoteles() {
		return "Hoteles";
	}
	
	
}
