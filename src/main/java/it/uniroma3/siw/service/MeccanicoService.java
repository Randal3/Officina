package it.uniroma3.siw.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.uniroma3.siw.model.Meccanico;
import it.uniroma3.siw.repository.MeccanicoRepository;

@Service
public class MeccanicoService {

	
    @Autowired
    protected MeccanicoRepository meccanicoRepository;
	
	@Transactional
    public Meccanico setMeccanico(Meccanico meccanico) {
       return this.meccanicoRepository.save(meccanico);
    }

	@Transactional
    public List<Meccanico> listaMeccanico() {
        return (List<Meccanico>) this.meccanicoRepository.findAll();
    }

	@Transactional
	public List<Meccanico> listaMeccaniciAutorizzati(Long id) {
		List<Meccanico> meccanico = this.meccanicoRepository.findByIdTipologia(id);
		return meccanico;
	}
}
