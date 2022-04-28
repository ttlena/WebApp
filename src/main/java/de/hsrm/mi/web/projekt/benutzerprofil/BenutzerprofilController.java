package de.hsrm.mi.web.projekt.benutzerprofil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("")
@SessionAttributes("profil")
public class BenutzerprofilController {
    Logger logger = LoggerFactory.getLogger(BenutzerprofilController.class);
    
    @ModelAttribute
    public void addAttributes(Model m){
        m.addAttribute("name", "Joenhard Biffel");
        m.addAttribute("geburtsdatum", "01.01.2010");
    }

    @GetMapping("/benutzerprofil")
    public String getProfilansicht(@ModelAttribute("profil") BenutzerProfil profil, Model m){
        m.addAttribute("name", profil.getName());
        m.addAttribute("geburtsdatum", profil.getGeburtsdatum());
        m.addAttribute("adresse", profil.getAdresse());
        m.addAttribute("eMail", profil.getEmail());
        m.addAttribute("lieblingsfarbe", profil.getLieblingsfarbe());
        m.addAttribute("interessenliste", profil.getInteressenListe());
        
        return "benutzerprofil/profilansicht";
    }
}
