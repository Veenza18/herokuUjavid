/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.repositorios;

import es.ujaen.dae.ujavid.entidades.ContactoCercano;
import es.ujaen.dae.ujavid.entidades.Usuario;
import static es.ujaen.dae.ujavid.entidades.Usuario.DIAS_TRANSCURRIDOS;
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

     /**
     * Método para obtener el usuario deseado en la persistencia
     *
     * @param uuid UUID del usuario usuario
     * @return Optional del usuario encontrado
     */
    public Optional<Usuario> buscar(UUID uuid) {
        return Optional.ofNullable(em.find(Usuario.class, uuid));
    }

    /**
     * Método para obtener el usuario deseado en la persistencia
     *
     * @param telefono Teléfono del Usuario
     * @return Optional del usuario encontrado
     */
    public Optional<Usuario> buscar(String telefono) {
        List<Usuario> lista = em.createQuery("SELECT u FROM Usuario u WHERE ?1 = u.numTelefono", Usuario.class).setParameter(1,telefono).getResultList();
        Usuario u = null;
        if (!lista.isEmpty()) {
            u = lista.get(0);
        }
        return Optional.ofNullable(u);
    }
        /**
     * Método para guardar el usuario deseado en la persistencia
     *
     * @param usuario Usuario deseado
     */
    public void guardar(Usuario usuario) {
        em.persist(usuario);
    }

    /**
     * Método para actualizar el usuario deseado en la persistencia
     *
     * @param usuario Usuario deseado
     */
    public void actualizar(Usuario usuario) {
        em.merge(usuario);
    }

        /**
     * Método para borrar el usuario deseado en la persistencia
     *
     * @param usuario Usuario deseado
     */
    public void borrar(Usuario usuario) {
        em.remove(em.merge(usuario));
    }

     /**
     * Método para añadir a un usuario, un contacto cercano en la persistencia
     *
     * @param contacto ContactoCercano que guarda la información sobre un contacto con otro usuario
     */
    public void addContactoCercano(ContactoCercano contacto) {
        em.persist(contacto);
    }

    /**
     * Método para obtener todos los usuarios de la persistencia
     */
    public List<Usuario> obtenerUsuarios() {
        List<Usuario> lista = em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
        return lista;
    }
    
     /**
     * Método para obtener todos los usuarios que hayan sido almenos positivos una vez de la persistencia
     */
    public List<Usuario> obtenerUsuariosPositivos() {
        List<Usuario> lista = em.createQuery("SELECT u FROM Usuario u WHERE u.fPositivo IS NOT NULL ", Usuario.class).getResultList();
        return lista;
    }

     /**
     * Método para obtener todos los usuarios que hayan sido positivos en los últimos 15 días de la persistencia
     */
    public int positivos15Dias() {
        LocalDateTime fecha15dias = LocalDateTime.now().minusDays(15);
        Long positivos = em.createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.positivo = TRUE AND u.fPositivo >= ?1 ", Long.class).setParameter(1, fecha15dias).getSingleResult();
        return positivos.intValue();
    }

     /**
     * Método para obtener todos los usuarios contagiados actualmente de la persistencia
     */
    public int positivosActual() {
        Long positivos = em.createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.positivo = TRUE  ", Long.class).getSingleResult();
        return positivos.intValue();
    }

    /**
     * Método para obtener todos los usuarios los cuales hayan tenido una modificación en su atributo positivo
     */
    public List<Usuario> positivosHistorial() {
        List<Usuario> lista = em.createQuery("SELECT u FROM Usuario u WHERE u.positivo IS NOT NULL  ", Usuario.class).getResultList();
        return lista;
    }
    
    public List<ContactoCercano> verContactos(UUID uuid){
         LocalDateTime fecha2Semanas = LocalDateTime.now().minusDays(14);
        List<ContactoCercano> lista = em.createQuery("SELECT c FROM Usuario u JOIN u.listadoContactos c WHERE c.fechaContacto  >= ?1 AND u.uuid = ?2 ORDER BY c.riesgo DESC",ContactoCercano.class ).setParameter(1,fecha2Semanas).setParameter(2,uuid).getResultList();
        return lista;
    }
}
