/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package es.ujaen.dae.ujavid.app;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
/**
 *
 * @author admin
 */

@SpringBootApplication(scanBasePackages={
    "es.ujaen.dae.ujavid.servicios", 
    "es.ujaen.dae.ujavid.repositorios",
    "es.ujaen.dae.ujavid.controladoresREST"
})

@EntityScan(basePackages="es.ujaen.dae.ujavid.entidades")
public class UjaVidApp {    
    public static void main(String[] args) throws Exception {
        // Creación de servidor
        SpringApplication.run(UjaVidApp.class, args);
    }
}


