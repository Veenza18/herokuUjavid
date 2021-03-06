/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.controladoresREST;

import es.ujaen.dae.ujavid.controladoresREST.DTO.DTOContactoCercano;
import es.ujaen.dae.ujavid.controladoresREST.DTO.DTORastreador;
import es.ujaen.dae.ujavid.controladoresREST.DTO.DTOUsuario;
import es.ujaen.dae.ujavid.entidades.Rastreador;
import es.ujaen.dae.ujavid.entidades.Usuario;
import es.ujaen.dae.ujavid.excepciones.UsuarioNoRegistrado;
import es.ujaen.dae.ujavid.excepciones.ContactosNoAnadidos;
import es.ujaen.dae.ujavid.excepciones.RastreadorNoRegistrado;
import es.ujaen.dae.ujavid.excepciones.RastreadorYaRegistrado;
import es.ujaen.dae.ujavid.excepciones.UsuarioYaRegistrado;
import es.ujaen.dae.ujavid.servicios.ServicioUjaVid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author admin
 */
@RestController
@RequestMapping("/ujavid")
public class ControladorREST {

    @Autowired
    ServicioUjaVid servicios;

    /**
     * Handler para excepciones de violaci??n de restricciones
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handlerViolacionRestricciones(ConstraintViolationException e) {

    }

    /**
     * Handler para excepciones de accesos de usuarios no registrados
     */
    @ExceptionHandler(UsuarioNoRegistrado.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handlerUsuarioNoRegistrado(UsuarioNoRegistrado e) {
    }

    /**
     * Handler para excepciones de accesos de rastreadores no registrados
     */
    @ExceptionHandler(RastreadorNoRegistrado.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handlerRastreadorNoRegistrado(RastreadorNoRegistrado e) {
    }

    /**
     * Creacion de Usuarios
     * @param usuario DTO del usuario
     * 
     * @return UUID del usuario
     */
    @PostMapping("/usuarios")
    ResponseEntity<UUID> altaUsuario(@RequestBody DTOUsuario usuario) {
        try {
            UUID uuid = servicios.altaUsuario(usuario.aUsuario());
            return ResponseEntity.status(HttpStatus.CREATED).body(uuid);
        } catch (UsuarioYaRegistrado e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Creacion de Rastreadores
     * @param rastreador DTO del rastreador
     * 
     * @return UUID del rastreador
     */
    @PostMapping("/rastreadores")
    ResponseEntity<UUID> altaRastreador(@RequestBody DTORastreador rastreador) {
        try {
            Rastreador rastreadorAux = servicios.altaRastreador(rastreador.aRastreador());
            return ResponseEntity.status(HttpStatus.CREATED).body(rastreadorAux.getUuid());
        } catch (RastreadorYaRegistrado e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Mostrar el rastreador
     * @param dni dni del rastreador
     * 
     * @return DTO del rastreador escogido
     */
    @GetMapping("/rastreadores/{dni}")
    ResponseEntity<DTORastreador> verRastreador(@PathVariable String dni) {
        Optional<Rastreador> rastreador = servicios.verRastreador(dni);
        return rastreador
                .map(c -> ResponseEntity.ok(new DTORastreador(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Registrar contactos
     * @param uuid UUID del usuario
     * @param contactos Lista de contactos cercanos a a??adir a nuestro usuario
     */
    @PostMapping("/usuarios/{uuid}/contactos")
    ResponseEntity<Void> realizarContacto(@PathVariable UUID uuid, @RequestBody List<DTOContactoCercano> contactos) {
        try {
            servicios.addContactoCercano(contactos, uuid);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (UsuarioNoRegistrado e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

    }

    /**
     * Listar coontactos
     * @param uuid UUID del usuario
     * @param uuidRastreador UUID del rastreador
     * 
     * @return ListaDTO con los contactos del determinado usuario
     */
    @GetMapping("/usuarios/{uuid}/contactos/{uuidRastreador}")
    @ResponseStatus(HttpStatus.OK)
    List<DTOContactoCercano> verContactos(@PathVariable UUID uuid,@PathVariable UUID uuidRastreador) {
        return servicios.verContactosCercanos(uuid,uuidRastreador).stream()
                .map(t -> new DTOContactoCercano(t.getFechaContacto(), t.getContacto().getUuid(), t.getDistancia(), t.getDuracion())).collect(Collectors.toList());
      
    }

    /**
     * M??todo para resgistrar un Positivo
     *
     * @param numTelefono N?? de tel??fono del usuario
     * @param uuidRastreador UUID del rastreador
     * @return DTO del usuario que se le ha notificado el positivo
     */
    @PostMapping("/usuarios/{uuid}/notificaciones/positivo")
    ResponseEntity<DTOUsuario> registrarPositivo(@PathVariable UUID uuid, @RequestBody UUID uuidRastreador) {
        System.out.println("----------------------------------Registrar Positivo");
        Optional<Usuario> u = servicios.devuelveUsuario(uuidRastreador, uuid);
        servicios.notificarPos(u.get().getUuid(), LocalDateTime.now(), uuidRastreador);
        DTOUsuario dtoUsuario = new DTOUsuario(servicios.devuelveUsuario(uuidRastreador, uuid).get());

        return ResponseEntity.status(HttpStatus.OK).body(dtoUsuario);
    }

    /**
     * M??todo para resgistrar una curaci??n de un positivo
     *
     * @param numTelefono N?? de tel??fono del usuario
     * @param uuidRastreador UUID del rastreador
     * @return DTO del usuario que se le ha notificado la curaci??n
     */
    @PostMapping("/usuarios/{uuid}/notificaciones/curacion")
    ResponseEntity<DTOUsuario> registrarCuracion(@PathVariable UUID uuid, @RequestBody UUID uuidRastreador) {
        System.out.println("-------------------------------Registrar curacion");
        Optional<Usuario> u = servicios.devuelveUsuario(uuidRastreador, uuid);
        servicios.notificarCuracion(u.get().getUuid(), uuidRastreador);
        DTOUsuario dtoUsuario = new DTOUsuario(servicios.devuelveUsuario(uuidRastreador, uuid).get());

        return ResponseEntity.status(HttpStatus.OK).body(dtoUsuario);
    }

    /**
     * M??todo para obtener el N?? total de infectados
     * 
     * @return N?? total de infectados 
     */
    @GetMapping("/estadisticas/infectados/total")
    ResponseEntity<Integer> obtenerTotalInfectados() {
        return ResponseEntity.status(HttpStatus.OK).body(servicios.totalInfectados());
    }

    /**
     * M??todo para obtener el N?? de infectados actual
     * 
     * @return N?? toal de infectados actual
     */
    @GetMapping("/estadisticas/infectados/actual")
    ResponseEntity<Integer> obtenerTotalPositivosActual() {
        return ResponseEntity.status(HttpStatus.OK).body(servicios.positivosActual());
    }

    /**
     * M??todo para obtener el N?? de positivos de los ??ltimos 15 d??as
     * 
     * @return N?? de positivos en los ??ltimos 15 d??as
     */
    @GetMapping("/estadisticas/infectados/dosSemanas")
    ResponseEntity<Integer> obtenerTotalPositivos15Dias() {
        return ResponseEntity.status(HttpStatus.OK).body(servicios.positivos15Dias());
    }

    /**
     * M??todo para obtener la incidencia de contagiados x usuario
     * 
     * @return Incidencia de contagiados por usuario 
     */
    @GetMapping("/estadisticas/infectados/incidencia")
    ResponseEntity<Double> obtenerContagiadosUsuario() {
        return ResponseEntity.status(HttpStatus.OK).body(servicios.contagiadosXusuario());
    }

    /**
     * M??todo para obtener el N?? total de personas que ha notificado un rastreador
     * 
     * @param dni DNI del rastreador a obtener su estad??stica
     * @return Personas notificadas por el rastreador
     */
    @GetMapping("/rastreadores/{dni}/estadisticas/positivos")
    ResponseEntity<Integer> obtenerTotalPositivosRastreador(@PathVariable String dni) {
        return ResponseEntity.status(HttpStatus.OK).body(servicios.positivosRastreador(servicios.verRastreador(dni).get().getUuid()));
    }

}
