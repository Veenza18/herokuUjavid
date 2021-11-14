/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.repositorios;

import es.ujaen.dae.ujavid.entidades.Usuario;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Venza
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class RepositorioUsuarios {
     @PersistenceContext
    EntityManager em;
    
    public Optional<Usuario> buscar(UUID uuid){
        return Optional.ofNullable(em.find(Usuario.class,uuid));
    }
    
    public void guardar(Usuario usuario) {
        em.persist(usuario);
    }
    
    public void actualizar(Usuario usuario) {
        em.merge(usuario);
    }

}
