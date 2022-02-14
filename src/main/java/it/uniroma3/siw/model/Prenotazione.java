package it.uniroma3.siw.model;

import javax.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Entity
@Data
public class Prenotazione {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(value = AccessLevel.NONE)
    private Long id;

	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private String data_prenotazione;
    
    @Column(columnDefinition="TEXT")
    private String descrizione_veicolo;
    
    @Column
    private boolean conferma;
    
    @Column
    private boolean status;

    @Column
    private String data_intervento;

    @ManyToOne
    private Meccanico meccanico;

    @ManyToOne
    private Utente utente;

    @ManyToOne
    private TipologiaIntervento tipologia;

	public void setData_prenotazione(String now) {
		this.data_prenotazione = now;
	}

	public void conferma() {
		this.conferma = true;
	}
	

	public void setStatus() {
		this.status = true;
	}
}
