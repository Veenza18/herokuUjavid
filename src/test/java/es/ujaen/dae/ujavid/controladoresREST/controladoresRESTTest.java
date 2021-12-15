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
    public void testAltaUsuarioInvalido() {
        // Usuario con Nº de teléfono mal

        DTOUsuario usuario = new DTOUsuario("656749","adrian123");

        ResponseEntity<UUID> respuesta = restTemplate.postForEntity(
                "/usuarios",
                usuario,
                UUID.class
        );
        System.out.println("----------------------------- " + restTemplate.getRootUri());
        Assertions.assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    /**
     * Intento de creación de un rastreador inválido
     */
    @Test
    public void testAltaRsstreadorInvalido() {
        // Usuario con Nº de teléfono mal

        DTORastreador rastreador = new DTORastreador("27656A", "Adrian", "Perez", "Sanchez", "655656565", "jaen");

        //this.restTemplateBuilder.basicAuthentication(usuario.getNumTelefono(), usuario.getPassword());

        ResponseEntity<UUID> respuesta = restTemplate.postForEntity(
                "/rastreadores",
                rastreador,
                UUID.class
        );

        Assertions.assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
