// package de.hsrm.mi.web.projekt.security;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
// import org.springframework.security.crypto.factory.PasswordEncoderFactories;
// import org.springframework.security.crypto.password.PasswordEncoder;

// @Configuration
// @EnableWebSecurity
// public class ProjektSecurityConfig extends WebSecurityConfigurerAdapter {
//     @Autowired
//     ProjektUserDetailService projektUserDetailService;

//     @Bean
//     PasswordEncoder passwordEncoder() {
//         return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//     }

//     @Override
//     protected void configure(AuthenticationManagerBuilder authmanagerbuilder) throws Exception {
//         authmanagerbuilder
//         .userDetailsService(projektUserDetailService)
//         .passwordEncoder(passwordEncoder());
//     }

//     @Override
//     protected void configure(HttpSecurity http) throws Exception {
//         http.authorizeRequests()
//                 .antMatchers("/registrieren").permitAll()
//                 .antMatchers("/login").permitAll()
//                 .antMatchers("/h2-console/**").permitAll()
//                 .antMatchers("/api/**").permitAll()
//                 .antMatchers("/topic/**").permitAll()
//                 .antMatchers("/stompbroker").permitAll()
//                 .anyRequest().authenticated()
//             .and()
//                 .formLogin()
//                 .defaultSuccessUrl("/benutzerprofil")
//                 .permitAll()
//             .and()
//                 .httpBasic()
//             .and()
//                 .csrf().ignoringAntMatchers("/h2-console/**")
//             .and()
//                 .headers().frameOptions().disable();
//     }
// }
