/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.repositorios;

import javax.persistence.EntityManager;
import es.ujaen.dae.ujavid.entidades.ContactoCercano;
import java.util.Optional;
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
public class RepositorioContactoCercano {
    @PersistenceContext
    EntityManager em;
    
    public Optional<ContactoCercano> buscar(String dni){
        return Optional.ofNullable(em.find(ContactoCercano.class,dni));
    }
    
    public void guardar(ContactoCercano contacto) {
        em.persist(contacto);
    }
    
    public void actualizar(ContactoCercano contacto) {
        em.merge(contacto);
    }
    
}
