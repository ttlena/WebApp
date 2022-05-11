package de.hsrm.mi.web.projekt.benutzerprofil;

import java.util.Locale;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
// @Validated //Ueberpruefung aktivieren
@RequestMapping("/")
@SessionAttributes(names={"profil"})
public class BenutzerprofilController {
    Logger logger = LoggerFactory.getLogger(BenutzerprofilController.class);
    
    @ModelAttribute("profil")
    public void initProfil(Model m){
        BenutzerProfil bp = new BenutzerProfil();
        m.addAttribute("profil", bp);
    }

    @GetMapping("benutzerprofil")
    public String getProfilansicht(@ModelAttribute("profil") BenutzerProfil profil, Model m){
        return "benutzerprofil/profilansicht";
    }

    @GetMapping("benutzerprofil/bearbeiten")
    public String getProfileditor(
        @ModelAttribute("profil") BenutzerProfil profil, 
        Model m, 
        Locale locale){
        return "benutzerprofil/profileditor";
    }

    @PostMapping("benutzerprofil/bearbeiten")
    public String postProfil(
                @Valid @ModelAttribute("profil") BenutzerProfil profil,
                BindingResult result, 
                Model m){

        if (result.hasErrors()){
            return "benutzerprofil/profileditor";
        }
        
        return "redirect:/benutzerprofil";
    }
}
