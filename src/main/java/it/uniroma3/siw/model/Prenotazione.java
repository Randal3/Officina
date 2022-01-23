package it.uniroma3.siw.model;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Entity
@Data
public class Prenotazione {
	
	@EmbeddedId
	private PrenotazioneId id;
	
	@Column
	private Date data_prenotazione;
	
	@Column
	private Date data_intervento;
	

	@ManyToOne
	@MapsId("Meccanico_id")
	@JoinColumn(name ="Meccanico_id")
	private Meccanico meccanico;
	
	@ManyToOne
	@MapsId("Utente_id")
	@JoinColumn(name = "Utente_id")
	private Utente cliente;
	
	@ManyToOne
	@MapsId("Intervento_id")
	@JoinColumn(name = "Intervento_id")
	private TipologiaIntervento tipologia;
}
