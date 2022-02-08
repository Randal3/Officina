package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Meccanico;

public interface MeccanicoRepository extends CrudRepository<Meccanico, Long> {
	
	@Query(value = "select * from Meccanico m join meccanico_interventi t on m.id = t.meccanico_id where t.interventi_id=?1 ", nativeQuery = true)
	List<Meccanico> findByIdTipologia(Long id);
}
