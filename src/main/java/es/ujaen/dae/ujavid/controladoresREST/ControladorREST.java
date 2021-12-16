/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.controladoresREST;

import es.ujaen.dae.ujavid.controladoresREST.DTO.DTOContactoCercano;
import es.ujaen.dae.ujavid.controladoresREST.DTO.DTORastreador;
import es.ujaen.dae.ujavid.controladoresREST.DTO.DTOUsuario;
import es.ujaen.dae.ujavid.entidades.Rastreador;
import es.ujaen.dae.ujavid.excepciones.RastreadorNoRegistrado;
import es.ujaen.dae.ujavid.excepciones.UsuarioNoRegistrado;
import es.ujaen.dae.ujavid.excepciones.ContactosNoAnadidos;
import es.ujaen.dae.ujavid.excepciones.UsuarioYaRegistrado;
import es.ujaen.dae.ujavid.servicios.ServicioUjaVid;
import java.time.LocalDateTime;
import java.util.List;
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
     * Handler para excepciones de violación de restricciones
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
     * Creación de usuarios
     */
    @PostMapping("/usuarios")
    ResponseEntity<UUID> altausuario(@RequestBody DTOUsuario usuario) {
        try {
            UUID uuid = servicios.altaUsuario(usuario.aUsuario());
            return ResponseEntity.status(HttpStatus.CREATED).body(uuid);
        } catch (UsuarioYaRegistrado e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Creacion de Rastreadores
     */
    @PostMapping("/rastreadores")
    ResponseEntity<UUID> altaRastreador(@RequestBody DTORastreador rastreador) {
        try {
            Rastreador rastreadorAux = servicios.altaRastreador(rastreador.aRastreador());
            return ResponseEntity.status(HttpStatus.CREATED).body(rastreadorAux.getUuid());
        } catch (RastreadorNoRegistrado e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /** Login de RASTREADORES (temporal hasta incluir autenticación mediante Spring Security?????? */
    @GetMapping("/rastreadores/{dni}")
    ResponseEntity<DTORastreador> verRastreador(@PathVariable String dni) {
        Optional<Rastreador> rastreador = servicios.verRastreador(dni);
        return rastreador
                .map(c -> ResponseEntity.ok(new DTORastreador(c)))
                .orElse(ResponseEntity.notFound().build());
    }
    /**
     * Registrar contactos
     */
    @PostMapping("/usuarios/{uuidUsuario}/contactos")
    ResponseEntity<Void> realizarContacto(@RequestBody UUID uuidUsuario, @RequestBody List<DTOContactoCercano> contactos) {
        try {
            servicios.addContactoCercano(contactos, uuidUsuario);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (ContactosNoAnadidos e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Listar coontactos
     */
    @GetMapping("/usuarios/{uuidUsuario}/contactos")
    @ResponseStatus(HttpStatus.OK)
    List<DTOContactoCercano> verContactos(@PathVariable UUID uuidUsuario, @RequestBody UUID uuidRastreador) {
        return servicios.verContactosCercanos(uuidUsuario, uuidRastreador).stream()
                .map(t -> new DTOContactoCercano(t.getFechaContacto(), t.getContacto().getUuid(), t.getDistancia(), t.getDuracion())).collect(Collectors.toList());

    }
     

    @PostMapping("/usuarios/{uuidUsuario}/notificacion")
    ResponseEntity<Void> registrarCuracion(@PathVariable UUID uuidUsuario,@RequestBody DTORastreador rastreador) {
        servicios.notificarCuracion(uuidUsuario, rastreador.getUuid());
        
          return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
