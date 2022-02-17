package it.uniroma3.siw.service;

import java.util.List;
import java.util.Optional;

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
        return (List<Utente>) this.utenteRepository.findClienti();
    }

    @Transactional
	public void elimina(long id) {
		this.utenteRepository.deleteById(id);
	}
    
	@Transactional
    public Utente getUtente(long id) {
		Optional<Utente> utente = this.utenteRepository.findById(id);
        return utente.orElse(null);
    }

    @Transactional
    public void saveUser(Utente utente) {
       //credentials.setRole(Credentials.ADMIN_ROLE);
       utenteRepository.save(utente);
    }
    
    @Transactional
	public Utente getNumero(String numero) {
		Optional<Utente> valida = utenteRepository.findByNumero(numero);
		return valida.orElse(null);
	}

}
