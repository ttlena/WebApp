package de.hsrm.mi.web.projekt.projektuser;

import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilService;

@Service
public class ProjektUserServiceImpl implements ProjektUserService {
    Logger logger = LoggerFactory.getLogger(ProjektUserServiceImpl.class);
    
    @Autowired
    private PasswordEncoder pwenc;

    @Autowired
    private ProjektUserRepository projektUserRepository;

    @Autowired
    private BenutzerprofilService benutzerprofilService;
    
    @Override
    @Transactional
    public ProjektUser neuenBenutzerAnlegen(String username, String klartextpasswort, String rolle) {
        if (rolle == null || rolle.equalsIgnoreCase("")){
            rolle = "USER";
        }
        ProjektUser newUser = new ProjektUser(username, pwenc.encode(klartextpasswort), rolle);
        if (projektUserRepository.existsById(username)) {
            logger.error("ProjektUser existiert schon");
            throw new ProjektUserServiceException("ProjektUser existiert schon");
        }
        BenutzerProfil benutzerProfil = new BenutzerProfil();
        benutzerProfil.setName(username);
        benutzerProfil.setProjektUser(projektUserRepository.save(newUser));
        newUser.setBenutzerProfil(benutzerprofilService.speichereBenutzerProfil(benutzerProfil));
        return projektUserRepository.save(newUser);
    }

    @Override
    public ProjektUser findeBenutzer(String username) {
        Optional<ProjektUser> projektUser = projektUserRepository.findById(username);
        if (!projektUser.isEmpty()){
            return projektUser.get();
        }else {
            logger.error("ProjektUser nicht im Repo gefunden");
            throw new ProjektUserServiceException("ProjektUser nicht gefunden");
        }
    }
}
