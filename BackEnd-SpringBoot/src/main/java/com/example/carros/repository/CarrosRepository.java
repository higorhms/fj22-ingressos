package com.example.carros.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.carros.models.Carro;

public interface CarrosRepository extends JpaRepository<Carro, Long> {

	List<Carro> findByTipo(String tipo);

}
