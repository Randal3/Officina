package it.uniroma3.siw.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Prenotazione;
import it.uniroma3.siw.model.TipologiaIntervento;
import it.uniroma3.siw.repository.PrenotazioneRepository;

@Service
public class PrenotazioneService {


    @Autowired
    protected PrenotazioneRepository prenotazioneRepository;

    @Transactional
	public void setPrenotazione(Prenotazione prenotazione) {
		this.prenotazioneRepository.save(prenotazione);
	}
    
	@Transactional
    public List<Prenotazione> infoPrenotazione() {
        return (List<Prenotazione>) this.prenotazioneRepository.findAll();
    }
	
	@Transactional
    public Prenotazione getPrenotazione(long id) {
		Optional<Prenotazione> prenotazione = this.prenotazioneRepository.findById(id);
        return prenotazione.orElse(null);
    }
	
    @Transactional
	public void elimina(long id) {
		this.prenotazioneRepository.deleteById(id);
	}
}
