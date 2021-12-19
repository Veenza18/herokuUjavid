/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujavid.controladoresREST;

import es.ujaen.dae.ujavid.controladoresREST.DTO.DTOContactoCercano;
import es.ujaen.dae.ujavid.controladoresREST.DTO.DTORastreador;
import es.ujaen.dae.ujavid.controladoresREST.DTO.DTOUsuario;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * @author admin
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
     * Test para dar de alta un Usuario
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
     * Test para dar de alta un rastreador
     */
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testAltaRastreador() {
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

        // Rastreador Repetido
        ResponseEntity<UUID> respuesta3 = restTemplate.postForEntity(
                "/rastreadores",
                rastreador2,
                UUID.class
        );

        Assertions.assertThat(respuesta3.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

    }

    /**
     * Test de alta y obtención de los datos de un rastreador
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
     * Test de alta y acceso a los datos de un rastreador distintos
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

    /**
     * Test para añadir los contactos de un Usuario
     * Test para ver los contactos de un usuario
     * Test para comprobar los contagiosXUsuario como estadística
     */
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void contactoTestyContagiadosXUsuario() {

        // Creamos los Usuarios
        DTOUsuario usuario1 = new DTOUsuario("655151513", "adrian123");
        DTOUsuario usuario2 = new DTOUsuario("654116512", "juanjose123");
        DTOUsuario usuario3 = new DTOUsuario("656651515", "op1123");
        DTOUsuario usuario4 = new DTOUsuario("612345678", "op1123");

        ResponseEntity<UUID> respuestaUsuario1 = restTemplate.postForEntity(
                "/usuarios",
                usuario1,
                UUID.class
        );

        ResponseEntity<UUID> respuestaUsuario2 = restTemplate.postForEntity(
                "/usuarios",
                usuario2,
                UUID.class
        );

        ResponseEntity<UUID> respuestaUsuario3 = restTemplate.postForEntity(
                "/usuarios",
                usuario3,
                UUID.class
        );

        ResponseEntity<UUID> respuestaUsuario4 = restTemplate.postForEntity(
                "/usuarios",
                usuario4,
                UUID.class
        );

        Assertions.assertThat(respuestaUsuario1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(respuestaUsuario2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(respuestaUsuario3.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(respuestaUsuario4.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Creamos el rastreador
        DTORastreador rastreador = new DTORastreador("27090987G", "Adrian", "Perez", "Sanchez", "655656565", "secret");

        ResponseEntity<UUID> respuestaRastreador = restTemplate.postForEntity(
                "/rastreadores",
                rastreador,
                UUID.class
        );

        Assertions.assertThat(respuestaRastreador.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(respuestaRastreador.getBody()).isNotNull();

        //Creamos la lista de los contactos
        DTOContactoCercano contacto0 = new DTOContactoCercano(LocalDateTime.now().minusDays(1),
                respuestaUsuario2.getBody(), 2.8, 3);
        DTOContactoCercano contacto1 = new DTOContactoCercano(LocalDateTime.now().minusDays(1),
                respuestaUsuario3.getBody(), 1.6, 2);
        DTOContactoCercano contacto2 = new DTOContactoCercano(LocalDateTime.now().minusDays(1),
                respuestaUsuario4.getBody(), 1.2, 10);

        List<DTOContactoCercano> lista1 = new ArrayList<>();
        lista1.add(contacto0);
        lista1.add(contacto1);
        lista1.add(contacto2);

        // Realizamos la insercción de los contactos al usuario 1
        ResponseEntity<Void> respuesta = restTemplate.withBasicAuth(rastreador.getDni(), rastreador.getPassword()).
                postForEntity("/usuarios/{uuid}/contactos",
                        lista1,
                        Void.class,
                        respuestaUsuario1.getBody()
                );

        Assertions.assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Obtenemos la lista de contactos del usuario 1
        ResponseEntity<DTOContactoCercano[]> respuestaListadoContactos = restTemplate.withBasicAuth(rastreador.getDni(), rastreador.getPassword()).
                getForEntity("/usuarios/{uuid}/contactos/{uuidRastreador}",
                        DTOContactoCercano[].class,
                        respuestaUsuario1.getBody(),
                        respuestaRastreador.getBody()
                );

        Assertions.assertThat(respuestaListadoContactos.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(respuestaListadoContactos.getBody().length).isEqualTo(3);
        DTOContactoCercano[] listaContactos = respuestaListadoContactos.getBody();
        // Comprobamos que los contactos están ordenados
        for (int i = 0; i < listaContactos.length - 1; i++) {
            Assertions.assertThat(listaContactos[i].getRiesgo()).isGreaterThanOrEqualTo(listaContactos[i + 1].getRiesgo());

        }

        // Notificamos el positivo del usuario 1 y de los contactos
        restTemplate.withBasicAuth(rastreador.getDni(), rastreador.getPassword()).
                postForEntity("/usuarios/{uuid}/notificaciones/positivo",
                        respuestaRastreador.getBody(),
                        DTOUsuario.class,
                        respuestaUsuario1.getBody()
                );
        restTemplate.withBasicAuth(rastreador.getDni(), rastreador.getPassword()).
                postForEntity("/usuarios/{uuid}/notificaciones/positivo",
                        respuestaRastreador.getBody(),
                        DTOUsuario.class,
                        respuestaUsuario2.getBody()
                );
        restTemplate.withBasicAuth(rastreador.getDni(), rastreador.getPassword()).
                postForEntity("/usuarios/{uuid}/notificaciones/positivo",
                        respuestaRastreador.getBody(),
                        DTOUsuario.class,
                        respuestaUsuario3.getBody()
                );
        restTemplate.withBasicAuth(rastreador.getDni(), rastreador.getPassword()).
                postForEntity("/usuarios/{uuid}/notificaciones/positivo",
                        respuestaRastreador.getBody(),
                        DTOUsuario.class,
                        respuestaUsuario4.getBody()
                );

        // Obtenemos la incidencia acumulada (contagiados por usuario)
        ResponseEntity<Double> resouestaContagiadosXUsuario = restTemplate.
                getForEntity("/estadisticas/infectados/incidencia",
                        Double.class
                );

        // Comprobamos el valor de la incidencia
        System.out.println(resouestaContagiadosXUsuario.getBody());
        Assertions.assertThat(resouestaContagiadosXUsuario.getBody()).isGreaterThan(0);
    }

    /**
     * Método para resgistrar un positivo y su curacion
     */
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testNotificarPositivoCuracion() {
        // Creamos el rastreador
        DTORastreador rastreador = new DTORastreador("27090987G", "Adrian", "Perez", "Sanchez", "655656565", "secret");

        ResponseEntity<UUID> respuestaRastreador = restTemplate.postForEntity(
                "/rastreadores",
                rastreador,
                UUID.class
        );
        Assertions.assertThat(respuestaRastreador.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Creamos el Usuario 
        DTOUsuario usuario2 = new DTOUsuario("655151515", "adrian123");

        ResponseEntity<UUID> respuestaUsuario = restTemplate.postForEntity(
                "/usuarios",
                usuario2,
                UUID.class
        );

        Assertions.assertThat(respuestaUsuario.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(respuestaUsuario.getBody()).isNotNull();

        // Notificamos el positivo
        ResponseEntity<DTOUsuario> respuesta5 = restTemplate.withBasicAuth(rastreador.getDni(), rastreador.getPassword()).
                postForEntity("/usuarios/{uuid}/notificaciones/positivo",
                        respuestaRastreador.getBody(),
                        DTOUsuario.class,
                        respuestaUsuario.getBody()
                );

        // Comprobamos que se devuelve un usuario positivo
        Assertions.assertThat(respuesta5.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(respuesta5.getBody().isPositivo()).isEqualTo(true);
        Assertions.assertThat(respuesta5.getBody().getNumTelefono()).isEqualTo(usuario2.getNumTelefono());

        // Notificamos la curación
        ResponseEntity<DTOUsuario> respuesta3 = restTemplate.withBasicAuth(rastreador.getDni(), rastreador.getPassword()).
                postForEntity("/usuarios/{uuid}/notificaciones/curacion",
                        respuestaRastreador.getBody(),
                        DTOUsuario.class,
                        respuestaUsuario.getBody()
                );

        // Comprobamos que se devuelve un usuario curado
        Assertions.assertThat(respuesta3.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(respuesta3.getBody().isPositivo()).isEqualTo(false);
        Assertions.assertThat(respuesta3.getBody().getNumTelefono()).isEqualTo(usuario2.getNumTelefono());
    }

    /**
     * Test para obtener el número total de infectados
     */
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testTotalInfectados() {
        // Creamos el rastreador
        DTORastreador rastreador = new DTORastreador("27090987G", "Adrian", "Perez", "Sanchez", "655656565", "secret");

        ResponseEntity<UUID> respuestaRastreador = restTemplate.postForEntity(
                "/rastreadores",
                rastreador,
                UUID.class
        );
        Assertions.assertThat(respuestaRastreador.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Creamos el Usuario 1
        DTOUsuario usuario1 = new DTOUsuario("655151515", "adrian123");

        ResponseEntity<UUID> respuestaUsuario1 = restTemplate.postForEntity(
                "/usuarios",
                usuario1,
                UUID.class
        );

        Assertions.assertThat(respuestaUsuario1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(respuestaUsuario1.getBody()).isNotNull();

        // Creamos el Usuario 2
        DTOUsuario usuario2 = new DTOUsuario("688546701", "adrian123");

        ResponseEntity<UUID> respuestaUsuario2 = restTemplate.postForEntity(
                "/usuarios",
                usuario2,
                UUID.class
        );

        Assertions.assertThat(respuestaUsuario2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(respuestaUsuario2.getBody()).isNotNull();

        // Notificamos los positivos
        ResponseEntity<DTOUsuario> respuesta3 = restTemplate.withBasicAuth(rastreador.getDni(), rastreador.getPassword()).
                postForEntity("/usuarios/{uuid}/notificaciones/positivo",
                        respuestaRastreador.getBody(),
                        DTOUsuario.class,
                        respuestaUsuario1.getBody()
                );

        ResponseEntity<DTOUsuario> respuesta4 = restTemplate.withBasicAuth(rastreador.getDni(), rastreador.getPassword()).
                postForEntity("/usuarios/{uuid}/notificaciones/positivo",
                        respuestaRastreador.getBody(),
                        DTOUsuario.class,
                        respuestaUsuario2.getBody()
                );
        Assertions.assertThat(respuesta3.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(respuesta3.getBody().isPositivo()).isEqualTo(true);
        Assertions.assertThat(respuesta4.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(respuesta4.getBody().isPositivo()).isEqualTo(true);

        // Obtenemos el número de infectados totales
        ResponseEntity<Integer> respuesta5 = restTemplate.withBasicAuth(rastreador.getDni(), rastreador.getPassword()).
                getForEntity(
                        "/estadisticas/infectados/total",
                        Integer.class
                );
        Assertions.assertThat(respuesta5.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(respuesta5.getBody()).isEqualTo(2);

    }

    /**
     * Test para obtener el número de infectados actuales
     */
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testTotalInfectadosActual() {
        // Creamos el rastreador
        DTORastreador rastreador = new DTORastreador("27090987G", "Adrian", "Perez", "Sanchez", "655656565", "secret");

        ResponseEntity<UUID> respuestaRastreador = restTemplate.postForEntity(
                "/rastreadores",
                rastreador,
                UUID.class
        );
        Assertions.assertThat(respuestaRastreador.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Creamos el usuario 1
        DTOUsuario usuario1 = new DTOUsuario("655151515", "adrian123");

        ResponseEntity<UUID> respuestaUsuario1 = restTemplate.postForEntity(
                "/usuarios",
                usuario1,
                UUID.class
        );

        Assertions.assertThat(respuestaUsuario1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(respuestaUsuario1.getBody()).isNotNull();

        // Creamos el usuario 2
        DTOUsuario usuario2 = new DTOUsuario("688546701", "adrian123");

        ResponseEntity<UUID> respuestaUsuario2 = restTemplate.postForEntity(
                "/usuarios",
                usuario2,
                UUID.class
        );

        Assertions.assertThat(respuestaUsuario2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(respuestaUsuario2.getBody()).isNotNull();

        // Notificamos los positivos
        ResponseEntity<DTOUsuario> respuesta3 = restTemplate.withBasicAuth(rastreador.getDni(), rastreador.getPassword()).
                postForEntity("/usuarios/{uuid}/notificaciones/positivo",
                        respuestaRastreador.getBody(),
                        DTOUsuario.class,
                        respuestaUsuario1.getBody()
                );

        ResponseEntity<DTOUsuario> respuesta4 = restTemplate.withBasicAuth(rastreador.getDni(), rastreador.getPassword()).
                postForEntity("/usuarios/{uuid}/notificaciones/positivo",
                        respuestaRastreador.getBody(),
                        DTOUsuario.class,
                        respuestaUsuario2.getBody()
                );

        ResponseEntity<DTOUsuario> respuesta5 = restTemplate.withBasicAuth(rastreador.getDni(), rastreador.getPassword()).
                postForEntity("/usuarios/{uuid}/notificaciones/curacion",
                        respuestaRastreador.getBody(),
                        DTOUsuario.class,
                        respuestaUsuario1.getBody()
                );

        ResponseEntity<DTOUsuario> respuesta6 = restTemplate.withBasicAuth(rastreador.getDni(), rastreador.getPassword()).
                postForEntity("/usuarios/{uuid}/notificaciones/curacion",
                        respuestaRastreador.getBody(),
                        DTOUsuario.class,
                        respuestaUsuario2.getBody()
                );

        Assertions.assertThat(respuesta5.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(respuesta5.getBody().isPositivo()).isEqualTo(false);
        Assertions.assertThat(respuesta6.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(respuesta6.getBody().isPositivo()).isEqualTo(false);

        // Obtenemos el número de infectados actualmente
        ResponseEntity<Integer> respuesta7 = restTemplate.
                getForEntity(
                        "/estadisticas/infectados/actual",
                        Integer.class
                );
        Assertions.assertThat(respuesta7.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(respuesta7.getBody()).isEqualTo(0);

    }

    /**
     * Test para obtener los positivos de los últimos 15 días
     */
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testInfectados15Dias() {
        // Creamos el rastreador
        DTORastreador rastreador = new DTORastreador("27090987G", "Adrian", "Perez", "Sanchez", "655656565", "secret");

        ResponseEntity<UUID> respuestaRastreador = restTemplate.postForEntity(
                "/rastreadores",
                rastreador,
                UUID.class
        );
        Assertions.assertThat(respuestaRastreador.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Creamos los Usuarios 
        DTOUsuario usuario1 = new DTOUsuario("655151515", "adrian123");

        ResponseEntity<UUID> respuestaUsuario1 = restTemplate.postForEntity(
                "/usuarios",
                usuario1,
                UUID.class
        );

        Assertions.assertThat(respuestaUsuario1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(respuestaUsuario1.getBody()).isNotNull();

        DTOUsuario usuario2 = new DTOUsuario("688546701", "adrian123");

        ResponseEntity<UUID> respuestaUsuario2 = restTemplate.postForEntity(
                "/usuarios",
                usuario2,
                UUID.class
        );

        Assertions.assertThat(respuestaUsuario2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(respuestaUsuario2.getBody()).isNotNull();

        DTOUsuario usuario2a = new DTOUsuario("687546701", "juan");

        ResponseEntity<UUID> respuestaUsuario2a = restTemplate.postForEntity(
                "/usuarios",
                usuario2a,
                UUID.class
        );

        Assertions.assertThat(respuestaUsuario2a.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(respuestaUsuario2a.getBody()).isNotNull();

        // Notificamos los positivos
        ResponseEntity<DTOUsuario> respuesta3 = restTemplate.withBasicAuth(rastreador.getDni(), rastreador.getPassword()).
                postForEntity("/usuarios/{uuid}/notificaciones/positivo",
                        respuestaRastreador.getBody(),
                        DTOUsuario.class,
                        respuestaUsuario1.getBody()
                );

        ResponseEntity<DTOUsuario> respuesta4 = restTemplate.withBasicAuth(rastreador.getDni(), rastreador.getPassword()).
                postForEntity("/usuarios/{uuid}/notificaciones/positivo",
                        respuestaRastreador.getBody(),
                        DTOUsuario.class,
                        respuestaUsuario2.getBody()
                );

        ResponseEntity<DTOUsuario> respuesta4b = restTemplate.withBasicAuth(rastreador.getDni(), rastreador.getPassword()).
                postForEntity("/usuarios/{uuid}/notificaciones/positivo",
                        respuestaRastreador.getBody(),
                        DTOUsuario.class,
                        respuestaUsuario2.getBody()
                );

        Assertions.assertThat(respuesta3.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(respuesta3.getBody().isPositivo()).isEqualTo(true);
        Assertions.assertThat(respuesta4.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(respuesta4.getBody().isPositivo()).isEqualTo(true);
        Assertions.assertThat(respuesta4b.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(respuesta4b.getBody().isPositivo()).isEqualTo(true);

        // Obtenemos el número de infectados en las últimas dos semanas
        ResponseEntity<Integer> respuesta5 = restTemplate.
                getForEntity(
                        "/estadisticas/infectados/dosSemanas",
                        Integer.class
                );

        Assertions.assertThat(respuesta5.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(respuesta5.getBody()).isEqualTo(2);
    }

    /**
     * Test para obtener el número de infectados notificados por un rastreador
     */
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testInfectadosRastreador() {
        // Creamos los rastreadores
        DTORastreador rastreador = new DTORastreador("27090987G", "Adrian", "Perez", "Sanchez", "655656565", "secret");
        DTORastreador rastreador2 = new DTORastreador("26523700P", "Juan José", "Perro", "Sanchez", "655656513", "secret");

        //Los damos de alta
        ResponseEntity<UUID> respuestaRastreador = restTemplate.postForEntity(
                "/rastreadores",
                rastreador,
                UUID.class
        );

        ResponseEntity<UUID> respuestaRastreador2 = restTemplate.postForEntity(
                "/rastreadores",
                rastreador2,
                UUID.class
        );

        //Creamos los usuarios
        DTOUsuario usuario1 = new DTOUsuario("655151513", "adrian123");
        DTOUsuario usuario2 = new DTOUsuario("654116512", "juanjose123");
        DTOUsuario usuario3 = new DTOUsuario("656651515", "op1123");
        DTOUsuario usuario4 = new DTOUsuario("612345678", "op1123");

        //Los damos de alta
        ResponseEntity<UUID> respuestaUsuario1 = restTemplate.postForEntity(
                "/usuarios",
                usuario1,
                UUID.class
        );

        ResponseEntity<UUID> respuestaUsuario2 = restTemplate.postForEntity(
                "/usuarios",
                usuario2,
                UUID.class
        );

        ResponseEntity<UUID> respuestaUsuario3 = restTemplate.postForEntity(
                "/usuarios",
                usuario3,
                UUID.class
        );

        ResponseEntity<UUID> respuestaUsuario4 = restTemplate.postForEntity(
                "/usuarios",
                usuario4,
                UUID.class
        );

        //Damos positivo a determinados usuarios
        restTemplate.withBasicAuth(rastreador.getDni(), rastreador.getPassword()).
                postForEntity("/usuarios/{uuid}/notificaciones/positivo",
                        respuestaRastreador.getBody(),
                        DTOUsuario.class,
                        respuestaUsuario1.getBody()
                );

        restTemplate.withBasicAuth(rastreador.getDni(), rastreador.getPassword()).
                postForEntity("/usuarios/{uuid}/notificaciones/positivo",
                        respuestaRastreador.getBody(),
                        DTOUsuario.class,
                        respuestaUsuario2.getBody()
                );

        restTemplate.withBasicAuth(rastreador.getDni(), rastreador.getPassword()).
                postForEntity("/usuarios/{uuid}/notificaciones/positivo",
                        respuestaRastreador.getBody(),
                        DTOUsuario.class,
                        respuestaUsuario3.getBody()
                );

        restTemplate.withBasicAuth(rastreador.getDni(), rastreador.getPassword()).
                postForEntity("/usuarios/{uuid}/notificaciones/positivo",
                        respuestaRastreador2.getBody(),
                        DTOUsuario.class,
                        respuestaUsuario4.getBody()
                );

        //Calculamos el número de positivos que ha dado cada rastreador
        ResponseEntity<Integer> respuestaTest1 = restTemplate.withBasicAuth(rastreador.getDni(), rastreador.getPassword()).
                getForEntity(
                        "/rastreadores/{dni}/estadisticas/positivos",
                        Integer.class,
                        rastreador.getDni()
                );

        ResponseEntity<Integer> respuestaTest2 = restTemplate.withBasicAuth(rastreador2.getDni(), rastreador2.getPassword()).
                getForEntity(
                        "/rastreadores/{dni}/estadisticas/positivos",
                        Integer.class,
                        rastreador2.getDni()
                );

        Assertions.assertThat(respuestaTest1.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(respuestaTest1.getBody()).isEqualTo(3);
        Assertions.assertThat(respuestaTest2.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(respuestaTest2.getBody()).isEqualTo(1);

    }

}
