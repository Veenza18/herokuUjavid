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
public class UsuarioTest {
      public UsuarioTest() {
    }

     @Test
    void testValidacionUsuario() {

        Usuario usuario = new Usuario(
                "+34653716415",
                "juanjo_pass"
        );
        
        Validator validador = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Usuario>> violations = validador.validate(usuario);
                
        Assertions.assertThat(violations).isEmpty();}
    
     @Test
    void testComprobacionClave() {
        String clave = "jjpg0006";
        String clave2 = "asdsa";
        
        Usuario usuario = new Usuario(
                "+34653716415",
                clave
        );
        
        Assertions.assertThat(usuario.passwordValida(clave)).isTrue();
        Assertions.assertThat(usuario.passwordValida(clave2)).isFalse();
    }
}
