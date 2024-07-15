package com.callenger.literatura.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.logging.Logger;
import java.util.logging.Level;

public class ConvierteDatos implements  IConvierteDatos{
    private static final Logger logger = Logger.getLogger(ConvierteDatos.class.getName());
    private ObjectMapper objectMapper;

    public ConvierteDatos(){
        this.objectMapper = new ObjectMapper();
    }


    public <T> T obtenerDatos(String jsonData, Class<T> clase) {
        if (jsonData == null || jsonData.isEmpty()){
            logger.warning("No se recibieron datos");
            return null;
        }
        try {
            logger.info("Inicializando JSON");
            T datos = objectMapper.convertValue(jsonData, clase);
            if (datos == null){
                logger.warning("La data es null");
            }else{
                logger.info("JSON deserialización exitosa");
            }
            return datos;
        }catch (Exception e) {
            logger.log(Level.SEVERE, "Erros en la deserialización del JSON");
            return null;
        }
    }
}
