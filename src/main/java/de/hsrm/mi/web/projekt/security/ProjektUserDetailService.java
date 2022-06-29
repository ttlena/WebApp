package de.hsrm.mi.web.projekt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.hsrm.mi.web.projekt.projektuser.ProjektUser;
import de.hsrm.mi.web.projekt.projektuser.ProjektUserRepository;

@Service
public class ProjektUserDetailService implements UserDetailsService {

    @Autowired
    private ProjektUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ProjektUser user = userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.getPassword()) // falls in DB encoded gespeichert
                .roles("USER") // Rolle könnte auch aus DB kommen
                .build();
    }
}
