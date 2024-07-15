package com.callenger.literatura.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsumoAPI {
    private static final Logger logger = Logger.getLogger(ConsumoAPI.class.getName());
    public String obtenerDatos(String url){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = null;
        try{
            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();
            return json;
        } catch (IOException | InterruptedException e) {
            logger.log(Level.SEVERE, "Error al recuperar datos de API");
            throw new RuntimeException("Error al recuperar datos de API",e);
        }
    }
}
