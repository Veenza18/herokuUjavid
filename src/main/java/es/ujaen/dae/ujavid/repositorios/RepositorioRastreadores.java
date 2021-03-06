/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.repositorios;

import es.ujaen.dae.ujavid.entidades.Rastreador;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO de la clase Rsstreadores
 *
 * @author Venza
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class RepositorioRastreadores {

    @PersistenceContext
    EntityManager em;

    /**
     * Método para obtener el rastreador deseado en la persistencia
     *
     * @param dni Dni del rastreador deseado
     * @return Optional del rastreador encontrado
     */
    public Optional<Rastreador> buscar(String dni) {
        List<Rastreador> lista = em.createQuery("SELECT r FROM Rastreador r WHERE r.dni=?1", Rastreador.class).setParameter(1, dni).getResultList();
        Rastreador r = null;
        if (!lista.isEmpty()) {
            r = lista.get(0);
        }
        return Optional.ofNullable(r);
    }

    /**
     * Método para obtener el rastreador deseado en la persistencia
     *
     * @param uuidRastreador UUID del rastreador deseado
     * @return Optional del rastreador encontrado
     */
    public Optional<Rastreador> buscar(UUID uuidRastreador) {
        return Optional.ofNullable(em.find(Rastreador.class, uuidRastreador));
    }

    /**
     * Método para guardar el rastreador deseado en la persistencia
     *
     * @param rastreador Rastreador deseado
     */
    public void guardar(Rastreador rastreador) {
        em.persist(rastreador);
    }

    /**
     * Método para actualizar el rastreador deseado en la persistencia
     *
     * @param rastreador Rastreador deseado
     */
    public void actualizar(Rastreador rastreador) {
        em.merge(rastreador);
    }

    /**
     * Método para borrar el rastreador deseado en la persistencia
     *
     * @param rastreador Rastreador deseado
     */
    public void borrar(Rastreador rastreador) {
        em.remove(em.merge(rastreador));
    }

}
