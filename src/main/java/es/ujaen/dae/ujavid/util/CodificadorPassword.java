/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujavid.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author admin
 */
public class CodificadorPassword {
    static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private CodificadorPassword() {
    }

    public static String codificar(String cadena) {
        return encoder.encode(cadena);
    }
    public static boolean igual(String password, String passwordCodificado) {
        return encoder.matches(password, passwordCodificado);
    }

}
