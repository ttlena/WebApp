package de.hsrm.mi.web.projekt.benutzerprofil;

import java.time.LocalDate;

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
@SessionAttributes(names={"profil"})
public class BenutzerprofilController {
    Logger logger = LoggerFactory.getLogger(BenutzerprofilController.class);
    
    @ModelAttribute
    public void initProfil(Model m){
        BenutzerProfil bp = new BenutzerProfil();
        bp.setName("Peter");
        bp.setGeburtsdatum(LocalDate.now());
        bp.setEmail("ha@lo.lo");
        bp.setAdresse("Goldgasse 10, 65185 Wiesbaden");
        bp.setInteressen("schwimmen, lesen, schreiben");
        m.addAttribute("profil", bp);
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

    @GetMapping("/benutzerprofil/bearbeiten")
    public String getProfileditor(@ModelAttribute("profil") BenutzerProfil profil, Model m){
        return "benutzerprofil/profileditor";
    }
}
