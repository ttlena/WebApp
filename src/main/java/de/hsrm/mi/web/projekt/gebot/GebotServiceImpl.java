package de.hsrm.mi.web.projekt.gebot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.hsrm.mi.web.projekt.angebot.Angebot;
import de.hsrm.mi.web.projekt.api.gebot.GetGebotResponseDTO;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilService;
import de.hsrm.mi.web.projekt.messaging.BackendInfoService;
import de.hsrm.mi.web.projekt.messaging.BackendOperation;

@Service
public class GebotServiceImpl implements GebotService{

    @Autowired
    private GebotRepository gebotRepository;
    
    @Autowired
    private BenutzerprofilService benutzerprofilService;

    @Autowired
    private BackendInfoService backendInfoService;

    @Override
    public List<Gebot> findeAlleGebote() {
        return gebotRepository.findAll();
    }

    @Override
    public List<Gebot> findeAlleGeboteFuerAngebot(long angebotid) {
        Angebot angebot = benutzerprofilService.findeAngebotMitId(angebotid).orElseThrow();
        return angebot.getGebote();
    }

    @Override
    public Gebot bieteFuerAngebot(long benutzerprofilid, long angebotid, long betrag) {
        Optional<Gebot> gebot = gebotRepository.findByAngebotIdAndBieterId(angebotid, benutzerprofilid); 
        if(gebot.isEmpty()){
            Gebot neuesGebot = new Gebot();
            neuesGebot.setAngebot(benutzerprofilService.findeAngebotMitId(angebotid).orElseThrow());
            neuesGebot.setGebieter(benutzerprofilService.holeBenutzerProfilMitId(benutzerprofilid).orElseThrow());
            neuesGebot.setBetrag(betrag);
            neuesGebot.setGebotzeitpunkt(LocalDateTime.now());
            benutzerprofilService.findeAngebotMitId(angebotid).orElseThrow().setGebot(neuesGebot);
            benutzerprofilService.holeBenutzerProfilMitId(benutzerprofilid).orElseThrow().setGebot(neuesGebot);
            gebotRepository.save(neuesGebot);
            //Gebot-DTO senden?
            GetGebotResponseDTO gebotDTO = GetGebotResponseDTO.from(neuesGebot);
            backendInfoService.sendInfo(String.format("/gebot/%d",gebotDTO.angebotid()), BackendOperation.CREATE, gebotDTO.gebotid());
            return neuesGebot;
        }else{
            gebot.get().setBetrag(betrag);
            gebot.get().setGebotzeitpunkt(LocalDateTime.now());
            GetGebotResponseDTO gebotDTO = GetGebotResponseDTO.from(gebot.get());
            backendInfoService.sendInfo(String.format("/gebot/%d",gebotDTO.angebotid()), BackendOperation.UPDATE, gebotDTO.gebotid());
            return gebot.get();
        }
    }

    @Override
    public void loescheGebot(long gebotid) {
        Gebot gebot = gebotRepository.findById(gebotid).orElseThrow();
        Angebot angebot = gebot.getAngebot();
        angebot.getGebote().remove(gebot);
        BenutzerProfil bieter = gebot.getGebieter();
        bieter.getGebote().remove(gebot);
        GetGebotResponseDTO gebotDTO = GetGebotResponseDTO.from(gebot);
        backendInfoService.sendInfo(String.format("/gebot/%d",gebotDTO.angebotid()), BackendOperation.DELETE, gebotDTO.gebotid());
        gebotRepository.deleteById(gebotid);
    }
    
}
