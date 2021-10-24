/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujavid.excepciones;

/**
 * Excepción provocada por el intento de registrar un rastreador en el sistema que
 * ya ha sido registrado anteriormente
 *
 * @author Juan José
 */
public class RastreadorYaRegistrado extends RuntimeException {

    public RastreadorYaRegistrado() {
    }

}
