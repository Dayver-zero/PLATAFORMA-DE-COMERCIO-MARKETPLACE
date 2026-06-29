package com.marketplace.pacccioli.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GeolocalizacionService Tests")
class GeolocalizacionServiceTest {
    
    private GeolocalizacionService geoService;
    
    // Coordenadas de Punata, Bolivia
    private static final Double PUNATA_LAT = -17.5528;
    private static final Double PUNATA_LON = -65.8756;
    
    @BeforeEach
    void setUp() {
        geoService = new GeolocalizacionService();
    }
    
    @Test
    @DisplayName("Calcular distancia entre dos puntos - Haversine correcta")
    void testCalcularDistancia() {
        // Coordenadas a ~1 km de Punata
        Double lat2 = -17.5600;
        Double lon2 = -65.8800;
        
        Double distancia = geoService.calcularDistancia(PUNATA_LAT, PUNATA_LON, lat2, lon2);
        
        assertNotNull(distancia);
        assertTrue(distancia > 0);
        assertTrue(distancia < 2); // Debe estar entre 0 y 2 km
        System.out.println("Distancia calculada: " + distancia + " km");
    }
    
    @Test
    @DisplayName("Calcular distancia con coordenadas nulas retorna null")
    void testCalcularDistanciaNull() {
        Double distancia = geoService.calcularDistancia(null, null, PUNATA_LAT, PUNATA_LON);
        assertNull(distancia);
    }
    
    @Test
    @DisplayName("Calcular distancia entre el mismo punto retorna 0")
    void testCalcularDistanciaIgualPunto() {
        Double distancia = geoService.calcularDistancia(PUNATA_LAT, PUNATA_LON, PUNATA_LAT, PUNATA_LON);
        assertNotNull(distancia);
        assertEquals(0.0, distancia, 0.01);
    }
    
    @Test
    @DisplayName("Verificar si comercio está en radio de 5 km")
    void testEstaEnRadio() {
        Double lat2 = -17.5600;
        Double lon2 = -65.8800;
        
        Boolean enRadio = geoService.estaEnRadio(PUNATA_LAT, PUNATA_LON, lat2, lon2, 5.0);
        
        assertTrue(enRadio);
    }
    
    @Test
    @DisplayName("Verificar que comercio fuera de radio retorna false")
    void testNoEstaEnRadio() {
        // Coordenadas a ~200 km de Punata
        Double lat2 = -17.0;
        Double lon2 = -65.0;
        
        Boolean enRadio = geoService.estaEnRadio(PUNATA_LAT, PUNATA_LON, lat2, lon2, 5.0);
        
        assertFalse(enRadio);
    }
    
    @Test
    @DisplayName("Calcular tiempo de viaje entre dos puntos")
    void testCalcularTiempoViaje() {
        Double lat2 = -17.5600;
        Double lon2 = -65.8800;
        
        Integer minutos = geoService.calcularTiempoViaje(PUNATA_LAT, PUNATA_LON, lat2, lon2);
        
        assertNotNull(minutos);
        assertTrue(minutos > 0);
        System.out.println("Tiempo estimado: " + minutos + " minutos");
    }
    
    @Test
    @DisplayName("Calcular acimut entre dos puntos")
    void testCalcularAcimut() {
        Double lat2 = -17.5600;
        Double lon2 = -65.8800;
        
        Double acimut = geoService.calcularAcimut(PUNATA_LAT, PUNATA_LON, lat2, lon2);
        
        assertNotNull(acimut);
        assertTrue(acimut >= 0 && acimut <= 360);
        System.out.println("Acimut: " + acimut + "°");
    }
    
    @Test
    @DisplayName("Obtener dirección cardinal por acimut")
    void testObtenerDireccionCardinal() {
        String norte = geoService.obtenerDireccionCardinal(0.0);
        assertEquals("Norte", norte);
        
        String este = geoService.obtenerDireccionCardinal(90.0);
        assertEquals("Este", este);
        
        String sur = geoService.obtenerDireccionCardinal(180.0);
        assertEquals("Sur", sur);
        
        String oeste = geoService.obtenerDireccionCardinal(270.0);
        assertEquals("Oeste", oeste);
    }
}
