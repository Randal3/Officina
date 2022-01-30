package it.uniroma3.siw.model;

import java.util.Date;
import java.util.List;

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

    @Column
    private String data_prenotazione;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date data_intervento;


    @ManyToOne
    private Meccanico meccanico;

    @ManyToOne
    private Utente cliente;

    @ManyToMany
    private List<TipologiaIntervento> tipologia;

    @ManyToOne(cascade = CascadeType.ALL)
    private Auto auto;
    

}
