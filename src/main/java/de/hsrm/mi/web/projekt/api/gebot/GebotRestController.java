package de.hsrm.mi.web.projekt.api.gebot;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.hsrm.mi.web.projekt.gebot.Gebot;
import de.hsrm.mi.web.projekt.gebot.GebotRepository;
import de.hsrm.mi.web.projekt.gebot.GebotService;

@RestController
@RequestMapping("/api")
public class GebotRestController {
    
    @Autowired
    GebotRepository gebotRepository;

    @Autowired
    GebotService gebotService;

    @GetMapping(value="/gebot", produces=MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    public List<GetGebotResponseDTO> getGetGebotResponseDTOs() {
        List<GetGebotResponseDTO> gebote = new ArrayList<>();
        for(Gebot g: gebotRepository.findAll()){
            gebote.add(GetGebotResponseDTO.from(g));
        }
        return gebote;
    }

    @PostMapping("/gebot")
    public Long postGebot(@RequestBody AddGebotRequestDTO newGebot){
        return gebotService.bieteFuerAngebot(newGebot.benutzerprofilid(), newGebot.angebotid(), newGebot.betrag()).getId();
    }

    @DeleteMapping("/gebot/{id}")
    public void deleteGebot(@PathVariable Long id){
        gebotService.loescheGebot(id);
    }
}
