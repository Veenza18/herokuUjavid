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

    public int positivos15Dias() {
        LocalDateTime fecha15dias = LocalDateTime.now().minusDays(15);
        Long positivos = em.createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.positivo = TRUE AND u.fPositivo >= ?1 ", Long.class).setParameter(1, fecha15dias).getSingleResult();
        return positivos.intValue();
    }

    public int contagiadosXusuario() {
        LocalDateTime fecha15dias = LocalDateTime.now().minusDays(15);
        List<Usuario> listaContagiados = em.createQuery("SELECT u FROM Usuario u WHERE u.positivo = TRUE  ", Usuario.class).getResultList();

        LocalDate curacion;
        LocalDateTime contagio;

        for (int i = 0; i < listaContagiados.size(); i++) {

            contagio = listaContagiados.get(0).getfPositivo();
            curacion = listaContagiados.get(0).getfCuracion();
            if (listaContagiados.get(0).getfCuracion() == null) {
                curacion = LocalDate.now();
            };

            //NECESITAMOS MIRAR SOLOS SUS CONTACTOS
            List<Usuario> listaCausadoPorContagiados = em.createQuery("SELECT u FROM Usuario u WHERE u.positivo = TRUE AND u.fPositivo >= ?1 AND u.fPositivo >= ?2 ", Usuario.class).setParameter(1, fecha15dias).setParameter(2, curacion).getResultList();
        }

        //cambiar el return esta hecho para pruebas
        return listaContagiados.size();
    }

    public int positivosActual() {
        Long positivos = em.createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.positivo = TRUE  ", Long.class).getSingleResult();
        return positivos.intValue();
    }
}
