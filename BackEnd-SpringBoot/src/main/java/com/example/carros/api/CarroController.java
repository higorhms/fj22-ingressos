package com.example.carros.api;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.carros.models.Carro;
import com.example.carros.models.dto.CarroDTO;
import com.example.carros.services.CarroService;

@CrossOrigin(origins = { "*", "http://localhost:3000" })
@RestController
@RequestMapping("/api/v1/carros")
public class CarroController {

	@Autowired
	private CarroService service;

	@GetMapping
	public ResponseEntity<List<CarroDTO>> get() {
		return ResponseEntity.ok(service.getCarros());
	}

	@GetMapping("/{id}")
	public ResponseEntity<CarroDTO> getCarroById(@PathVariable("id") Long id) {

		Optional<CarroDTO> carro = service.getCarroById(id);

		return carro.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/tipo/{tipo}")
	public ResponseEntity<List<CarroDTO>> getCarroByTipo(@PathVariable("tipo") String tipo) {
		List<CarroDTO> carros = service.getCarroByTipo(tipo);

		return carros.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(carros);
	}

	@PostMapping
	public ResponseEntity<?> post(@RequestBody Carro carro) {
		try {
			CarroDTO carroDto = service.insert(carro);
			URI location = getUri(carroDto.getId());
			return ResponseEntity.created(location).body(carroDto);
		} catch (Exception e) {

			return ResponseEntity.badRequest().build();
		}
	}

	private URI getUri(Long id) {
		return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> alterarCarro(@PathVariable("id") Long id, @RequestBody Carro carro) {
		CarroDTO c = service.update(id, carro);

		return c != null ? ResponseEntity.ok(c) : ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> post(@PathVariable("id") Long id) {

		return service.delete(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
	}

}
