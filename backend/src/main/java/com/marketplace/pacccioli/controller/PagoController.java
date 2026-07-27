package com.marketplace.pacccioli.controller;

import com.marketplace.pacccioli.dto.ApiResponseDTO;
import com.marketplace.pacccioli.dto.PagoDTO;
import com.marketplace.pacccioli.service.PagoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<ApiResponseDTO<List<PagoDTO>>> obtenerPagosPorPedido(
            @PathVariable Long pedidoId) {
        try {
            List<PagoDTO> pagos = pagoService.obtenerPagosPorPedido(pedidoId);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Pagos obtenidos", pagos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error: " + e.getMessage(), null));
        }
    }
}
