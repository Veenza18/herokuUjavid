/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package es.ujaen.dae.ujavid.app;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
/**
 *
 * @author admin
 */

@SpringBootApplication(scanBasePackages="es.ujaen.dae.ujavid.servicios")
public class UjaVidApp {

    public static void main(String[] args) {
        SpringApplication servidor = new SpringApplication(UjaVidApp.class);
        ApplicationContext context = servidor.run(args);
    }
    
}
