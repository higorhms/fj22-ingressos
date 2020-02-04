package com.example.carros.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.carros.models.Carro;

public interface CarrosRepository extends CrudRepository<Carro, Long> {

	Iterable<Carro> findByTipo(String tipo);

}
