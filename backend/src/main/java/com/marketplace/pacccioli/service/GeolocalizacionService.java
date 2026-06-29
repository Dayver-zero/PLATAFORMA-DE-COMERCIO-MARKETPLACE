package com.marketplace.pacccioli.service;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio de geolocalización con cálculos de distancia Haversine
 * Proporciona funcionalidades de ubicación geográfica para búsquedas locales
 */
@Service
@Slf4j
public class GeolocalizacionService {
    
    // Radio de la Tierra en kilómetros
    private static final double RADIO_TIERRA_KM = 6371.0;
    
    /**
     * Calcula la distancia entre dos puntos geográficos usando la fórmula Haversine
     * 
     * @param latitud1 Latitud del primer punto
     * @param longitud1 Longitud del primer punto
     * @param latitud2 Latitud del segundo punto
     * @param longitud2 Longitud del segundo punto
     * @return Distancia en kilómetros
     */
    public Double calcularDistancia(Double latitud1, Double longitud1, Double latitud2, Double longitud2) {
        try {
            if (latitud1 == null || longitud1 == null || latitud2 == null || longitud2 == null) {
                log.warn("Coordenadas nulas recibidas en calcularDistancia");
                return null;
            }
            
            // Convertir de grados a radianes
            double lat1Rad = Math.toRadians(latitud1);
            double lat2Rad = Math.toRadians(latitud2);
            double deltaLat = Math.toRadians(latitud2 - latitud1);
            double deltaLon = Math.toRadians(longitud2 - longitud1);
            
            // Fórmula Haversine
            double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                       Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                       Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
            
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double distancia = RADIO_TIERRA_KM * c;
            
            log.debug("Distancia calculada: {} km", distancia);
            return Math.round(distancia * 100.0) / 100.0; // Redondear a 2 decimales
            
        } catch (Exception e) {
            log.error("Error al calcular distancia", e);
            return null;
        }
    }
    
    /**
     * Calcula el tiempo aproximado de viaje entre dos puntos
     * Asume velocidad promedio de 40 km/h en zona urbana
     * 
     * @param latitud1 Latitud del primer punto
     * @param longitud1 Longitud del primer punto
     * @param latitud2 Latitud del segundo punto
     * @param longitud2 Longitud del segundo punto
     * @return Tiempo estimado en minutos
     */
    public Integer calcularTiempoViaje(Double latitud1, Double longitud1, Double latitud2, Double longitud2) {
        try {
            Double distancia = calcularDistancia(latitud1, longitud1, latitud2, longitud2);
            if (distancia == null) {
                return null;
            }
            
            // Velocidad promedio: 40 km/h en zona urbana
            double velocidadPromedioKmh = 40.0;
            double tiempoHoras = distancia / velocidadPromedioKmh;
            int tiempoMinutos = (int) Math.ceil(tiempoHoras * 60);
            
            log.debug("Tiempo de viaje estimado: {} minutos", tiempoMinutos);
            return tiempoMinutos;
            
        } catch (Exception e) {
            log.error("Error al calcular tiempo de viaje", e);
            return null;
        }
    }
    
    /**
     * Verifica si un comercio está dentro de un radio de búsqueda
     * 
     * @param latitudUsuario Latitud del usuario
     * @param longitudUsuario Longitud del usuario
     * @param latitudComercio Latitud del comercio
     * @param longitudComercio Longitud del comercio
     * @param radioKm Radio de búsqueda en kilómetros
     * @return true si el comercio está dentro del radio
     */
    public Boolean estaEnRadio(Double latitudUsuario, Double longitudUsuario, 
                               Double latitudComercio, Double longitudComercio, 
                               Double radioKm) {
        try {
            if (radioKm == null || radioKm <= 0) {
                log.warn("Radio inválido: {}", radioKm);
                return false;
            }
            
            Double distancia = calcularDistancia(latitudUsuario, longitudUsuario, 
                                                 latitudComercio, longitudComercio);
            
            if (distancia == null) {
                return false;
            }
            
            boolean enRadio = distancia <= radioKm;
            log.debug("Comercio en radio: {} (distancia: {} km, radio: {} km)", 
                     enRadio, distancia, radioKm);
            return enRadio;
            
        } catch (Exception e) {
            log.error("Error al verificar si está en radio", e);
            return false;
        }
    }
    
    /**
     * Obtiene coordenadas aproximadas por dirección (placeholder - requiere API externa)
     * TODO: Integrar con Google Maps Geocoding API
     * 
     * @param direccion Dirección en texto
     * @return Objeto con latitud y longitud, o null si no se encuentra
     */
    public UbicacionDTO obtenerCoordenadasPorDireccion(String direccion) {
        try {
            log.info("Geocodificación solicitada para dirección: {}", direccion);
            
            // TODO: Integrar con Google Maps Geocoding API
            // String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + direccion + "&key=" + googleMapsApiKey;
            // Hacer llamada HTTP y parsear respuesta JSON
            
            // Por ahora, retornar null (requiere API key de Google)
            log.warn("Geocodificación no implementada - se requiere API key de Google Maps");
            return null;
            
        } catch (Exception e) {
            log.error("Error al obtener coordenadas por dirección", e);
            return null;
        }
    }
    
    /**
     * Obtiene dirección aproximada por coordenadas (placeholder - requiere API externa)
     * TODO: Integrar con Google Maps Reverse Geocoding API
     * 
     * @param latitud Latitud
     * @param longitud Longitud
     * @return Dirección aproximada, o null si no se encuentra
     */
    public String obtenerDireccionPorCoordenadas(Double latitud, Double longitud) {
        try {
            log.info("Reverse geocodificación solicitada para: {}, {}", latitud, longitud);
            
            // TODO: Integrar con Google Maps Reverse Geocoding API
            // String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitud + "," + longitud + "&key=" + googleMapsApiKey;
            // Hacer llamada HTTP y parsear respuesta JSON
            
            // Por ahora, retornar null (requiere API key de Google)
            log.warn("Reverse geocodificación no implementada - se requiere API key de Google Maps");
            return null;
            
        } catch (Exception e) {
            log.error("Error al obtener dirección por coordenadas", e);
            return null;
        }
    }
    
    /**
     * Calcula el acimut (ángulo) entre dos puntos
     * Útil para determinar la dirección (norte, sur, este, oeste)
     * 
     * @param latitud1 Latitud del punto inicial
     * @param longitud1 Longitud del punto inicial
     * @param latitud2 Latitud del punto final
     * @param longitud2 Longitud del punto final
     * @return Ángulo en grados (0-360)
     */
    public Double calcularAcimut(Double latitud1, Double longitud1, Double latitud2, Double longitud2) {
        try {
            double lat1Rad = Math.toRadians(latitud1);
            double lat2Rad = Math.toRadians(latitud2);
            double deltaLon = Math.toRadians(longitud2 - longitud1);
            
            double y = Math.sin(deltaLon) * Math.cos(lat2Rad);
            double x = Math.cos(lat1Rad) * Math.sin(lat2Rad) -
                      Math.sin(lat1Rad) * Math.cos(lat2Rad) * Math.cos(deltaLon);
            
            double acimut = Math.toDegrees(Math.atan2(y, x));
            acimut = (acimut + 360) % 360; // Normalizar a 0-360
            
            log.debug("Acimut calculado: {} grados", acimut);
            return Math.round(acimut * 100.0) / 100.0;
            
        } catch (Exception e) {
            log.error("Error al calcular acimut", e);
            return null;
        }
    }
    
    /**
     * Obtiene una descripción textual de la dirección basada en el acimut
     * 
     * @param acimut Ángulo en grados (0-360)
     * @return Dirección cardinal (N, NE, E, SE, S, SO, O, NO)
     */
    public String obtenerDireccionCardinal(Double acimut) {
        if (acimut == null) {
            return "Desconocida";
        }
        
        if (acimut >= 337.5 || acimut < 22.5) return "Norte";
        if (acimut >= 22.5 && acimut < 67.5) return "Noreste";
        if (acimut >= 67.5 && acimut < 112.5) return "Este";
        if (acimut >= 112.5 && acimut < 157.5) return "Sureste";
        if (acimut >= 157.5 && acimut < 202.5) return "Sur";
        if (acimut >= 202.5 && acimut < 247.5) return "Suroeste";
        if (acimut >= 247.5 && acimut < 292.5) return "Oeste";
        if (acimut >= 292.5 && acimut < 337.5) return "Noroeste";
        
        return "Desconocida";
    }
    
    /**
     * DTO para almacenar coordenadas geográficas
     */
    public static class UbicacionDTO {
        public Double latitud;
        public Double longitud;
        public String direccion;
        
        public UbicacionDTO(Double latitud, Double longitud, String direccion) {
            this.latitud = latitud;
            this.longitud = longitud;
            this.direccion = direccion;
        }
        
        public UbicacionDTO(Double latitud, Double longitud) {
            this.latitud = latitud;
            this.longitud = longitud;
        }
    }
}
