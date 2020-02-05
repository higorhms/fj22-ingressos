package com.example.carros.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.example.carros.models.Carro;
import com.example.carros.models.dto.CarroDTO;
import com.example.carros.repository.CarrosRepository;

@Service
public class CarroService {

	@Autowired
	private CarrosRepository repository;

	public List<CarroDTO> getCarros() {
		return repository.findAll().stream().map(CarroDTO::create).collect(Collectors.toList());

	}

	public Optional<CarroDTO> getCarroById(Long id) {
		return repository.findById(id).map(CarroDTO::create);
	}

	public List<CarroDTO> getCarroByTipo(String tipo) {
		return repository.findByTipo(tipo).stream().map(CarroDTO::create).collect(Collectors.toList());
	}

	public CarroDTO insert(Carro carro) {
		Assert.isNull(carro.getId(), "Não foi possivel inserir o registro");

		return CarroDTO.create(repository.save(carro));
	}

	public CarroDTO update(Long id, Carro carro) {
		Assert.notNull(id, "Não foi possivel localizar o registro");

		Optional<Carro> optional = repository.findById(id);
		if (optional.isPresent()) {
			Carro carroDb = optional.get();

			carroDb.setNome(carro.getNome());
			carroDb.setTipo(carro.getTipo());

			repository.save(carroDb);

			return CarroDTO.create(carroDb);
		} else {
			return null;
		}
	}

	public boolean delete(Long id) {

		if (getCarroById(id).isPresent()) {
			repository.deleteById(id);
			return true;
		}
		return false;
	}

}
