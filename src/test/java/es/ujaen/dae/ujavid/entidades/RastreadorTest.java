/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujavid.entidades;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Juan José
 */
public class RastreadorTest {
    
     public RastreadorTest() {
    }

     /**
     * Valida la creación de un rastreador
     */
     @Test
    void testValidacionRastreador() {
        
        Rastreador rastreador = new Rastreador(
                "26523700P", 
                "Juan José", 
                "Peiró",
                "Garrido",
                "+34656764549",
                "jjpg0006");
        
        Validator validador = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Rastreador>> violations = validador.validate(rastreador);
                
        Assertions.assertThat(violations).isEmpty();}

    
     /**
     * Valida la comprobación de la clave en un rastreador
     */
     @Test
    void testComprobacionClave() {
        String clave = "jjpg0006";
        
         Rastreador rastreador = new Rastreador(
                "26523700P", 
                "Juan José", 
                "Peiró",
                "Garrido",
                "+34656764549",
                clave);
        
        Assertions.assertThat(rastreador.passwordValida(clave)).isTrue();
    }

    
    
}
