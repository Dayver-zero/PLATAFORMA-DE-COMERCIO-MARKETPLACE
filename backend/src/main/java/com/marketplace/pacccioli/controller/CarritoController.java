package com.marketplace.pacccioli.controller;

import com.marketplace.pacccioli.dto.ApiResponseDTO;
import com.marketplace.pacccioli.dto.CarritoDTO;
import com.marketplace.pacccioli.dto.CarritoItemDTO;
import com.marketplace.pacccioli.service.CarritoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<CarritoDTO>> obtenerCarrito(HttpServletRequest request) {
        try {
            Long usuarioId = (Long) request.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponseDTO<>(false, "Usuario no autenticado", null));
            }
            CarritoDTO carrito = carritoService.obtenerCarrito(usuarioId);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Carrito obtenido exitosamente", carrito));
        } catch (Exception e) {
            log.error("Error al obtener carrito", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al obtener carrito: " + e.getMessage(), null));
        }
    }

    @GetMapping("/conteo")
    public ResponseEntity<ApiResponseDTO<Integer>> contarItems(HttpServletRequest request) {
        try {
            Long usuarioId = (Long) request.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponseDTO<>(false, "Usuario no autenticado", 0));
            }
            int conteo = carritoService.contarItems(usuarioId);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Conteo obtenido", conteo));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Conteo obtenido", 0));
        }
    }

    @PostMapping("/items")
    public ResponseEntity<ApiResponseDTO<CarritoItemDTO>> agregarItem(
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

            CarritoItemDTO item = carritoService.agregarItem(usuarioId, productoId, cantidad);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDTO<>(true, "Item agregado al carrito", item));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO<>(false, e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error al agregar item al carrito", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al agregar item: " + e.getMessage(), null));
        }
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<ApiResponseDTO<CarritoItemDTO>> actualizarCantidad(
            @PathVariable Long itemId,
            @RequestBody Map<String, Object> body,
            HttpServletRequest request) {
        try {
            Long usuarioId = (Long) request.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponseDTO<>(false, "Usuario no autenticado", null));
            }

            Integer cantidad = Integer.valueOf(body.get("cantidad").toString());
            CarritoItemDTO item = carritoService.actualizarCantidad(usuarioId, itemId, cantidad);

            if (item == null) {
                return ResponseEntity.ok(new ApiResponseDTO<>(true, "Item eliminado del carrito", null));
            }

            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Cantidad actualizada", item));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO<>(false, e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error al actualizar cantidad", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al actualizar cantidad: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<ApiResponseDTO<Void>> eliminarItem(
            @PathVariable Long itemId,
            HttpServletRequest request) {
        try {
            Long usuarioId = (Long) request.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponseDTO<>(false, "Usuario no autenticado", null));
            }

            carritoService.eliminarItem(usuarioId, itemId);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Item eliminado del carrito", null));
        } catch (Exception e) {
            log.error("Error al eliminar item", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al eliminar item: " + e.getMessage(), null));
        }
    }

    @DeleteMapping
    public ResponseEntity<ApiResponseDTO<Void>> limpiarCarrito(HttpServletRequest request) {
        try {
            Long usuarioId = (Long) request.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponseDTO<>(false, "Usuario no autenticado", null));
            }

            carritoService.limpiarCarrito(usuarioId);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Carrito limpiado", null));
        } catch (Exception e) {
            log.error("Error al limpiar carrito", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al limpiar carrito: " + e.getMessage(), null));
        }
    }
}
