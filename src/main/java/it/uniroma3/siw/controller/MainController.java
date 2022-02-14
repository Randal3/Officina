package it.uniroma3.siw.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import it.uniroma3.siw.controller.validator.CredentialsValidator;
import it.uniroma3.siw.controller.validator.MeccaniciValidator;
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
	private MeccaniciValidator meccanicoValidator;
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

	
	//Sezione Index
	
	@GetMapping(value ={"/","/index","/login"})
    public String index(@RequestParam Optional<String> error,Model model ) {
		autorizzazione(model);
        if(error.orElse(null) != null) {
            model.addAttribute("errore",true);
        }else {
            model.addAttribute("errore",false);
        }
        return "index";
    }
	
	//Sezione contatti
	
	@RequestMapping(value = "/contatti", method = RequestMethod.GET)
    public String contatti(Model model) {
		autorizzazione(model);
		return "contatti";
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
            return "index";
        }
        return "register";
    }
	
	//Sezione profilo
	
	@RequestMapping(value = "/utente/profilo", method = RequestMethod.GET)
    public String profilo(Model model) {
		
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = credentialService.getCredentials(userDetails.getUsername());
        Utente utente = credentials.getUser();
        model.addAttribute("utente", utente);
		
		return "utente/profilo";
    }
	
	//Sezione interventi
	
		@RequestMapping(value = "/utente/interventi", method = RequestMethod.GET)
	    public String interventi(Model model) {
			
			model.addAttribute("tipi", tipologiaService.tipi());
			
			return "/utente/interventi";
	    }
		
		@GetMapping(value ="/utente/cronologiaUtente")
	    public String cronologiaUtente(Model model) {
	        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        Credentials credentials = credentialService.getCredentials(userDetails.getUsername());
	        Utente utente = credentials.getUser();
	        model.addAttribute("prenotazione",prenotazioneService.findByUtente(utente));
	        return "/utente/cronologiaUtente";
	    }
		
	
	//ADMIN
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
		Utente utente = new Utente();
		Credentials credenziali = new Credentials();
		model.addAttribute("user", utente);
		model.addAttribute("credentials", credenziali);
		
		model.addAttribute("users", utenteService.anagrafica());
		
		return "admin/visualizzaClienti";
    }
	
	@GetMapping(value = "/admin/eliminaUtente/{id}")
    public String eliminaUtente(@PathVariable("id") Long id) {
		
		this.credentialService.elimina(id);
		this.utenteService.elimina(id);
        
        return "redirect:/admin/visualizzaClienti";
    }
	
	@GetMapping(value = "/admin/aggiornaUtente/{id}")
    public String aggiornaUtente(@PathVariable("id") Long id, Model model) {
		
		Utente utente = this.utenteService.getUtente(id);
		model.addAttribute("utente", utente);
        
        return "/admin/aggiornaUtente";
    }
	
	@PostMapping(value = "/admin/aggiornaUtenti")
    public String aggiornaUtenti(@RequestParam Long ClienteId, Model model,@ModelAttribute("utente") Utente utente) {
		
        Utente utente_attuale = this.utenteService.getUtente(ClienteId);

        utente_attuale.setNome(utente.getNome());
        utente_attuale.setCognome(utente.getCognome());
        utente_attuale.setNumero(utente.getNumero());
        this.utenteService.saveUser(utente_attuale);
		
        return "redirect:/admin/visualizzaClienti";
    }
	
	//REGISTER ADMIN
	
	@RequestMapping(value = "/registerAdmin" , method = RequestMethod.POST)
    public String registerAdmin(@ModelAttribute("user") Utente user, BindingResult userBindingResult, @ModelAttribute("credentials") Credentials credentials, BindingResult credentialsBindingResult, Model model) {

        this.userValidator.validate(user, userBindingResult);
        this.credentialsValidator.validate(credentials, credentialsBindingResult);
        if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
            credentials.setUser(user);
            credentialService.saveCredentials(credentials);
            return "redirect:/admin/visualizzaClienti";
        }
        model.addAttribute("users", utenteService.anagrafica());
        return "/admin/visualizzaClienti";
    }
	
	//Sezione Visualizza Meccanici ADMIN
	
	@RequestMapping(value = "/admin/visualizzaMeccanici", method = RequestMethod.GET)
    public String visualizzaMeccanici(Model model) {
		
		model.addAttribute("listaMeccanico", meccanicoService.listaMeccanico());
		
		model.addAttribute("tipi", tipologiaService.tipi());
		Meccanico meccanico = new Meccanico();
		model.addAttribute("meccanico", meccanico);
		
		return "admin/visualizzaMeccanici";
    }
	
	@PostMapping(value ="/NuovoMeccanico")
	public String nuovoMeccanico(Model model, @ModelAttribute("meccanico") Meccanico meccanico , BindingResult meccanicoBindingResult) {

		this.meccanicoValidator.validate(meccanico, meccanicoBindingResult);
		if(!meccanicoBindingResult.hasErrors()) {
			this.meccanicoService.setMeccanico(meccanico);
			return "redirect:admin/visualizzaMeccanici";
		}
		model.addAttribute("listaMeccanico",this.meccanicoService.listaMeccanico());
		model.addAttribute("tipi", this.tipologiaService.tipi());
		return "admin/visualizzaMeccanici";
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
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		prenotazione.setData_prenotazione(dtf.format(LocalDateTime.now()));
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
        
        return "/admin/confermaIntervento";
    }
	
	@PostMapping(value = "/admin/ConfermaPrenotazione")
    public String confermaIntervento(@RequestParam Long prenotaizioneId, Model model, @ModelAttribute("prenotazione") Prenotazione prenotazione) {
		
		Meccanico meccanico = prenotazione.getMeccanico();
		String data = prenotazione.getData_intervento();
		prenotazione = prenotazioneService.getPrenotazione(prenotaizioneId);
		prenotazione.setMeccanico(meccanico);
		prenotazione.setData_intervento(data);
		prenotazione.conferma();
		prenotazioneService.setPrenotazione(prenotazione);
		return "redirect:/admin/cronologiaInterventi";
    }

	//Informazioni Interventi ADMIN
	
	@GetMapping(value = "/admin/infoIntervento/{id}")
    public String infoIntervento(@PathVariable("id") Long id, Model model) {
		
		Prenotazione prenotazione = prenotazioneService.getPrenotazione(id);
        model.addAttribute("prenotazione", prenotazione);

        
        return "/admin/infoIntervento";
    }
	
	@GetMapping(value = "/admin/eliminaIntervento/{id}")
    public String eliminaIntervento(@PathVariable("id") Long id) {
		
		this.prenotazioneService.elimina(id);
        
        return "redirect:/admin/cronologiaInterventi";
    }
	
	@GetMapping(value = "/admin/statusIntervento/{id}")
    public String statusIntervento(@PathVariable("id") Long id) {
		System.out.println("SONO QUI " + this.prenotazioneService.getPrenotazione(id));
		Prenotazione prenotazione = this.prenotazioneService.getPrenotazione(id);
		prenotazione.setStatus();
		prenotazioneService.setPrenotazione(prenotazione);
		
        return "redirect:/admin/cronologiaInterventi";
    }
	
	//Funzione Accesso NAVBAR [ADMIN - User - Generico ]
	
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
