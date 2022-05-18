package de.hsrm.mi.web.projekt.benutzerprofil;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Operation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
<<<<<<< HEAD
=======
import org.springframework.web.bind.annotation.RequestParam;
>>>>>>> e071d7245a42e36c80b98d795122a86d769e62c4
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
// @Validated //Ueberpruefung aktivieren
@RequestMapping("/")
@SessionAttributes(names={"profil", "profilliste"})
public class BenutzerprofilController {
    Logger logger = LoggerFactory.getLogger(BenutzerprofilController.class);
    
    @Autowired
    BenutzerprofilService service;

    @ModelAttribute("profil")
    public void initProfil(Model m){
        BenutzerProfil bp = new BenutzerProfil();
<<<<<<< HEAD
        // bp.setName("Peter");
        // bp.setGeburtsdatum(LocalDate.now());
        // bp.setEmail("ha@lo.lo");
        // bp.setAdresse("Goldgasse 10, 65185 Wiesbaden");
        // bp.setInteressen("schwimmen, lesen, schreiben");
        // bp.setLieblingsfarbe("#000000");
=======
>>>>>>> e071d7245a42e36c80b98d795122a86d769e62c4
        m.addAttribute("profil", bp);
    }

    @ModelAttribute("profilliste")
    public void initListe(Model m){
        List<BenutzerProfil> profilliste = service.alleBenutzerProfile();
        m.addAttribute("profilliste", profilliste);
    }


    @GetMapping("benutzerprofil")
    public String getProfilansicht(
                @ModelAttribute("profil") BenutzerProfil profil, 
                Model m,
                Locale locale){
        m.addAttribute("sprache", locale.getDisplayLanguage());
        return "benutzerprofil/profilansicht";
    }

    @GetMapping("benutzerprofil/bearbeiten")
    public String getProfileditor(
        
                @ModelAttribute("profil") BenutzerProfil profil, 
                Model m, 
                Locale locale){
        m.addAttribute("sprache", locale);
        return "benutzerprofil/profileditor";
    }

    @PostMapping("benutzerprofil/bearbeiten")
    public String postProfil(
                @Valid @ModelAttribute("profil") BenutzerProfil profil,
                BindingResult result, 
                Model m){

        if (result.hasErrors()){
            return "benutzerprofil/profileditor";
        }else{
            m.addAttribute("profil", service.speichereBenutzerProfil(profil));
        }
        return "redirect:/benutzerprofil";
    }

    @GetMapping("benutzerprofil/liste")
    public String getListe(
                Model m, 
                Locale locale){
        m.addAttribute("sprache", locale);
        m.addAttribute("profilliste", service.alleBenutzerProfile());
        return "benutzerprofil/profilliste";
    }

    @GetMapping(value="benutzerprofil/liste", params="op=loeschen")
    public String loescheProfil(
                @RequestParam Long id,
                Model m,
                Locale locale){
        m.addAttribute("sprache", locale);
        service.loescheBenutzerProfilMitId(id);
        return "redirect:/benutzerprofil/liste";
    }

    @GetMapping(value="benutzerprofil/liste", params="op=bearbeiten")
    public String bearbeiteProfil(
                @RequestParam Long id, 
                Model m, 
                Locale locale){
        m.addAttribute("sprache", locale);
        if (service.holeBenutzerProfilMitId(id).isPresent()){
            BenutzerProfil pb = service.holeBenutzerProfilMitId(id).get();
            m.addAttribute("profil", pb);
        }
        return "redirect:/benutzerprofil/bearbeiten";
    }

    @GetMapping("benutzerprofil/clearsession")
    public String clearSession(SessionStatus status){
        status.setComplete();
        return "redirect:/benutzerprofil";
    }
}
