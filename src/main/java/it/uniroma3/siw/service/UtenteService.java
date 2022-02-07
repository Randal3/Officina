package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repository.UtenteRepository;

@Service
public class UtenteService {


    @Autowired
    protected UtenteRepository utenteRepository;
    
	@Transactional
    public List<Utente> anagrafica() {
        return (List<Utente>) this.utenteRepository.findAll();
    }

}
