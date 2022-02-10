package it.uniroma3.siw.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import it.uniroma3.siw.controller.validator.CredentialsValidator;
import it.uniroma3.siw.controller.validator.UtenteValidator;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Meccanico;
import it.uniroma3.siw.model.Prenotazione;
import it.uniroma3.siw.model.TipologiaIntervento;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.MeccanicoService;
import it.uniroma3.siw.service.PrenotazioneService;
import it.uniroma3.siw.service.TipologiaService;
import it.uniroma3.siw.service.UtenteService;

@Controller
public class MainController {
	
	@Autowired
	private CredentialsService credentialService;
	@Autowired
	private UtenteValidator userValidator;
	@Autowired
	private CredentialsValidator credentialsValidator;
	@Autowired
	private TipologiaService tipologiaService;
	@Autowired
	private UtenteService utenteService;
	@Autowired
	private MeccanicoService meccanicoService;
	@Autowired
	private PrenotazioneService prenotazioneService;
	
	
	private long prenotazione_id;
	//Sezione Index
	
	@RequestMapping(value = {"/index", "/"}, method = RequestMethod.GET)
    public String index(Model model) {
		autorizzazione(model);
		return "index";
    }
	
	//Sezione interventi
	
	@RequestMapping(value = "/interventi", method = RequestMethod.GET)
    public String interventi() {
		return "interventi";
    }
	
	//Sezione registrazione
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Model model) {
		Utente utente = new Utente();
		Credentials credenziali = new Credentials();
		model.addAttribute("user", utente);
		model.addAttribute("credentials", credenziali);
		return "register";
    }
	
	@RequestMapping(value = "/register" , method = RequestMethod.POST)
    public String registerUser(@ModelAttribute("user") Utente user, BindingResult userBindingResult, @ModelAttribute("credentials") Credentials credentials, BindingResult credentialsBindingResult, Model model) {

        this.userValidator.validate(user, userBindingResult);
        this.credentialsValidator.validate(credentials, credentialsBindingResult);
        if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
            credentials.setUser(user);
            credentialService.saveCredentials(credentials);
            model.addAttribute("ROLE", 3);
            System.out.println("SONO USER");
            return "index";
        }
        return "register";
    }
	
	//Sezione logout
	@RequestMapping(value = "/logout", method = RequestMethod.GET) 
	public String logout() {
		return "index";
	}
	
	//Sezione profilo
	
	@RequestMapping(value = "/profilo", method = RequestMethod.GET)
    public String profilo(Model model) {
		autorizzazione(model);
		return "profilo";
    }
	
	//Sezione contatti
	
	@RequestMapping(value = "/contatti", method = RequestMethod.GET)
    public String contatti(Model model) {
		autorizzazione(model);
		return "contatti";
    }
	
	//Sezione modifica  Interventi ADMIN
	
	@RequestMapping(value = "/admin/modificaInterventi", method = RequestMethod.GET)
    public String modificaInterventi(Model model) {
		
		model.addAttribute("tipologia", new TipologiaIntervento());
		
		model.addAttribute("tipi", tipologiaService.tipi());
		
		return "admin/modificaInterventi";
    }
	
	@RequestMapping(value = "/NuovaTipologia", method = RequestMethod.POST)
    public String nuovaTipologia(Model model, @ModelAttribute("tipologia") TipologiaIntervento tipologia) {
		
		tipologiaService.setTipologia(tipologia);
		return "redirect:admin/modificaInterventi";
    }
	
	//Sezione Visualizza Utenti ADMIN
	
	@RequestMapping(value = "/admin/visualizzaClienti", method = RequestMethod.GET)
    public String visualizzaClienti(Model model) {
		
		model.addAttribute("anagrafica", utenteService.anagrafica());
		
		return "admin/visualizzaClienti";
    }
	
	@GetMapping(value = "/admin/eliminaUtente/{id}")
    public String eliminaUtente(@PathVariable("id") Long id) {
		
		this.credentialService.elimina(id);
		this.utenteService.elimina(id);
        
        return "redirect:/admin/visualizzaClienti";
    }
	
	//Sezione Visualizza Meccanici ADMIN
	
	@RequestMapping(value = "/admin/visualizzaMeccanici", method = RequestMethod.GET)
    public String visualizzaMeccanici(Model model) {
		
		model.addAttribute("meccanico", new Meccanico());
		
		model.addAttribute("nuovoMeccanico", meccanicoService.aggiungiMeccanico());
		
		model.addAttribute("tipi", tipologiaService.tipi());
		
		return "admin/visualizzaMeccanici";
    }
	
	@RequestMapping(value = "/NuovoMeccanico", method = RequestMethod.POST)
    public String nuovoMeccanico(Model model, @ModelAttribute("meccanico") Meccanico meccanico) {
		
		meccanicoService.setMeccanico(meccanico);
		return "redirect:admin/visualizzaMeccanici";
    }
	
	//Sezione Aggiungi-Prenota Intervento ADMIN
	
	@RequestMapping(value = "/admin/prenotaInterventi", method = RequestMethod.GET)
    public String aggiungiIntervento(Model model) {
		model.addAttribute("tipi", tipologiaService.tipi());
		model.addAttribute("anagrafica", utenteService.anagrafica());
		
		Prenotazione prenotazione = new Prenotazione();
		model.addAttribute(prenotazione);
		
		return "admin/prenotaInterventi";
    }
	
	@RequestMapping(value = "/NuovaPrenotazione", method = RequestMethod.POST)
    public String nuovarPenotazione(Model model, @ModelAttribute("prenotazione") Prenotazione prenotazione) {
		System.out.println(prenotazione.getData_intervento());
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		prenotazione.setData_prenotazione(dtf.format(LocalDateTime.now()));
		prenotazione.setConferma(false);
		prenotazioneService.setPrenotazione(prenotazione);
		
		return "redirect:index";
    }
	
	//Cronologia Interventi ADMIN
	
	@RequestMapping(value = "/admin/cronologiaInterventi", method = RequestMethod.GET)
    public String cronologiaInterventi(Model model) {
		model.addAttribute("prenotazione", prenotazioneService.infoPrenotazione());
		
		return "admin/cronologiaInterventi";
    }
	
	@GetMapping(value = "/admin/confermaIntervento/{id}")
    public String confermaIntervento(@PathVariable("id") Long id, Model model) {
		
		Prenotazione prenotazione = prenotazioneService.getPrenotazione(id);
        model.addAttribute("meccanico", meccanicoService.listaMeccaniciAutorizzati(prenotazione.getTipologia().getId()));
        model.addAttribute("prenotazione", prenotazione);
        
        prenotazione_id = prenotazione.getId();
        return "/admin/confermaIntervento";
    }
	
	@RequestMapping(value = "/ConfermaPrenotazione", method = RequestMethod.POST)
    public String confermaIntervento(Model model, @ModelAttribute("prenotazione") Prenotazione prenotazione) {
		Meccanico meccanico = prenotazione.getMeccanico();
		prenotazione = prenotazioneService.getPrenotazione(prenotazione_id);
		prenotazione.setMeccanico(meccanico);
		prenotazione.conferma();
		/*
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		p.setData_Intervento(dtf.format(LocalDateTime.now()));
		*/
		
		prenotazioneService.setPrenotazione(prenotazione);
		return "redirect:/admin/cronologiaInterventi";
    }
	
	//Funzione Accesso [ADMIN - User - Generico ]
	
	private void autorizzazione(Model model) {
		model.addAttribute("ROLE", 3);
		try {
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		    Credentials credentials = credentialService.getCredentials(userDetails.getUsername());
		    
			if(credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
				model.addAttribute("ROLE", 0);
			}else {
				if(credentials.getRole().equals(Credentials.DEFAULT_ROLE))model.addAttribute("ROLE", 1);
			}
		}catch (Exception e) {
			System.out.println("UTENTE NON LOGGATO");
		}
	}
}
