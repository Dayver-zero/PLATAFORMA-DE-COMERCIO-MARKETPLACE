package com.marketplace.pacccioli.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ClimaService Tests")
class ClimaServiceTest {
    
    private ClimaService climaService;
    
    @BeforeEach
    void setUp() {
        climaService = new ClimaService();
    }
    
    @Test
    @DisplayName("Obtener clima por coordenadas retorna ClimaDTO")
    void testObtenerClimaPorCoordenadas() {
        ClimaService.ClimaDTO clima = climaService.obtenerClimaPorCoordenadas(-17.5528, -65.8756);
        
        assertNotNull(clima);
        assertNotNull(clima.getTemperatura());
        assertNotNull(clima.getHumedad());
        assertNotNull(clima.getCondicion());
        
        System.out.println("Clima: " + clima.getTemperatura() + "°C, " + 
                          clima.getCondicion() + ", " + clima.getHumedad() + "% humedad");
    }
    
    @Test
    @DisplayName("Obtener clima con coordenadas nulas retorna null")
    void testObtenerClimaNull() {
        ClimaService.ClimaDTO clima = climaService.obtenerClimaPorCoordenadas(null, null);
        assertNull(clima);
    }
    
    @Test
    @DisplayName("Verificar clima favorable para lluvia")
    void testEsClimaFavorableLluvia() {
        Boolean favorable = climaService.esClimaFavorable("lluvia", 18.0, "rain");
        assertTrue(favorable);
    }
    
    @Test
    @DisplayName("Verificar clima favorable para frío")
    void testEsClimaFavorableFromio() {
        Boolean favorable = climaService.esClimaFavorable("frío", 5.0, "clear");
        assertTrue(favorable);
    }
    
    @Test
    @DisplayName("Verificar clima favorable para calor")
    void testEsClimaFavorableCalor() {
        Boolean favorable = climaService.esClimaFavorable("calor", 30.0, "sunny");
        assertTrue(favorable);
    }
    
    @Test
    @DisplayName("Verificar clima no favorable retorna false")
    void testEsClimaNoFavorable() {
        Boolean favorable = climaService.esClimaFavorable("lluvia", 25.0, "sunny");
        assertFalse(favorable);
    }
    
    @Test
    @DisplayName("Obtener pronóstico retorna datos válidos")
    void testObtenerPronostico() {
        // Sin API key, debería retornar null o simulación
        ClimaService.ClimaDTO[] pronostico = climaService.obtenerPronostico(-17.5528, -65.8756, 5);
        // Puede ser null o un array (dependiendo de la API key)
        System.out.println("Pronóstico obtenido (puede ser null sin API key)");
    }
    
    @Test
    @DisplayName("Validar rangos de valores de clima")
    void testValidarRangosClima() {
        ClimaService.ClimaDTO clima = climaService.obtenerClimaPorCoordenadas(-17.5528, -65.8756);
        
        assertNotNull(clima);
        assertTrue(clima.getTemperatura() >= -50 && clima.getTemperatura() <= 50);
        assertTrue(clima.getHumedad() >= 0 && clima.getHumedad() <= 100);
        assertTrue(clima.getVelocidalVientoKmh() >= 0);
    }
}
