package de.hsrm.mi.web.projekt.benutzerprofil;

import java.security.Principal;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import de.hsrm.mi.web.projekt.angebot.Angebot;
import de.hsrm.mi.web.projekt.messaging.BackendInfoService;
import de.hsrm.mi.web.projekt.messaging.BackendOperation;
import de.hsrm.mi.web.projekt.projektuser.ProjektUserService;

@Controller
@RequestMapping("/")
@SessionAttributes(names={"profil", "profilliste"})
public class BenutzerprofilController {
    Logger logger = LoggerFactory.getLogger(BenutzerprofilController.class);
    
    @Autowired
    BenutzerprofilService benutzerprofilService;

    @Autowired
    ProjektUserService projektUserService;

    @Autowired
    BackendInfoService backendInfoService;

    @ModelAttribute("profil")
    public void initProfil(Model m, Principal principal){
        BenutzerProfil benutzerProfil = new BenutzerProfil();
        if(principal != null) {
            logger.info("Principal: " + principal.toString());
            benutzerProfil = projektUserService.findeBenutzer(principal.getName()).getBenutzerProfil();
            if(benutzerProfil == null)
                benutzerProfil = new BenutzerProfil();
        }
        m.addAttribute("profil", benutzerProfil);
    }

    @ModelAttribute("profilliste")
    public void initListe(Model m){
        List<BenutzerProfil> profilliste = benutzerprofilService.alleBenutzerProfile();
        m.addAttribute("profilliste", profilliste);
    }

    @GetMapping("benutzerprofil/clearsession")
    public String clearSession(SessionStatus status){
        status.setComplete();
        return "redirect:/benutzerprofil";
    }

    @GetMapping("benutzerprofil")
    public String getProfilansicht(
                @ModelAttribute("profil") BenutzerProfil profil, 
                Model m,
                Locale locale){
        m.addAttribute("sprache", locale);
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
            m.addAttribute("profil", benutzerprofilService.speichereBenutzerProfil(profil));
        }
        return "redirect:/benutzerprofil";
    }

    @GetMapping("benutzerprofil/liste")
    public String getListe(
                Model m, 
                Locale locale){
        m.addAttribute("sprache", locale);
        m.addAttribute("profilliste", benutzerprofilService.alleBenutzerProfile());
        return "benutzerprofil/profilliste";
    }

    @GetMapping(value="benutzerprofil/liste", params="op=loeschen")
    public String loescheProfil(
                @RequestParam Long id,
                Model m,
                Locale locale){
        m.addAttribute("sprache", locale);
        benutzerprofilService.loescheBenutzerProfilMitId(id);
        return "redirect:/benutzerprofil/liste";
    }

    @GetMapping(value="benutzerprofil/liste", params="op=bearbeiten")
    public String bearbeiteProfil(
                @RequestParam Long id, 
                Model m, 
                Locale locale){
        m.addAttribute("sprache", locale);
        if (benutzerprofilService.holeBenutzerProfilMitId(id).isPresent()){
            BenutzerProfil pb = benutzerprofilService.holeBenutzerProfilMitId(id).get();
            m.addAttribute("profil", pb);
        }
        return "redirect:/benutzerprofil/bearbeiten";
    }
    
    @GetMapping("benutzerprofil/angebot")
    public String erstelleNeuesAngebot(Model m, Locale locale){
        m.addAttribute("sprache", locale);
        m.addAttribute("angebot", new Angebot());
        return "benutzerprofil/angebotsformular";
    }

    @PostMapping("benutzerprofil/angebot")
    public String postAngebot(
                    @SessionAttribute("profil") BenutzerProfil profil,
                    @ModelAttribute("angebot") Angebot angebot, 
                    Model m){
        benutzerprofilService.fuegeAngebotHinzu(profil.getId(), angebot);
        m.addAttribute("profil", benutzerprofilService.holeBenutzerProfilMitId(profil.getId()));
        backendInfoService.sendInfo("/angebot", BackendOperation.CREATE, angebot.getId());
        return "redirect:/benutzerprofil";
    }

    @GetMapping("benutzerprofil/angebot/{id}/del")
    public String loescheAngebot(
                    @PathVariable Long id,
                    @SessionAttribute("profil") BenutzerProfil profil,
                    Model m){
        benutzerprofilService.loescheAngebot(id);
        m.addAttribute("profil", benutzerprofilService.holeBenutzerProfilMitId(profil.getId()).get());
        backendInfoService.sendInfo("/angebot", BackendOperation.DELETE, id);
        return "redirect:/benutzerprofil";
    }
}
