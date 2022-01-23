package it.uniroma3.siw.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PrenotazioneId implements Serializable{
	@Column(name ="Meccanico_id")
	private Long meccanico_id;
	@Column(name ="Intervento_id")
	private Long tipologia_id;
	
	@Column(name = "Utente_id")
	private Long utente_id;
	

}
