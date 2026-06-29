package com.marketplace.pacccioli.controller;

import com.marketplace.pacccioli.dto.ApiResponseDTO;
import com.marketplace.pacccioli.dto.ReservaDTO;
import com.marketplace.pacccioli.service.ReservaService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<ReservaDTO>> crearReserva(
            @RequestBody Map<String, Object> body,
            HttpServletRequest request) {
        try {
            Long usuarioId = (Long) request.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponseDTO<>(false, "Usuario no autenticado", null));
            }

            Long productoId = Long.valueOf(body.get("productoId").toString());
            Integer cantidad = body.containsKey("cantidad") ?
                    Integer.valueOf(body.get("cantidad").toString()) : 1;
            String notas = body.containsKey("notas") ? body.get("notas").toString() : null;

            ReservaDTO reserva = reservaService.crearReserva(usuarioId, productoId, cantidad, notas);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDTO<>(true, "Reserva creada exitosamente", reserva));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO<>(false, e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error al crear reserva", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al crear reserva: " + e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<ReservaDTO>>> obtenerReservas(HttpServletRequest request) {
        try {
            Long usuarioId = (Long) request.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponseDTO<>(false, "Usuario no autenticado", null));
            }

            List<ReservaDTO> reservas = reservaService.obtenerReservasUsuario(usuarioId);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Reservas obtenidas", reservas));
        } catch (Exception e) {
            log.error("Error al obtener reservas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ReservaDTO>> obtenerReserva(@PathVariable Long id) {
        try {
            ReservaDTO reserva = reservaService.obtenerReserva(id);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Reserva obtenida", reserva));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>(false, "Reserva no encontrada", null));
        }
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<ApiResponseDTO<ReservaDTO>> cancelarReserva(
            @PathVariable Long id,
            HttpServletRequest request) {
        try {
            Long usuarioId = (Long) request.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponseDTO<>(false, "Usuario no autenticado", null));
            }

            ReservaDTO reserva = reservaService.cancelarReserva(id, usuarioId);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Reserva cancelada", reserva));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponseDTO<>(false, e.getMessage(), null));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO<>(false, e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error al cancelar reserva", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/completar")
    public ResponseEntity<ApiResponseDTO<ReservaDTO>> completarReserva(
            @PathVariable Long id,
            HttpServletRequest request) {
        try {
            Long usuarioId = (Long) request.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponseDTO<>(false, "Usuario no autenticado", null));
            }

            ReservaDTO reserva = reservaService.completarReserva(id, usuarioId);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Reserva completada", reserva));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponseDTO<>(false, e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error al completar reserva", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error: " + e.getMessage(), null));
        }
    }

    @GetMapping("/comercio")
    public ResponseEntity<ApiResponseDTO<List<ReservaDTO>>> obtenerReservasComercio(HttpServletRequest request) {
        try {
            Long usuarioId = (Long) request.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponseDTO<>(false, "Usuario no autenticado", null));
            }

            List<ReservaDTO> reservas = reservaService.obtenerReservasComercio(usuarioId);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Reservas del comercio obtenidas", reservas));
        } catch (Exception e) {
            log.error("Error al obtener reservas del comercio", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error: " + e.getMessage(), null));
        }
    }
}
