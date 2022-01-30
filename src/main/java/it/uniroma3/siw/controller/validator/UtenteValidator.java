package it.uniroma3.siw.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Utente;

@Component
public class UtenteValidator implements Validator {

    final Integer MAX_NAME_LENGTH = 10;
    final Integer MIN_NAME_LENGTH = 2;

    @Override
    public void validate(Object o, Errors errors) {
        Utente user = (Utente) o;
        String nome = user.getNome().trim();
        String cognome = user.getCognome().trim();


        if (nome.isEmpty())
            errors.rejectValue("nome", "required");
        else if (nome.length() < MIN_NAME_LENGTH || nome.length() > MAX_NAME_LENGTH)
            errors.rejectValue("nome", "size");

        if (cognome.isEmpty())
            errors.rejectValue("cognome", "required");
        else if (cognome.length() < MIN_NAME_LENGTH || cognome.length() > MAX_NAME_LENGTH)
            errors.rejectValue("cognome", "size");
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Utente.class.equals(clazz);
    }

}