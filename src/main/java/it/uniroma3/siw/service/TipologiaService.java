package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.TipologiaIntervento;
import it.uniroma3.siw.repository.TipologiaRepository;

@Service
public class TipologiaService {

    @Autowired
    protected TipologiaRepository tipologiaRepository;
	
	@Transactional
    public TipologiaIntervento setTipologia(TipologiaIntervento tipologia) {
       return this.tipologiaRepository.save(tipologia);
    }

	@Transactional
    public List<TipologiaIntervento> tipi() {
        return (List<TipologiaIntervento>) this.tipologiaRepository.findAll();
    }
}
