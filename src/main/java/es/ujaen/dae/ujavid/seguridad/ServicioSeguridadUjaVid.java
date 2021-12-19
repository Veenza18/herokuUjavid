/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasRole;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Proveedor de datos de seguridad de UJaVid
 *
 * @author Venza
 */
@Configuration
public class ServicioSeguridadUjaVid extends WebSecurityConfigurerAdapter {

    @Autowired
    ServicioDatosRastreador servicioDatosRastreador;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(servicioDatosRastreador)
                .passwordEncoder(new BCryptPasswordEncoder());

//        auth.inMemoryAuthentication()
//                .withUser("ujavid").roles("RASTREADOR").password("{noop}secret");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // Evitar crossSite request forgery
        httpSecurity.csrf().disable();
    
        // Activamos la autenticación básica
        httpSecurity.httpBasic();

        //Definimos nuestras políticas de seguridad desde más específico a más general
         httpSecurity.authorizeRequests().antMatchers(HttpMethod.POST,"/ujavid/usuarios/{uuid}/notificaciones/*").hasRole("RASTREADOR");       
         httpSecurity.authorizeRequests().antMatchers(HttpMethod.GET,"/ujavid/usuarios/{uuid}/**").hasRole("RASTREADOR");
          httpSecurity.authorizeRequests().antMatchers("/ujavid/rastreadores/{dni}/**")
                .access("hasRole('RASTREADOR') and #dni == principal.username");
         httpSecurity.authorizeRequests().antMatchers(HttpMethod.POST,"/ujavid/usuarios/{uuid}/**").permitAll();
         httpSecurity.authorizeRequests().antMatchers(HttpMethod.POST,"/ujavid/usuarios").permitAll();
         httpSecurity.authorizeRequests().antMatchers("/ujavid/estadisticas/**").permitAll();
         httpSecurity.authorizeRequests().antMatchers("/ujavid/rastreadores").permitAll();
      
    }

}
