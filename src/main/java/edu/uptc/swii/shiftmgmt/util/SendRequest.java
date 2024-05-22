package edu.uptc.swii.shiftmgmt.util;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
public class SendRequest {

    public void sendPostRequest(String jsonP) {
        // Crear una instancia de RestTemplate
        RestTemplate restTemplate = new RestTemplate();
    
        // Definir la URL
        String url = "http://localhost:9094/logs/create";
    
        // Crear el objeto de la petición
        String json = jsonP;
    
        // Crear los headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    
        // Crear la entidad de la petición
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
    
        // Enviar la petición POST
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
    
        // Imprimir la respuesta
        System.out.println("Response: " + response.getBody());
    }
    
    
}
