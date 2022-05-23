package de.hsrm.mi.web.projekt.benutzerprofil;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import de.hsrm.mi.web.projekt.geo.AdressInfo;
import de.hsrm.mi.web.projekt.geo.GeoService;

@Service
public class BenutzerprofilServiceImpl implements BenutzerprofilService{
    Logger logger = LoggerFactory.getLogger(BenutzerprofilService.class);
    
    @Autowired
    private BenutzerprofilRepository repository;
    @Autowired
    private GeoService geoService;

    @Override
    public BenutzerProfil speichereBenutzerProfil(BenutzerProfil bp) {
        logger.info(bp.toString());
        List<AdressInfo> adressInfos = geoService.findeAdressInfo(bp.getAdresse());
        if(adressInfos.size() > 0){
            AdressInfo adressInfo = adressInfos.get(0);
            bp.setLat(adressInfo.lat());
            bp.setLon(adressInfo.lon());
        }
        logger.info(bp.toString());
        return repository.save(bp);
    }

    @Override
    public Optional<BenutzerProfil> holeBenutzerProfilMitId(Long id) {
        for (BenutzerProfil bp: repository.findAll()){
            if (bp.getId() == id){
                return Optional.of(bp);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<BenutzerProfil> alleBenutzerProfile() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public void loescheBenutzerProfilMitId(Long loesch) {
        repository.deleteById(loesch);
    }
    
}
