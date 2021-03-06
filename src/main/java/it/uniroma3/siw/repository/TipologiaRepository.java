package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.TipologiaIntervento;


public interface TipologiaRepository extends CrudRepository<TipologiaIntervento, Long> {

	Object findByNome(String nome);

}
