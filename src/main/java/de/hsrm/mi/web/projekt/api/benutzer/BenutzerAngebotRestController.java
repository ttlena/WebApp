package de.hsrm.mi.web.projekt.api.benutzer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.hsrm.mi.web.projekt.angebot.Angebot;
import de.hsrm.mi.web.projekt.angebot.AngebotRepository;
import de.hsrm.mi.web.projekt.api.gebot.GetGebotResponseDTO;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilRepository;
import de.hsrm.mi.web.projekt.gebot.Gebot;

@RestController
@RequestMapping("/api")
public class BenutzerAngebotRestController {
    
    @Autowired
    AngebotRepository angebotRepository;

    @Autowired
    BenutzerprofilRepository benutzerprofilRepository;

    @GetMapping(value="/angebot", produces=MediaType.APPLICATION_JSON_VALUE)
    public List<GetAngebotResponseDTO> getGetAngebotResponseDTOs(){
        List<GetAngebotResponseDTO> angebote = new ArrayList<>();
        for (Angebot a: angebotRepository.findAll()){
            angebote.add(GetAngebotResponseDTO.from(a));
        }
        return angebote;
    }

    @GetMapping("/angebot/{id}")
    public GetAngebotResponseDTO getGetAngebotResponseDTO(@PathVariable("id") long angebotid){
        return GetAngebotResponseDTO.from(angebotRepository.getById(angebotid));
    }

    @GetMapping("/angebot/{id}/gebot")
    public List<GetGebotResponseDTO> getGebotResponseDTOsVonAngebot(@PathVariable("id") long angebotid){
        List<GetGebotResponseDTO> gebote = new ArrayList<>();
        for (Gebot g: angebotRepository.findById(angebotid).get().getGebote()){
            gebote.add(GetGebotResponseDTO.from(g));
        }
        return gebote;
    }

    @GetMapping("/benutzer/{id}/angebot")
    public List<GetAngebotResponseDTO> getAngebotResponseDTOVonBenutzerprofil(@PathVariable("id") long benutzerprofilid){
        List<GetAngebotResponseDTO> angebote = new ArrayList<>();
        for (Angebot a: benutzerprofilRepository.findById(benutzerprofilid).get().getAngebote()){
            angebote.add(GetAngebotResponseDTO.from(a));
        }
        return angebote;
    }

}

