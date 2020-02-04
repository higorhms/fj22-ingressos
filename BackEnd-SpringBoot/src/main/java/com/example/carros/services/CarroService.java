package com.example.carros.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.example.carros.models.Carro;
import com.example.carros.repository.CarrosRepository;

@Service
public class CarroService {

	@Autowired
	private CarrosRepository repository;
	private Optional<Carro> carroById;

	public Iterable<Carro> getCarros() {
		return repository.findAll();

	}

	public Optional<Carro> getCarroById(Long id) {

		return repository.findById(id);
	}

	public Iterable<Carro> getCarroByTipo(String tipo) {
		return repository.findByTipo(tipo);
	}

	public Carro save(Carro carro) {

		return repository.save(carro);
	}

	public Carro update(Long id, Carro carro) {
		Assert.notNull(id, "Não foi possivel localizar o registro");

		Optional<Carro> optional = getCarroById(id);
		if (optional.isPresent()) {
			Carro carroDb = optional.get();

			carroDb.setNome(carro.getNome());
			carroDb.setTipo(carro.getTipo());

			repository.save(carroDb);

			return carroDb;
		} else {
			throw new RuntimeException();
		}
	}

	public void delete(Long id) {

		Optional<Carro> carroD = getCarroById(id);
		if (carroD.isPresent()) {
			repository.deleteById(carroD.get().getId());
		}
	}

}
