package it.uniroma3.siw.controller;

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

import antlr.collections.List;
import it.uniroma3.siw.controller.validator.CredentialsValidator;
import it.uniroma3.siw.controller.validator.UtenteValidator;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Meccanico;
import it.uniroma3.siw.model.TipologiaIntervento;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.MeccanicoService;
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
	
	
	@RequestMapping(value = "/interventi", method = RequestMethod.GET)
    public String interventi() {
		return "interventi";
    }
	
	@RequestMapping(value = {"/index", "/"}, method = RequestMethod.GET)
    public String index(Model model) {
		autorizzazione(model);
		return "index";
    }
	
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
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET) 
	public String logout() {
		return "index";
	}
	
	@RequestMapping(value = "/profilo", method = RequestMethod.GET)
    public String profilo(Model model) {
		autorizzazione(model);
		return "profilo";
    }
	
	@RequestMapping(value = "/contatti", method = RequestMethod.GET)
    public String contatti(Model model) {
		autorizzazione(model);
		return "contatti";
    }
	
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
	
	@RequestMapping(value = "/admin/visualizzaClienti", method = RequestMethod.GET)
    public String visualizzaClienti(Model model) {
		
		model.addAttribute("anagrafica", utenteService.anagrafica());
		
		return "admin/visualizzaClienti";
    }
	
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
	
	@RequestMapping(value = "/admin/aggiungiIntervento", method = RequestMethod.GET)
    public String aggiungiIntervento(Model model) {
		model.addAttribute("tipi", tipologiaService.tipi());
		return "admin/aggiungiInterventi";
    }
	
	@GetMapping(value = "/admin/prenotaIntervento/{id}")
    public String prenotaIntervento(@PathVariable("id") Long id, Model model) {
		TipologiaIntervento tipi = this.tipologiaService.getTipologia(id);
		
        model.addAttribute("meccanico", meccanicoService.listaMeccaniciAutorizzati(id));
        
        return "/admin/prenotaIntervento";
    }
	
	
	
	
	
	
	
	
	
	
	private void autorizzazione(Model model) {
		System.out.println("PROVAPROVA");
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
