/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Proveedor de datos de seguridad de UJaVid
 * @author Venza
 */
public class ServicioSeguridadUjaVid extends WebSecurityConfigurerAdapter {
     @Autowired
    ServicioDatosRastreador servicioDatosRastreador;
     
 @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(servicioDatosRastreador)
            .passwordEncoder(new BCryptPasswordEncoder());
        
        //auth.inMemoryAuthentication()
        //        .withUser("ujacoin").roles("CLIENTE").password("{noop}secret");
    }

       @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable();
        
        httpSecurity.httpBasic();
        
        httpSecurity.authorizeRequests().antMatchers(HttpMethod.POST, "/ujavid/rastreador").anonymous();
      
        
        httpSecurity.authorizeRequests().antMatchers("/ujavid/rastreador/{dni}/**")
                .access("hasRole('RASTREADOR') and #dni == principal.username");
    }

}
