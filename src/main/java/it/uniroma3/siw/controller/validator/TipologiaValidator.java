package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import it.uniroma3.siw.model.TipologiaIntervento;
import it.uniroma3.siw.service.TipologiaService;

@Component
public class TipologiaValidator implements Validator {

    @Autowired
    private TipologiaService tipologiaService;

    @Override
    public void validate(Object o, Errors errors) {
        TipologiaIntervento tipo =(TipologiaIntervento) o;
        String nome = tipo.getNome().trim();

        if(nome.isEmpty())
            errors.rejectValue("nome", "required");
        else if (this.tipologiaService.findByNome(nome) != null)
            errors.rejectValue("nome", "duplicate");

    }

     @Override
        public boolean supports(Class<?> clazz) {
            return TipologiaService.class.equals(clazz);
        }
}