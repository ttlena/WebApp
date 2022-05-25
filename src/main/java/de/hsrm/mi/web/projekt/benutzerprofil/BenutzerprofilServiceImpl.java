package de.hsrm.mi.web.projekt.benutzerprofil;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.hsrm.mi.web.projekt.angebot.Angebot;
import de.hsrm.mi.web.projekt.angebot.AngebotRepository;
import de.hsrm.mi.web.projekt.geo.AdressInfo;
import de.hsrm.mi.web.projekt.geo.GeoService;

@Service
@Transactional
public class BenutzerprofilServiceImpl implements BenutzerprofilService{
    Logger logger = LoggerFactory.getLogger(BenutzerprofilService.class);
    
    @Autowired
    private BenutzerprofilRepository benutzerProfilRepository;
    @Autowired
    private AngebotRepository angebotRepository;
    @Autowired
    private GeoService geoService;

    @Override
    public BenutzerProfil speichereBenutzerProfil(BenutzerProfil bp) {
        List<AdressInfo> adressInfos = geoService.findeAdressInfo(bp.getAdresse());
        if(adressInfos.size() > 0){
            AdressInfo adressInfo = adressInfos.get(0);
            bp.setLat(adressInfo.lat());
            bp.setLon(adressInfo.lon());
        }
        return benutzerProfilRepository.save(bp);
    }

    @Override
    public Optional<BenutzerProfil> holeBenutzerProfilMitId(Long id) {
        for (BenutzerProfil bp: benutzerProfilRepository.findAll()){
            if (bp.getId() == id){
                return Optional.of(bp);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<BenutzerProfil> alleBenutzerProfile() {
        return benutzerProfilRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public void loescheBenutzerProfilMitId(Long loesch) {
        benutzerProfilRepository.deleteById(loesch);
    }

    @Override
    public void fuegeAngebotHinzu(long id, Angebot angebot) {
       List<AdressInfo> adressen = geoService.findeAdressInfo(angebot.getAbholort());
       if (!adressen.isEmpty()){
           AdressInfo abholort = adressen.get(0);
           angebot.setLat(abholort.lat());
           angebot.setLon(abholort.lon());
       }else{
        angebot.setLat(0);
        angebot.setLon(0);
       }
       BenutzerProfil benutzerprofil = holeBenutzerProfilMitId(id).orElseThrow();
       angebot.setAnbieter(benutzerprofil);
       benutzerprofil.getAngebote().add(angebot);
       logger.info("Angebotsanzahl: " + benutzerprofil.getAngebote().size());
       for (Angebot ang: benutzerprofil.getAngebote()){
           logger.info(ang.getBeschreibung());
       }
    }

    @Override
    public void loescheAngebot(long id) {
        BenutzerProfil anbieter = angebotRepository.getById(id).getAnbieter();
        anbieter.getAngebote().remove(angebotRepository.getById(id));
        angebotRepository.deleteById(id);
    }
    
}
