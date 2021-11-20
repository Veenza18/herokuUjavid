/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.servicios;

import es.ujaen.dae.ujavid.entidades.ContactoCercano;
import es.ujaen.dae.ujavid.entidades.DTO.DTOContactoCercano;
import es.ujaen.dae.ujavid.entidades.Rastreador;
import es.ujaen.dae.ujavid.entidades.Usuario;
import es.ujaen.dae.ujavid.excepciones.RastreadorYaRegistrado;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

/**
 *
 * @author Venza
 */
@SpringBootTest(classes = es.ujaen.dae.ujavid.app.UjaVidApp.class)
@ActiveProfiles(profiles = {"test"})
public class ServicioUjaVidTest {

    @Autowired
    ServicioUjaVid servicioUjaVid;

    @Test
    public void testAccesoServicioUJAVID() {
        Assertions.assertThat(servicioUjaVid).isNotNull();
    }

    /**
     * Comprueba que no podemos dar de alta a un usuario en nuestro servicio
     * usando un nº de teléfono incorrecto
     */
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testAltaUsuarioInvalido() {
        // Usuario con Teléfono incorrecto
        Usuario usuario = new Usuario(
                "4524",
                "nuevaclave");

        Assertions.assertThatThrownBy(() -> {
            servicioUjaVid.altaUsuario(usuario);
        })
                .isInstanceOf(ConstraintViolationException.class);
    }

    /**
     * Comprueba que no podemos dar de alta a un rastreador en nuestro servicio
     * usando un teléfono incorrecto
     */
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testAltaRastreadorInvalido() {
        // Rastreador con Teléfono incorrecto!!!
        Rastreador rastreador = new Rastreador(
                "77434825N",
                "Antonio",
                "Venzala",
                "Campaña",
                "6603760",
                "contraseña1");

        Assertions.assertThatThrownBy(() -> {
            servicioUjaVid.altaRastreador(rastreador);
        })
                .isInstanceOf(ConstraintViolationException.class);
    }

    /**
     * Comprueba que podemos dar de alta a un rastreador en nuestro servicio
     * usando credenciales correctas
     */
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testAltaYLoginRastreador() {
        Rastreador rastreador = new Rastreador(
                "77434825N",
                "Antonio",
                "Venzala",
                "Campaña",
                "660376093",
                "contraseña1");

        servicioUjaVid.altaRastreador(rastreador);
        UUID rastreadorLogin = servicioUjaVid.loginRastreador(rastreador.getDni(), "contraseña1");

        Assertions.assertThat(rastreadorLogin).isNotNull();
        Assertions.assertThat(rastreadorLogin).isEqualTo(rastreador.getUuid());

        // Comprobamos que no se puede dar de alta otro rastreador con el mismo dni
        Rastreador rastreador2 = new Rastreador(
                "77434825N",
                "Adrian",
                "Venzala",
                "Campaña",
                "660376093",
                "contraseña1");

        Assertions.assertThatThrownBy(() -> {
            servicioUjaVid.altaRastreador(rastreador2);
        })
                .isInstanceOf(RastreadorYaRegistrado.class);
    }

    /**
     * Validamos que podemos notificar como positivo a un usuario registrado
     */
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testNotificarPos() {
        String contrasena = "contraseña1";

        Usuario usuario = new Usuario(
                "660376093",
                "nuevaclave");
        Rastreador rastreador = new Rastreador(
                "77434825N",
                "Antonio",
                "Venzala",
                "Campaña",
                "660376093",
                contrasena);
        // Damos de alta al usuario
        servicioUjaVid.altaUsuario(usuario);
        // Damos de alta al rastreador
        servicioUjaVid.altaRastreador(rastreador);
        // Logueamos el rastreador
        UUID uuid_rastreador = servicioUjaVid.loginRastreador(rastreador.getDni(), contrasena);

        servicioUjaVid.notificarPos(usuario.getUuid(), LocalDateTime.now(), uuid_rastreador);

        Usuario u = servicioUjaVid.devuelveUsuario(uuid_rastreador, usuario.getUuid()).get();
        Assertions.assertThat(u.isPositivo()).isTrue();

    }

    /**
     * Valida que podemos añadir a los usuarios un contacto con otra persona
     */
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testContactoCercano() {
        Usuario usuario1 = new Usuario(
                "656764549",
                "nuevaclave");

        Rastreador rastreador = new Rastreador(
                "77434825N",
                "Antonio",
                "Venzala",
                "Campaña",
                "660376093",
                "redefine");

        Usuario usuario2 = new Usuario(
                "699699699",
                "nuevaclave");
        Usuario usuario3 = new Usuario(
                "699699692",
                "nuevaclave3");

        servicioUjaVid.altaUsuario(usuario1);
        servicioUjaVid.altaUsuario(usuario2);
        servicioUjaVid.altaUsuario(usuario3);
        servicioUjaVid.altaRastreador(rastreador);
        //Si creamos 2 contactos iguales pero con horas diferentes, se sobreescriben
        DTOContactoCercano contacto0 = new DTOContactoCercano(LocalDateTime.now(),
                usuario2.getUuid(), 4, 2);
        DTOContactoCercano contacto1 = new DTOContactoCercano(LocalDateTime.now(),
                usuario2.getUuid(), 4, 2);
        DTOContactoCercano contacto2 = new DTOContactoCercano(LocalDateTime.now(),
                usuario3.getUuid(), 4, 2);

        List<DTOContactoCercano> contactos = new ArrayList<>();
        contactos.add(contacto0);
        contactos.add(contacto1);
        contactos.add(contacto2);

        servicioUjaVid.addContactoCercano(contactos, usuario1.getUuid());
        //servicioUjaVid.verContactosCercanos(usuario1.getUuid(), rastreador.getDni(), rastreador.getUuid());
        Assertions.assertThat(servicioUjaVid.verContactosCercanos(usuario1.getUuid(), rastreador.getUuid()).size()).isEqualTo(2);
    }

    /**
     * Valida nuestra función metaheurística, donde se comprueba que se obtiene
     * una lista ordenada para una determinada persona de los contactos que ha
     * tenido recientemente
     */
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testMetaheuristica() {

        String contrasena = "contraseña1";

        Rastreador rastreador = new Rastreador(
                "77434825N",
                "Juan Jose",
                "Peiro",
                "Garrido",
                "612121211",
                contrasena);

        Usuario usuario1 = new Usuario(
                "656764549",
                "nuerrrfve");

        Usuario usuario2 = new Usuario(
                "699699699",
                "nrerfre");

        Usuario usuario3 = new Usuario(
                "670670670",
                "nurfrerfeve");

        Usuario usuario4 = new Usuario(
                "660376093",
                "efrerfree");

        servicioUjaVid.altaRastreador(rastreador);
        // Logueamos el rastreador
        UUID uuid_rastreador = servicioUjaVid.loginRastreador(rastreador.getDni(), contrasena);

        servicioUjaVid.altaUsuario(usuario1);
        servicioUjaVid.altaUsuario(usuario2);
        servicioUjaVid.altaUsuario(usuario3);
        servicioUjaVid.altaUsuario(usuario4);

        DTOContactoCercano contacto0 = new DTOContactoCercano(LocalDateTime.now(),
                usuario2.getUuid(), 4, 2);

        DTOContactoCercano contacto1 = new DTOContactoCercano(LocalDateTime.now(),
                usuario3.getUuid(), 2, 4);

        DTOContactoCercano contacto2 = new DTOContactoCercano(LocalDateTime.now(),
                usuario4.getUuid(), 1, 3);

        List<DTOContactoCercano> listaContactos = new ArrayList<>();
        listaContactos.add(contacto0);
        listaContactos.add(contacto1);
        listaContactos.add(contacto2);
        servicioUjaVid.addContactoCercano(listaContactos, usuario1.getUuid());

        servicioUjaVid.notificarPos(usuario1.getUuid(), LocalDateTime.now(), uuid_rastreador);

        List<ContactoCercano> lista = servicioUjaVid.verContactosCercanos(usuario1.getUuid(), uuid_rastreador);

        for (int i = 0; i < lista.size() - 1; i++) {
            Assertions.assertThat(lista.get(i).getRiesgo()).isGreaterThanOrEqualTo(lista.get(i + 1).getRiesgo());
        }
    }

    /**
     * Validación de que el estadístico de positivos actualmente y la función
     * notificar curación trabajan correctamente
     */
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testNotificacionCuracionyPositivosActualmente() {
        String contrasena = "contraseña1";

        Rastreador rastreador = new Rastreador(
                "77434825N",
                "Juan Jose",
                "Peiro",
                "Garrido",
                "612121211",
                contrasena);

        Usuario usuario1 = new Usuario(
                "656764549",
                "nuerrrfve");

        Usuario usuario2 = new Usuario(
                "699699699",
                "nrerfre");

        Usuario usuario3 = new Usuario(
                "670670670",
                "nurfrerfeve");

        servicioUjaVid.altaRastreador(rastreador);
        // Logueamos el rastreador
        UUID uuid_rastreador = servicioUjaVid.loginRastreador(rastreador.getDni(), contrasena);
        servicioUjaVid.altaUsuario(usuario1);
        servicioUjaVid.altaUsuario(usuario2);
        servicioUjaVid.altaUsuario(usuario3);

        servicioUjaVid.notificarPos(usuario1.getUuid(), LocalDateTime.now(), uuid_rastreador);
        servicioUjaVid.notificarPos(usuario2.getUuid(), LocalDateTime.now(), uuid_rastreador);
        servicioUjaVid.notificarPos(usuario3.getUuid(), LocalDateTime.now(), uuid_rastreador);

        Assertions.assertThat(servicioUjaVid.positivosActual(uuid_rastreador)).isEqualTo(3);
        servicioUjaVid.notificarCuracion(usuario1.getUuid(), uuid_rastreador);
        Assertions.assertThat(servicioUjaVid.positivosActual(uuid_rastreador)).isEqualTo(2);
    }

    /**
     * Validación de que el estadístico de que el estadístico de positivos en
     * los ultimos 15 dias funciona correctamente
     */
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testPositivos15Dias() {
        String contrasena = "contraseña1";

        Rastreador rastreador = new Rastreador(
                "77434825N",
                "Juan Jose",
                "Peiro",
                "Garrido",
                "612121211",
                contrasena);

        Usuario usuario1 = new Usuario(
                "656764549",
                "nuerrrfve");

        Usuario usuario2 = new Usuario(
                "699699699",
                "nrerfre");

        Usuario usuario3 = new Usuario(
                "670670670",
                "nurfrerfeve");

        servicioUjaVid.altaRastreador(rastreador);
        // Logueamos el rastreador
        UUID uuid_rastreador = servicioUjaVid.loginRastreador(rastreador.getDni(), contrasena);
        servicioUjaVid.altaUsuario(usuario1);
        servicioUjaVid.altaUsuario(usuario2);
        servicioUjaVid.altaUsuario(usuario3);

        servicioUjaVid.notificarPos(usuario1.getUuid(), LocalDateTime.now().minusDays(5), uuid_rastreador);
        servicioUjaVid.notificarPos(usuario2.getUuid(), LocalDateTime.now().minusDays(40), uuid_rastreador);
        servicioUjaVid.notificarPos(usuario3.getUuid(), LocalDateTime.now().minusDays(60), uuid_rastreador);

        Assertions.assertThat(servicioUjaVid.positivos15Dias(uuid_rastreador)).isEqualTo(1);
    }

    /**
     * Validación de que se pueden introducir fechas invalidas, como por ejemplo
     * fechas futuras a la hora de notificar un positivo
     */
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testFechasInvalidas() {
        String contrasena = "contraseña1";

        Rastreador rastreador = new Rastreador(
                "77434825N",
                "Juan Jose",
                "Peiro",
                "Garrido",
                "612121211",
                contrasena);

        Usuario usuario1 = new Usuario(
                "656764549",
                "nuerrrfve");

        servicioUjaVid.altaRastreador(rastreador);
        // Logueamos el rastreador
        UUID uuid_rastreador = servicioUjaVid.loginRastreador(rastreador.getDni(), contrasena);
        servicioUjaVid.altaUsuario(usuario1);

        Assertions.assertThatThrownBy(() -> {
            servicioUjaVid.notificarPos(usuario1.getUuid(), LocalDateTime.now().plusDays(5), uuid_rastreador);
        })
                .isInstanceOf(ConstraintViolationException.class);

    }

    /**
     * Validación de que el contador de los reportados por rastreador funciona
     * correctamente y del estadístico de total infectados
     */
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testReportadosRastreadorTotalinfectados() {
        String contrasena = "a";

        Rastreador rastreador = new Rastreador(
                "77434825N",
                "Juan Jose",
                "Peiro",
                "Garrido",
                "612121211",
                contrasena);

        Rastreador rastreador2 = new Rastreador(
                "26523700P",
                "Felipe",
                "Peiro",
                "Garrido",
                "676767676",
                contrasena);

        Usuario usuario1 = new Usuario(
                "656764549",
                "nuerrrfve");

        Usuario usuario2 = new Usuario(
                "699699699",
                "nrerfre");

        Usuario usuario3 = new Usuario(
                "670670670",
                "nurfrerfeve");

        servicioUjaVid.altaRastreador(rastreador);
        // Logueamos el rastreador
        UUID uuid_rastreador1 = servicioUjaVid.loginRastreador(rastreador.getDni(), contrasena);
        servicioUjaVid.altaRastreador(rastreador2);
        // Logueamos el rastreador
        UUID uuid_rastreador2 = servicioUjaVid.loginRastreador(rastreador2.getDni(), contrasena);
        servicioUjaVid.altaUsuario(usuario1);
        servicioUjaVid.altaUsuario(usuario2);
        servicioUjaVid.altaUsuario(usuario3);

        servicioUjaVid.notificarPos(usuario1.getUuid(), LocalDateTime.now().minusDays(60), uuid_rastreador1);

        servicioUjaVid.notificarPos(usuario2.getUuid(), LocalDateTime.now().minusDays(40), uuid_rastreador2);
        servicioUjaVid.notificarPos(usuario3.getUuid(), LocalDateTime.now().minusDays(5), uuid_rastreador2);
        servicioUjaVid.notificarCuracion(usuario1.getUuid(), uuid_rastreador1);
        servicioUjaVid.notificarCuracion(usuario2.getUuid(), uuid_rastreador1);
        servicioUjaVid.notificarPos(usuario1.getUuid(), LocalDateTime.now(), uuid_rastreador1);
        servicioUjaVid.notificarPos(usuario2.getUuid(), LocalDateTime.now(), uuid_rastreador1);

        Assertions.assertThat(servicioUjaVid.positivosRastreador(uuid_rastreador1)).isEqualTo(3);
        Assertions.assertThat(servicioUjaVid.positivosRastreador(uuid_rastreador2)).isEqualTo(2);
        Assertions.assertThat(servicioUjaVid.totalInfectados(uuid_rastreador1)).isEqualTo(5);
    }

    /**
     * Validación de que el estadístico de que el estadístico de
     * contagiadosXUsuario funciona correctamente (Si dos personas tienen
     * contacto entre si y dan positivo en el periodo que puede contagiar, se
     * contará dos veces)
     */
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testContagiadosXUsuario() {

        String contrasena = "a";
        Rastreador rastreador = new Rastreador(
                "77434825N",
                "Juan Jose",
                "Peiro",
                "Garrido",
                "612121211",
                contrasena);

        Usuario usuario1 = new Usuario(
                "656764549",
                "nuerrrfve");

        Usuario usuario2 = new Usuario(
                "699699699",
                "nrerfre");

        Usuario usuario3 = new Usuario(
                "670670670",
                "nurfrerfeve");

        Usuario usuario4 = new Usuario(
                "670670670",
                "nurfrerfeve");

        Usuario usuario5 = new Usuario(
                "670670670",
                "nurfrerfeve");

        servicioUjaVid.altaRastreador(rastreador);
        // Logueamos el rastreador
        UUID uuid_rastreador = servicioUjaVid.loginRastreador(rastreador.getDni(), contrasena);
        servicioUjaVid.altaUsuario(usuario1);
        servicioUjaVid.altaUsuario(usuario2);
        servicioUjaVid.altaUsuario(usuario3);
        servicioUjaVid.altaUsuario(usuario4);
        servicioUjaVid.altaUsuario(usuario5);

        DTOContactoCercano contacto0 = new DTOContactoCercano(LocalDateTime.now().minusDays(10),
                usuario2.getUuid(), 4, 2);
        DTOContactoCercano contacto1 = new DTOContactoCercano(LocalDateTime.now().minusDays(90),
                usuario3.getUuid(), 4, 2);
        DTOContactoCercano contactoX = new DTOContactoCercano(LocalDateTime.now().minusDays(10),
                usuario1.getUuid(), 4, 2);
        DTOContactoCercano contactoY = new DTOContactoCercano(LocalDateTime.now().minusDays(90),
                usuario1.getUuid(), 4, 2);

        List<DTOContactoCercano> lista1 = new ArrayList<>();
        lista1.add(contacto0);
        lista1.add(contacto1);

        List<DTOContactoCercano> lista2 = new ArrayList<>();
        lista2.add(contactoX);

        List<DTOContactoCercano> lista3 = new ArrayList<>();
        lista3.add(contactoY);

        servicioUjaVid.addContactoCercano(lista1, usuario1.getUuid());
        servicioUjaVid.addContactoCercano(lista2, usuario2.getUuid());
        servicioUjaVid.addContactoCercano(lista3, usuario3.getUuid());
        //Los contactosCercanos entre usuario 1 y usuario 3 sucedieron hace 90 días, por lo que el programa no lo contará para el estadístico
        //El contacto entre usuario 1 y usuario 2 fue en el periodo de contagio y se contará en el estadístico como 2 contagios, es decir
        //que el usuario 1 contagió al usuario 2 y el usuario 2 contagió al usuario 1
        servicioUjaVid.notificarPos(usuario1.getUuid(), LocalDateTime.now().minusDays(12), uuid_rastreador);
        servicioUjaVid.notificarPos(usuario2.getUuid(), LocalDateTime.now().minusDays(2), uuid_rastreador);
        servicioUjaVid.notificarPos(usuario3.getUuid(), LocalDateTime.now().minusDays(10), uuid_rastreador);

        servicioUjaVid.notificarCuracion(usuario1.getUuid(), uuid_rastreador);

        DTOContactoCercano contacto3 = new DTOContactoCercano(LocalDateTime.now().minusDays(10),
                usuario4.getUuid(), 4, 2);

        DTOContactoCercano contacto4 = new DTOContactoCercano(LocalDateTime.now().minusDays(10),
                usuario4.getUuid(), 4, 2);

        List<DTOContactoCercano> lista4 = new ArrayList<>();
        lista4.add(contacto4);

        List<DTOContactoCercano> lista5 = new ArrayList<>();
        lista5.add(contacto3);

        servicioUjaVid.addContactoCercano(lista4, usuario4.getUuid());
        servicioUjaVid.addContactoCercano(lista5, usuario5.getUuid());

        servicioUjaVid.notificarPos(usuario4.getUuid(), LocalDateTime.now().minusDays(6), uuid_rastreador);
        servicioUjaVid.notificarPos(usuario5.getUuid(), LocalDateTime.now(), uuid_rastreador);

        servicioUjaVid.notificarCuracion(usuario4.getUuid(), uuid_rastreador);
        servicioUjaVid.contagiadosXusuario(uuid_rastreador);

//        Assertions.assertThat(servicioUjaVid.contagiadosXusuario(rastreador.getDni(), uuid_rastreador)).isEqualTo(214);
    }

    /**
     * Comprobación de que el método ver contactos funciona correctamente,
     * haciendo test al número de contactos que ha tenido ese usuario
     */
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testVercontactos() {

        String contrasena = "contraseña1";

        Rastreador rastreador = new Rastreador(
                "77434825N",
                "Juan Jose",
                "Peiro",
                "Garrido",
                "612121211",
                contrasena);

        Usuario usuario1 = new Usuario(
                "656764549",
                "nuerrrfve");

        Usuario usuario2 = new Usuario(
                "699699699",
                "nrerfre");

        Usuario usuario3 = new Usuario(
                "670670670",
                "nurfrerfeve");

        Usuario usuario4 = new Usuario(
                "660376093",
                "efrerfree");

        servicioUjaVid.altaRastreador(rastreador);
        UUID uuid_rastreador = servicioUjaVid.loginRastreador(rastreador.getDni(), contrasena);
        servicioUjaVid.altaUsuario(usuario1);
        servicioUjaVid.altaUsuario(usuario2);
        servicioUjaVid.altaUsuario(usuario3);
        servicioUjaVid.altaUsuario(usuario4);

        // Comprobamos que ahora mismo no la lista de conatctos cercanos está vacia 
        Assertions.assertThat(servicioUjaVid.verContactosCercanos(usuario1.getUuid(), rastreador.getUuid()).size()).isEqualTo(0);

        DTOContactoCercano contacto0 = new DTOContactoCercano(LocalDateTime.now().minusDays(20),
                usuario2.getUuid(), 4, 2);

        DTOContactoCercano contacto1 = new DTOContactoCercano(LocalDateTime.now().minusDays(2),
                usuario3.getUuid(), 2, 4);

        DTOContactoCercano contacto2 = new DTOContactoCercano(LocalDateTime.now(),
                usuario4.getUuid(), 1, 3);

        List<DTOContactoCercano> contactos = new ArrayList<>();
        contactos.add(contacto0);
        contactos.add(contacto1);
        contactos.add(contacto2);

        servicioUjaVid.addContactoCercano(contactos, usuario1.getUuid());

        Assertions.assertThat(servicioUjaVid.verContactosCercanos(usuario1.getUuid(), uuid_rastreador).size()).isEqualTo(2);
    }

}
