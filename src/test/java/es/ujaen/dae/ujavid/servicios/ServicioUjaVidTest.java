/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.servicios;

import es.ujaen.dae.ujavid.entidades.Rastreador;
import es.ujaen.dae.ujavid.entidades.Usuario;
import java.time.LocalDate;
import java.util.Optional;
import javax.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

/**
 *
 * @author Venza
 */
@SpringBootTest(classes = es.ujaen.dae.ujavid.app.UjaVidApp.class)
public class ServicioUjaVidTest {
    @Autowired
    ServicioUjaVid servicioUjaVid;
    
    
    @Test
    public void testAccesoServicioUjaCoin() {
        Assertions.assertThat(servicioUjaVid).isNotNull();
    }
    
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testAltaUsuarioInvalido() {
        // Cliente con e-mail incorrecto!!!
        Usuario usuario = new Usuario(
                "4524",
                "nuevaclave");
        
        Assertions.assertThatThrownBy(() -> {
            servicioUjaVid.altaUsuario(usuario); })
                .isInstanceOf(ConstraintViolationException.class);
    }
    
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    
    public void testAltaRastreadorInvalido() {
        // Cliente con e-mail incorrecto!!!
        Rastreador rastreador = new Rastreador(
                "77434825N",
                "Antonio",
                "Venzala",
                "Campaña",
                "6603760",
                "contraseña1");

        Assertions.assertThatThrownBy(() -> {
            servicioUjaVid.altaRastreador(rastreador); })
                .isInstanceOf(ConstraintViolationException.class);
    }
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testAltaYLoginUsuario() {
        Usuario usuario = new Usuario(
                "660376093",
                "nuevaclave");

        servicioUjaVid.altaUsuario(usuario);
        Optional<Usuario> usuarioLogin = servicioUjaVid.loginUsuario(usuario.getNumTelefono(), "nuevaclave");
        
        Assertions.assertThat(usuarioLogin.isPresent()).isTrue();
        Assertions.assertThat(usuarioLogin.get()).isEqualTo(usuario);
    }
    
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testAltaYLoginRastreaddor() {
        Rastreador rastreador = new Rastreador(
                "77434825N",
                "Antonio",
                "Venzala",
                "Campaña",
                "660376093",
                "contraseña1");

        servicioUjaVid.altaRastreador(rastreador);
        Optional<Rastreador> rastreadorLogin = servicioUjaVid.loginRastreador(rastreador.getDni(), "contraseña1");
        
        Assertions.assertThat(rastreadorLogin.isPresent()).isTrue();
        Assertions.assertThat(rastreadorLogin.get()).isEqualTo(rastreador);
    }
    
    
    
}
