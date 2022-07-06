package de.hsrm.mi.web.projekt.gebot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import de.hsrm.mi.web.projekt.angebot.Angebot;
import de.hsrm.mi.web.projekt.api.gebot.GetGebotResponseDTO;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilService;
import de.hsrm.mi.web.projekt.messaging.BackendInfoService;
import de.hsrm.mi.web.projekt.messaging.BackendOperation;

@Service
public class GebotServiceImpl implements GebotService{
    Logger logger = LoggerFactory.getLogger(GebotServiceImpl.class);
    
    @Autowired
    private GebotRepository gebotRepository;
    
    @Autowired
    private BenutzerprofilService benutzerprofilService;

    @Autowired
    private BackendInfoService backendInfoService;

    @Autowired
    private SimpMessagingTemplate messaging;

    @Override
    public List<Gebot> findeAlleGebote() {
        return gebotRepository.findAll();
    }

    @Override
    public List<Gebot> findeAlleGeboteFuerAngebot(long angebotid) {
        Angebot angebot = benutzerprofilService.findeAngebotMitId(angebotid).orElseThrow();
        return angebot.getGebote();
    }

    @Transactional
    @Override
    public Gebot bieteFuerAngebot(long benutzerprofilid, long angebotid, long betrag) {
        Optional<Gebot> gebot = gebotRepository.findByAngebotIdAndBieterId(angebotid, benutzerprofilid); 
        if(gebot.isEmpty()){
            logger.info("neuer Bieter fuer dieses Angebot");
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
            messaging.convertAndSend("/topic/gebot/"+neuesGebot.getAngebot().getId(), gebotDTO);
            //backendInfoService.sendInfo(String.format("/gebot/%d",gebotDTO.angebotid()), BackendOperation.CREATE, gebotDTO.angebotid());
            return neuesGebot;
        }else{
            logger.info("Gebot update");
            gebot.get().setBetrag(betrag);
            gebot.get().setGebotzeitpunkt(LocalDateTime.now());
            GetGebotResponseDTO gebotDTO = GetGebotResponseDTO.from(gebot.get());
            messaging.convertAndSend("/topic/gebot/"+gebot.get().getAngebot().getId(), gebotDTO);
            //backendInfoService.sendInfo(String.format("/gebot/%d",gebotDTO.angebotid()), BackendOperation.UPDATE, gebotDTO.angebotid());
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
