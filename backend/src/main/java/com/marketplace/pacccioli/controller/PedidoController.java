package com.marketplace.pacccioli.controller;

import com.marketplace.pacccioli.dto.ApiResponseDTO;
import com.marketplace.pacccioli.dto.PedidoDTO;
import com.marketplace.pacccioli.service.PedidoService;
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
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<PedidoDTO>> crearPedido(
            @RequestBody Map<String, String> body,
            HttpServletRequest request) {
        try {
            Long usuarioId = (Long) request.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponseDTO<>(false, "Usuario no autenticado", null));
            }

            String metodoPago = body.getOrDefault("metodoPago", "EFECTIVO");
            PedidoDTO pedido = pedidoService.crearPedidoDesdeCarrito(usuarioId, metodoPago);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDTO<>(true, "Pedido creado exitosamente", pedido));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO<>(false, e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error al crear pedido", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al crear pedido: " + e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<PedidoDTO>>> obtenerPedidos(HttpServletRequest request) {
        try {
            Long usuarioId = (Long) request.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponseDTO<>(false, "Usuario no autenticado", null));
            }

            List<PedidoDTO> pedidos = pedidoService.obtenerPedidosUsuario(usuarioId);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Pedidos obtenidos", pedidos));
        } catch (Exception e) {
            log.error("Error al obtener pedidos", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al obtener pedidos: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<PedidoDTO>> obtenerPedido(@PathVariable Long id) {
        try {
            PedidoDTO pedido = pedidoService.obtenerPedido(id);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Pedido obtenido", pedido));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>(false, "Pedido no encontrado: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<ApiResponseDTO<PedidoDTO>> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            HttpServletRequest request) {
        try {
            Long usuarioId = (Long) request.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponseDTO<>(false, "Usuario no autenticado", null));
            }

            String estado = body.get("estado");
            PedidoDTO pedido = pedidoService.actualizarEstado(id, estado, usuarioId);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Estado actualizado a: " + estado, pedido));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponseDTO<>(false, e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error al actualizar estado del pedido", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error: " + e.getMessage(), null));
        }
    }

    @PostMapping("/{id}/pago/yape/generar")
    public ResponseEntity<ApiResponseDTO<String>> generarCodigoYape(
            @PathVariable Long id,
            HttpServletRequest request) {
        try {
            Long usuarioId = (Long) request.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponseDTO<>(false, "Usuario no autenticado", null));
            }

            String codigo = pedidoService.generarCodigoPagoYape(id, usuarioId);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Código Yape generado", codigo));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO<>(false, e.getMessage(), null));
        }
    }

    @PostMapping("/{id}/pago/yape/confirmar")
    public ResponseEntity<ApiResponseDTO<PedidoDTO>> confirmarPagoYape(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            HttpServletRequest request) {
        try {
            Long usuarioId = (Long) request.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponseDTO<>(false, "Usuario no autenticado", null));
            }

            String referencia = body.get("referenciaPago");
            String comprobanteUrl = body.get("comprobanteUrl");

            PedidoDTO pedido = pedidoService.confirmarPagoYape(id, usuarioId, referencia, comprobanteUrl);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Pago Yape registrado. Pendiente de verificación.", pedido));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/comercio")
    public ResponseEntity<ApiResponseDTO<List<PedidoDTO>>> obtenerPedidosComercio(HttpServletRequest request) {
        try {
            Long usuarioId = (Long) request.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponseDTO<>(false, "Usuario no autenticado", null));
            }

            List<PedidoDTO> pedidos = pedidoService.obtenerPedidosComerciante(usuarioId);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Pedidos del comercio obtenidos", pedidos));
        } catch (Exception e) {
            log.error("Error al obtener pedidos del comercio", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error: " + e.getMessage(), null));
        }
    }
}
