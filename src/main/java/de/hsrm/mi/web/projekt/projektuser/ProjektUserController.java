package de.hsrm.mi.web.projekt.projektuser;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ProjektUserController {
    @Autowired
    ProjektUserService service;

    @ModelAttribute("projektuser")
    public void initUser(Model m){
        ProjektUser user = new ProjektUser();
        m.addAttribute("projektuser", user);
    }

    @GetMapping("registrieren")
    public String getRegistrieren(Model m) {
        return "projektuser/registrieren";
    }

    @PostMapping("registrieren")
    public String postProjektUser(
                @Valid @ModelAttribute("projektuser") ProjektUser user,
                BindingResult result,
                Model m) {
        if(result.hasErrors()) {
            return "projektuser/registrieren";
        }else {
            m.addAttribute("user", service.neuenBenutzerAnlegen(user.getUsername(), user.getPassword(), user.getRole()));
        }
        return "redirect:/login";
    }
}
