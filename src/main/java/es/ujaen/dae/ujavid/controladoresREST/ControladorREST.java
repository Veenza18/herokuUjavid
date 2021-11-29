/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.controladoresREST;
import es.ujaen.dae.ujavid.controladoresREST.DTO.DTOUsuario;
import es.ujaen.dae.ujavid.entidades.Usuario;
import es.ujaen.dae.ujavid.excepciones.UsuarioNoRegistrado;
import es.ujaen.dae.ujavid.servicios.ServicioUjaVid;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Positive;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author admin
 */
@RestController
public class ControladorREST {
    @Autowired
    ServicioUjaVid servicios;
    
     /** Handler para excepciones de violación de restricciones */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handlerViolacionRestricciones(ConstraintViolationException e) {
        
    }
 /** Handler para excepciones de accesos de usuarios no registrados */
    @ExceptionHandler(UsuarioNoRegistrado.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handlerClienteNoRegistrado(UsuarioNoRegistrado e) {
    }

    /** Creación de usuarios */
    @PostMapping("/usuarios")
    ResponseEntity<UUID> altausuario(@RequestBody DTOUsuario usuario) {
        try {
            UUID uuid = servicios.altaUsuario(usuario.aUsuario());
            return ResponseEntity.status(HttpStatus.CREATED).body(uuid);
        }
        catch(UsuarioNoRegistrado e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    
}
