package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Prenotazione;

public interface InterventoRepository extends CrudRepository<Prenotazione, Long> {

}
