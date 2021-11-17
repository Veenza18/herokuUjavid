/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.repositorios;

import es.ujaen.dae.ujavid.entidades.Rastreador;
import java.util.Optional;
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
public class RepositorioRastreadores {
    @PersistenceContext
    EntityManager em;
    
    public Optional<Rastreador> buscar(String dni){
        return Optional.ofNullable(em.find(Rastreador.class,dni));
    }
    
    public void guardar(Rastreador rastreador) {
        em.persist(rastreador);
    }
    
    public void actualizar(Rastreador rastreador) {
        em.merge(rastreador);
    }

    public void borrar(Rastreador rastreador) {
        em.remove(em.merge(rastreador));
    }
    

}
