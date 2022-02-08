package it.uniroma3.siw.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.model.Credentials;

public interface CredentialsRepository extends CrudRepository<Credentials, Long> {

	public Optional<Credentials> findByUsername(String username);

	@Modifying(clearAutomatically = true)
	@Query(value = "DELETE from credentials c where c.user_id = ?1", nativeQuery = true)
	void deleteByIdUtente(long id);
	
}
