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
import org.springframework.test.annotation.DirtiesContext;
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
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testAltaUsuario() {
        // Usuario con Nº de teléfono mal
        DTOUsuario usuario = new DTOUsuario("656749", "adrian123");

        ResponseEntity<UUID> respuesta = restTemplate.postForEntity(
                "/usuarios",
                usuario,
                UUID.class
        );

        Assertions.assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        // Usuario correcto
        DTOUsuario usuario2 = new DTOUsuario("655151515", "adrian123");

        ResponseEntity<UUID> respuesta2 = restTemplate.postForEntity(
                "/usuarios",
                usuario2,
                UUID.class
        );

        Assertions.assertThat(respuesta2.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Usuario Repetido
        ResponseEntity<UUID> respuesta3 = restTemplate.postForEntity(
                "/usuarios",
                usuario2,
                UUID.class
        );

        Assertions.assertThat(respuesta3.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    /**
     * Intento de creación de un rastreador inválido
     */
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
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
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
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

    
    /**
     * test de alta y login de un rastreador
     */
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void AccesoDatosRastreadorDistinto() {
        
        // Creamos el primer rastreador
       DTORastreador rastreador = new DTORastreador("27090987G", "Adrian", "Perez", "Sanchez", "655656565", "secret");

       restTemplate.postForEntity(
                "/rastreadores",
                rastreador,
                UUID.class
        );

      DTORastreador rastreador2 = new DTORastreador("26523700P", "Juan José", "Peiró", "Sanchez", "653653653", "password");

      // Creamos el segundo rastreador
       restTemplate.postForEntity(
                "/rastreadores",
                rastreador2,
                UUID.class
        );
       
       //Intentamos acceder a las credenciales del segundo rastreador
       //estando logueados como el primer rastreador
        ResponseEntity<DTORastreador> respuestaLogin = this.restTemplate.withBasicAuth(rastreador.getDni(), rastreador.getPassword())
                .getForEntity(
                        "/rastreadores/{dni}",
                        DTORastreador.class,
                        rastreador2.getDni()
                );

        Assertions.assertThat(respuestaLogin.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);


    }
    
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testRealizarContacto() {
        // Añadimos los distintos Usuarios al sistema
        DTOUsuario usuario1 = new DTOUsuario("655151515", "adrian123");

        ResponseEntity<UUID> respuesta1 = restTemplate.postForEntity(
                "/usuarios",
                usuario1,
                UUID.class
        );

        DTOUsuario usuario2 = new DTOUsuario("600987656", "secret2");

        ResponseEntity<UUID> respuesta2 = restTemplate.postForEntity(
                "/usuarios",
                usuario2,
                UUID.class
        );

        DTOUsuario usuario3 = new DTOUsuario("687394093", "secret5");

        ResponseEntity<UUID> respuesta3 = restTemplate.postForEntity(
                "/usuarios",
                usuario3,
                UUID.class
        );

        Assertions.assertThat(respuesta1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(respuesta2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(respuesta3.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Añadimos el Rastreador
    }
    
      @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testNotificarCuracion() {
       // Creamos el primer rastreador
       DTORastreador rastreador = new DTORastreador("27090987G", "Adrian", "Perez", "Sanchez", "655656565", "secret");

       restTemplate.postForEntity(
                "/rastreadores",
                rastreador,
                UUID.class
        );
    
    
      this.restTemplate.withBasicAuth(rastreador.getDni(), rastreador.getPassword())
                .getForEntity(
                        "/rastreadores/{dni}",
                        DTORastreador.class,
                        rastreador.getDni()
                );
     
        DTOUsuario usuario2 = new DTOUsuario("655151515", "adrian123");

        restTemplate.postForEntity(
                "/usuarios",
                usuario2,
                UUID.class
        );
        
       ResponseEntity<DTORastreador> respuesta = restTemplate.withBasicAuth(rastreador.getDni(), rastreador.getPassword()).postForEntity("/usuarios/{uuidUsuario}/notificacion",
                rastreador, DTORastreador.class,usuario2.getUuid());
       
       Assertions.assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
   
    
}
