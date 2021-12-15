/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujavid.controladoresREST;

import es.ujaen.dae.ujavid.controladoresREST.DTO.DTORastreador;
import es.ujaen.dae.ujavid.controladoresREST.DTO.DTOUsuario;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;

/**
 *
 * @author Juan José
 */
@SpringBootTest(classes = es.ujaen.dae.ujavid.app.UjaVidApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = {"test"})
public class controladoresRESTTest {

    @LocalServerPort
    int localPort;

    @Autowired
    MappingJackson2HttpMessageConverter springBootJacksonConverter;

    TestRestTemplate restTemplate;

    /**
     * Crear un TestRestTemplate para las pruebas
     */
    @PostConstruct
    void crearRestTemplateBuilder() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                .rootUri("http://localhost:" + localPort + "/ujavid")
                .additionalMessageConverters(List.of(springBootJacksonConverter));
        restTemplate = new TestRestTemplate(restTemplateBuilder);
    }

    /**
     * Intento de creación de un cliente inválido
     */
    @Test
    public void testAltaUsuario() {
        // Usuario con Nº de teléfono mal
        DTOUsuario usuario = new DTOUsuario("656749","adrian123");

        ResponseEntity<UUID> respuesta = restTemplate.postForEntity(
                "/usuarios",
                usuario,
                UUID.class
        );

        Assertions.assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        
        // Usuario correcto
        DTOUsuario usuario2 = new DTOUsuario("655151515","adrian123");

        ResponseEntity<UUID> respuesta2 = restTemplate.postForEntity(
                "/usuarios",
                usuario2,
                UUID.class
        );

        Assertions.assertThat(respuesta2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
    /**
     * Intento de creación de un rastreador inválido
     */
    @Test
    public void testAltaRastreadorInvalido() {
        // Rstreador con DNI mal
        DTORastreador rastreador = new DTORastreador("27656A", "Adrian", "Perez", "Sanchez", "655656565", "jaen");

        ResponseEntity<UUID> respuesta = restTemplate.postForEntity(
                "/rastreadores",
                rastreador,
                UUID.class
        );

        Assertions.assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        
        // Rstreador CORRECTO
        DTORastreador rastreador2 = new DTORastreador("27878765G", "Adrian", "Perez", "Sanchez", "655656565", "jaen");

        ResponseEntity<UUID> respuesta2 = restTemplate.postForEntity(
                "/rastreadores",
                rastreador2,
                UUID.class
        );

        Assertions.assertThat(respuesta2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
    
     /**
     * test de alta y login de un rastreador
     */
    @Test
    public void testAltaYAccesoDatosRastreador() {
        // El DTO creado no tendrá la contraseña cifrada cuando hagas el post
        DTORastreador rastreador = new DTORastreador("27090987G", "Adrian", "Perez", "Sanchez", "655656565", "secret");

         ResponseEntity<UUID> respuestaAlta = restTemplate.postForEntity(
                "/rastreadores",
                rastreador,
                UUID.class
        );

        Assertions.assertThat(respuestaAlta.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<DTORastreador> respuestaLogin = this.restTemplate.withBasicAuth(rastreador.getDni(), rastreador.getPassword())
                .getForEntity(
                        "/rastreadores/{dni}",
                        DTORastreador.class,
                        rastreador.getDni()
                );

        Assertions.assertThat(respuestaLogin.getStatusCode()).isEqualTo(HttpStatus.OK);

        // EL DTORastreador este tendrá la contraseña cifrada
        DTORastreador clienteLogin = respuestaLogin.getBody();
        Assertions.assertThat(clienteLogin.getDni()).isEqualTo(rastreador.getDni());
    }
    
//     @Test
//    public void testRealizarContacto() {
//        
//    }
}
