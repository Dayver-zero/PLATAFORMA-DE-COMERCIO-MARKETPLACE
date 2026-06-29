package com.marketplace.pacccioli.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Servicio para obtener información del clima desde OpenWeather API
 * Proporciona datos de temperatura, condiciones, humedad y viento
 */
@Service
@Slf4j
public class ClimaService {
    
    @Value("${openweather.api.key:}")
    private String openWeatherApiKey;
    
    private static final String API_BASE_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String API_FORECAST_URL = "https://api.openweathermap.org/data/2.5/forecast";
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    /**
     * Obtiene el clima actual basado en coordenadas GPS
     */
    public ClimaDTO obtenerClimaPorCoordenadas(Double latitud, Double longitud) {
        try {
            if (latitud == null || longitud == null) {
                log.warn("Coordenadas nulas recibidas");
                return null;
            }
            
            if (openWeatherApiKey == null || openWeatherApiKey.isEmpty()) {
                log.warn("OpenWeather API Key no configurada en application.properties");
                return generarClimaSimulado(latitud, longitud);
            }
            
            String url = String.format("%s?lat=%f&lon=%f&appid=%s&units=metric&lang=es",
                    API_BASE_URL, latitud, longitud, openWeatherApiKey);
            
            log.debug("Llamando a OpenWeather API por coordenadas");
            Map<String, Object> respuesta = restTemplate.getForObject(url, Map.class);
            return parsearRespuestaClima(respuesta, latitud, longitud);
            
        } catch (RestClientException e) {
            log.error("Error al consultar OpenWeather por coordenadas: {}", e.getMessage());
            return generarClimaSimulado(latitud, longitud);
        } catch (Exception e) {
            log.error("Error al obtener clima por coordenadas", e);
            return null;
        }
    }
    
    /**
     * Obtiene el clima actual basado en el nombre de una ciudad
     */
    public ClimaDTO obtenerClimaPorCiudad(String ciudad) {
        try {
            if (ciudad == null || ciudad.isEmpty()) {
                log.warn("Nombre de ciudad vacío");
                return null;
            }
            
            if (openWeatherApiKey == null || openWeatherApiKey.isEmpty()) {
                log.warn("OpenWeather API Key no configurada");
                return null;
            }
            
            String url = String.format("%s?q=%s&appid=%s&units=metric&lang=es",
                    API_BASE_URL, ciudad, openWeatherApiKey);
            
            log.debug("Llamando a OpenWeather API por ciudad: {}", ciudad);
            Map<String, Object> respuesta = restTemplate.getForObject(url, Map.class);
            
            Double latitud = extraerCoordenada(respuesta, "lat");
            Double longitud = extraerCoordenada(respuesta, "lon");
            return parsearRespuestaClima(respuesta, latitud, longitud);
            
        } catch (RestClientException e) {
            log.error("Error al consultar OpenWeather por ciudad: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("Error al obtener clima por ciudad", e);
            return null;
        }
    }
    
    /**
     * Obtiene el pronóstico del clima para los próximos días
     */
    public ClimaDTO[] obtenerPronostico(Double latitud, Double longitud, Integer dias) {
        try {
            if (latitud == null || longitud == null) {
                log.warn("Coordenadas nulas para pronóstico");
                return null;
            }
            
            if (openWeatherApiKey == null || openWeatherApiKey.isEmpty()) {
                log.warn("OpenWeather API Key no configurada");
                return null;
            }
            
            int diasSolicitados = (dias == null || dias < 1) ? 5 : Math.min(dias, 5);
            
            String url = String.format("%s?lat=%f&lon=%f&appid=%s&units=metric&lang=es",
                    API_FORECAST_URL, latitud, longitud, openWeatherApiKey);
            
            log.debug("Llamando a OpenWeather Forecast API");
            Map<String, Object> respuesta = restTemplate.getForObject(url, Map.class);
            return parsearPronostico(respuesta, latitud, longitud, diasSolicitados);
            
        } catch (RestClientException e) {
            log.error("Error al consultar pronóstico OpenWeather: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("Error al obtener pronóstico", e);
            return null;
        }
    }
    
    /**
     * Determina si las condiciones climáticas son favorables para un tipo de producto
     */
    public Boolean esClimaFavorable(String etiquetasInteligentes, Double temperatura, String condicion) {
        try {
            if (etiquetasInteligentes == null || condicion == null) {
                return false;
            }
            
            String etiquetasLower = etiquetasInteligentes.toLowerCase();
            String condicionLower = condicion.toLowerCase();
            
            if (etiquetasLower.contains("lluvia") && (condicionLower.contains("rain") || condicionLower.contains("lluvia"))) {
                return true;
            }
            
            if (etiquetasLower.contains("frío") && temperatura != null && temperatura < 10) {
                return true;
            }
            
            if (etiquetasLower.contains("calor") && temperatura != null && temperatura > 25) {
                return true;
            }
            
            if (etiquetasLower.contains("soleado") && (condicionLower.contains("clear") || condicionLower.contains("sunny") || condicionLower.contains("despejado"))) {
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            log.error("Error al verificar clima favorable", e);
            return false;
        }
    }
    
    @SuppressWarnings("unchecked")
    private ClimaDTO parsearRespuestaClima(Map<String, Object> respuesta, Double latitud, Double longitud) {
        if (respuesta == null) {
            return null;
        }
        
        ClimaDTO clima = new ClimaDTO();
        
        Map<String, Object> main = (Map<String, Object>) respuesta.get("main");
        if (main != null) {
            clima.setTemperatura(toDouble(main.get("temp")));
            clima.setSensacionTermica(toDouble(main.get("feels_like")));
            clima.setHumedad(toInteger(main.get("humidity")));
            clima.setPresionHPa(toInteger(main.get("pressure")));
        }
        
        List<Map<String, Object>> weather = (List<Map<String, Object>>) respuesta.get("weather");
        if (weather != null && !weather.isEmpty()) {
            Map<String, Object> condicionActual = weather.get(0);
            clima.setCondicion(capitalizar((String) condicionActual.get("description")));
            clima.setIcono((String) condicionActual.get("icon"));
        }
        
        Map<String, Object> wind = (Map<String, Object>) respuesta.get("wind");
        if (wind != null && wind.get("speed") != null) {
            clima.setVelocidalVientoKmh(toDouble(wind.get("speed")) * 3.6);
        }
        
        if (respuesta.get("visibility") != null) {
            clima.setVisibilidadMetros(toInteger(respuesta.get("visibility")));
        }
        
        clima.setCiudad((String) respuesta.get("name"));
        
        Map<String, Object> sys = (Map<String, Object>) respuesta.get("sys");
        if (sys != null) {
            clima.setPais((String) sys.get("country"));
        }
        
        Map<String, Object> coord = (Map<String, Object>) respuesta.get("coord");
        if (coord != null) {
            clima.setLatitud(toDouble(coord.get("lat")));
            clima.setLongitud(toDouble(coord.get("lon")));
        } else {
            clima.setLatitud(latitud);
            clima.setLongitud(longitud);
        }
        
        clima.setTimestamp(System.currentTimeMillis());
        return clima;
    }
    
    @SuppressWarnings("unchecked")
    private ClimaDTO[] parsearPronostico(Map<String, Object> respuesta, Double latitud, Double longitud, int dias) {
        if (respuesta == null) {
            return null;
        }
        
        List<Map<String, Object>> lista = (List<Map<String, Object>>) respuesta.get("list");
        if (lista == null || lista.isEmpty()) {
            return new ClimaDTO[0];
        }
        
        Set<LocalDate> diasAgregados = new LinkedHashSet<>();
        List<ClimaDTO> pronostico = new ArrayList<>();
        
        for (Map<String, Object> item : lista) {
            Long timestamp = toLong(item.get("dt"));
            if (timestamp == null) {
                continue;
            }
            
            LocalDate fecha = Instant.ofEpochSecond(timestamp)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            
            if (diasAgregados.contains(fecha)) {
                continue;
            }
            
            ClimaDTO dia = parsearRespuestaClima(item, latitud, longitud);
            if (dia != null) {
                dia.setTimestamp(timestamp * 1000);
                pronostico.add(dia);
                diasAgregados.add(fecha);
            }
            
            if (pronostico.size() >= dias) {
                break;
            }
        }
        
        return pronostico.toArray(new ClimaDTO[0]);
    }
    
    @SuppressWarnings("unchecked")
    private Double extraerCoordenada(Map<String, Object> respuesta, String campo) {
        if (respuesta == null) {
            return null;
        }
        Map<String, Object> coord = (Map<String, Object>) respuesta.get("coord");
        if (coord == null) {
            return null;
        }
        return toDouble(coord.get(campo));
    }
    
    private Double toDouble(Object valor) {
        if (valor == null) {
            return null;
        }
        if (valor instanceof Number number) {
            return number.doubleValue();
        }
        return Double.parseDouble(valor.toString());
    }
    
    private Integer toInteger(Object valor) {
        if (valor == null) {
            return null;
        }
        if (valor instanceof Number number) {
            return number.intValue();
        }
        return Integer.parseInt(valor.toString());
    }
    
    private Long toLong(Object valor) {
        if (valor == null) {
            return null;
        }
        if (valor instanceof Number number) {
            return number.longValue();
        }
        return Long.parseLong(valor.toString());
    }
    
    private String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto;
        }
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }
    
    /**
     * Fallback para desarrollo cuando no hay API key o falla la consulta
     */
    private ClimaDTO generarClimaSimulado(Double latitud, Double longitud) {
        ClimaDTO clima = new ClimaDTO();
        clima.setTemperatura(18.0);
        clima.setHumedad(65);
        clima.setCondicion("Parcialmente nublado");
        clima.setIcono("02d");
        clima.setVelocidalVientoKmh(12.0);
        clima.setLatitud(latitud);
        clima.setLongitud(longitud);
        clima.setCiudad("Punata");
        clima.setPais("Bolivia");
        clima.setTimestamp(System.currentTimeMillis());
        
        log.info("Usando datos simulados de clima");
        return clima;
    }
    
    /**
     * DTO para datos del clima
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClimaDTO {
        private Double temperatura;
        private Double sensacionTermica;
        private Integer humedad;
        private String condicion;
        private String icono;
        private Double velocidalVientoKmh;
        private Integer visibilidadMetros;
        private Integer presionHPa;
        private Double latitud;
        private Double longitud;
        private String ciudad;
        private String pais;
        private Long timestamp;
    }
}
