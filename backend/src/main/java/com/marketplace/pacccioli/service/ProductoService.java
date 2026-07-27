package com.marketplace.pacccioli.service;

import com.marketplace.pacccioli.model.Producto;
import com.marketplace.pacccioli.repository.CarritoItemRepository;
import com.marketplace.pacccioli.repository.PedidoItemRepository;
import com.marketplace.pacccioli.repository.ProductoRepository;
import com.marketplace.pacccioli.repository.ReservaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CarritoItemRepository carritoItemRepository;
    private final PedidoItemRepository pedidoItemRepository;
    private final ReservaRepository reservaRepository;

    @Transactional
    public void eliminarProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + id));

        carritoItemRepository.deleteByProductoId(id);
        pedidoItemRepository.deleteByProductoId(id);
        reservaRepository.deleteByProductoId(id);

        productoRepository.delete(producto);
    }
}
