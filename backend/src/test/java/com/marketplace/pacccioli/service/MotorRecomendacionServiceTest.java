package com.marketplace.pacccioli.service;

import com.marketplace.pacccioli.dto.ProductoDTO;
import com.marketplace.pacccioli.model.Producto;
import com.marketplace.pacccioli.model.Usuario;
import com.marketplace.pacccioli.repository.InteraccionRepository;
import com.marketplace.pacccioli.repository.ProductoRepository;
import com.marketplace.pacccioli.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("MotorRecomendacionService Tests")
@ExtendWith(MockitoExtension.class)
class MotorRecomendacionServiceTest {
    
    private MotorRecomendacionService motorRecomendacion;
    
    @Mock
    private GeolocalizacionService geolocalizacionService;
    
    @Mock
    private ClimaService climaService;
    
    @Mock
    private ProductoRepository productoRepository;
    
    @Mock
    private UsuarioRepository usuarioRepository;
    
    @Mock
    private InteraccionRepository interaccionRepository;
    
    @BeforeEach
    void setUp() {
        motorRecomendacion = new MotorRecomendacionService();
        ReflectionTestUtils.setField(motorRecomendacion, "geolocalizacionService", geolocalizacionService);
        ReflectionTestUtils.setField(motorRecomendacion, "climaService", climaService);
        ReflectionTestUtils.setField(motorRecomendacion, "productoRepository", productoRepository);
        ReflectionTestUtils.setField(motorRecomendacion, "usuarioRepository", usuarioRepository);
        ReflectionTestUtils.setField(motorRecomendacion, "interaccionRepository", interaccionRepository);
    }
    
    @Test
    @DisplayName("Generar recomendaciones retorna lista no nula")
    void testGenerarRecomendaciones() {
        // Mock usuario
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Test User");
        
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(productoRepository.findByActivoTrueOrderByConteoVisualizacionesDesc())
                .thenReturn(new ArrayList<>());
        
        List<MotorRecomendacionService.RecomendacionDTO> recomendaciones = 
            motorRecomendacion.generarRecomendaciones(1L, -17.5528, -65.8756);
        
        assertNotNull(recomendaciones);
        assertInstanceOf(List.class, recomendaciones);
    }
    
    @Test
    @DisplayName("Generar recomendaciones para usuario inexistente retorna lista vacía")
    void testGenerarRecomendacionesUsuarioNoExiste() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());
        
        List<MotorRecomendacionService.RecomendacionDTO> recomendaciones = 
            motorRecomendacion.generarRecomendaciones(999L, -17.5528, -65.8756);
        
        assertTrue(recomendaciones.isEmpty());
    }
    
    @Test
    @DisplayName("Recomendar por clima retorna lista de productos")
    void testRecomendarPorClima() {
        ClimaService.ClimaDTO clima = new ClimaService.ClimaDTO();
        clima.setTemperatura(18.0);
        clima.setCondicion("rain");
        
        when(climaService.obtenerClimaPorCoordenadas(-17.5528, -65.8756))
                .thenReturn(clima);
        when(productoRepository.findByActivoTrueOrderByConteoVisualizacionesDesc())
                .thenReturn(new ArrayList<>());
        
        List<ProductoDTO> recomendaciones = 
            motorRecomendacion.recomendarPorClima(-17.5528, -65.8756);
        
        assertNotNull(recomendaciones);
        assertInstanceOf(List.class, recomendaciones);
    }
    
    @Test
    @DisplayName("Recomendar por ubicación retorna lista de productos")
    void testRecomendarPorUbicacion() {
        when(productoRepository.findByActivoTrueOrderByConteoVisualizacionesDesc())
                .thenReturn(new ArrayList<>());
        List<ProductoDTO> recomendaciones = 
            motorRecomendacion.recomendarPorUbicacion(-17.5528, -65.8756, 5.0);
        
        assertNotNull(recomendaciones);
        assertInstanceOf(List.class, recomendaciones);
    }
    
    @Test
    @DisplayName("RecomendacionDTO contiene puntajes válidos")
    void testRecomendacionDTOValido() {
        MotorRecomendacionService.RecomendacionDTO recomendacion = 
            new MotorRecomendacionService.RecomendacionDTO();
        
        recomendacion.setProductoId(1L);
        recomendacion.setNombreProducto("Paraguas");
        recomendacion.setPuntajeClima(1.0);
        recomendacion.setPuntajeUbicacion(0.8);
        recomendacion.setPuntajeHistorial(0.5);
        recomendacion.setPuntajeTotal(0.76);
        
        assertEquals(1L, recomendacion.getProductoId());
        assertEquals("Paraguas", recomendacion.getNombreProducto());
        assertTrue(recomendacion.getPuntajeTotal() >= 0 && recomendacion.getPuntajeTotal() <= 1);
    }
}
