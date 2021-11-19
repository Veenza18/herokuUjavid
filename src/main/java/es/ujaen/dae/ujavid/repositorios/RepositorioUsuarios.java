/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.repositorios;

import es.ujaen.dae.ujavid.entidades.ContactoCercano;
import es.ujaen.dae.ujavid.entidades.Usuario;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO de la clase Usuarios
 *
 * @author Venza
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class RepositorioUsuarios {

    @PersistenceContext
    EntityManager em;

    public Optional<Usuario> buscar(UUID uuid) {
        return Optional.ofNullable(em.find(Usuario.class, uuid));
    }

    public void guardar(Usuario usuario) {
        em.persist(usuario);
    }

    public void actualizar(Usuario usuario) {
        em.merge(usuario);
    }

    public void borrar(Usuario usuario) {
        em.remove(em.merge(usuario));
    }

    public void addContactoCercano(ContactoCercano contacto) {
        em.persist(contacto);
    }

    public List<Usuario> obtenerUsuarios() {
        List<Usuario> lista = em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
        return lista;
    }
    
    public List<Usuario> obtenerUsuariosPositivos() {
        List<Usuario> lista = em.createQuery("SELECT u FROM Usuario u WHERE u.fPositivo IS NOT NULL ", Usuario.class).getResultList();
        return lista;
    }

    public int positivos15Dias() {
        LocalDateTime fecha15dias = LocalDateTime.now().minusDays(15);
        Long positivos = em.createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.positivo = TRUE AND u.fPositivo >= ?1 ", Long.class).setParameter(1, fecha15dias).getSingleResult();
        return positivos.intValue();
    }

    public int positivosActual() {
        Long positivos = em.createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.positivo = TRUE  ", Long.class).getSingleResult();
        return positivos.intValue();
    }

    public List<Usuario> positivosHistorial() {
        List<Usuario> lista = em.createQuery("SELECT u FROM Usuario u WHERE u.positivo IS NOT NULL  ", Usuario.class).getResultList();
        return lista;
    }

}
