package it.uniroma3.siw.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Utente;

public interface UtenteRepository extends CrudRepository<Utente, Long> {

	Optional<Utente> findByNumero(String numero);


	
	
}
