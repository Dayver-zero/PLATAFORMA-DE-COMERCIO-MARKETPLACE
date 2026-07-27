package com.marketplace.pacccioli.config;

import com.marketplace.pacccioli.model.*;
import com.marketplace.pacccioli.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final ComercioRepository comercioRepository;

    private static final String PASSWORD_HASH = "$2a$10$GRLdNijSQmLFd4Z9xB5h.eKl3l4YzHPyKR2p5l8ZXQz3QMzWEW.YO";

    @Override
    @Transactional
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            log.info("Base de datos ya tiene usuarios, se omite DataSeeder");
            return;
        }

        log.info("Insertando datos de prueba...");
        seedUsuarios();
        seedComercios();
        log.info("Datos de prueba insertados exitosamente");
    }

    private void seedUsuarios() {
        Usuario juan = new Usuario();
        juan.setUsername("juan@example.com");
        juan.setNombre("Juan Pérez");
        juan.setEmail("juan@example.com");
        juan.setPassword(PASSWORD_HASH);
        juan.setRol(Usuario.Rol.CLIENTE);
        juan.setLatitud(-17.5528);
        juan.setLongitud(-65.8756);
        juan.setRadioBusquedaKm(5);
        juan.setPreferencias("{\"favoriteCategories\":[\"Alimentos\",\"Ropa\"]}");
        juan.setHistorialBusqueda("[\"paraguas\",\"chaqueta\"]");
        juan.setActivo(true);
        juan.setFechaCreacion(LocalDateTime.now());
        juan.setFechaActualizacion(LocalDateTime.now());
        usuarioRepository.save(juan);

        Usuario maria = new Usuario();
        maria.setUsername("maria@example.com");
        maria.setNombre("María García");
        maria.setEmail("maria@example.com");
        maria.setPassword(PASSWORD_HASH);
        maria.setRol(Usuario.Rol.CLIENTE);
        maria.setLatitud(-17.5550);
        maria.setLongitud(-65.8700);
        maria.setRadioBusquedaKm(5);
        maria.setPreferencias("{\"favoriteCategories\":[\"Electrónica\"]}");
        maria.setHistorialBusqueda("[\"celular\"]");
        maria.setActivo(true);
        maria.setFechaCreacion(LocalDateTime.now());
        maria.setFechaActualizacion(LocalDateTime.now());
        usuarioRepository.save(maria);

        Usuario mario = new Usuario();
        mario.setUsername("mario@example.com");
        mario.setNombre("Mario López");
        mario.setEmail("mario@example.com");
        mario.setPassword(PASSWORD_HASH);
        mario.setRol(Usuario.Rol.COMERCIANTE);
        mario.setLatitud(-17.5528);
        mario.setLongitud(-65.8756);
        mario.setRadioBusquedaKm(0);
        mario.setPreferencias("{\"storeSpecialties\":[\"General\"]}");
        mario.setHistorialBusqueda("[]");
        mario.setActivo(true);
        mario.setFechaCreacion(LocalDateTime.now());
        mario.setFechaActualizacion(LocalDateTime.now());
        usuarioRepository.save(mario);

        Usuario patricia = new Usuario();
        patricia.setUsername("patricia@example.com");
        patricia.setNombre("Patricia Morales");
        patricia.setEmail("patricia@example.com");
        patricia.setPassword(PASSWORD_HASH);
        patricia.setRol(Usuario.Rol.COMERCIANTE);
        patricia.setLatitud(-17.5530);
        patricia.setLongitud(-65.8750);
        patricia.setRadioBusquedaKm(0);
        patricia.setPreferencias("{\"storeSpecialties\":[\"Alimentos\"]}");
        patricia.setHistorialBusqueda("[]");
        patricia.setActivo(true);
        patricia.setFechaCreacion(LocalDateTime.now());
        patricia.setFechaActualizacion(LocalDateTime.now());
        usuarioRepository.save(patricia);

        Usuario admin = new Usuario();
        admin.setUsername("admin@example.com");
        admin.setNombre("Admin System");
        admin.setEmail("admin@example.com");
        admin.setPassword(PASSWORD_HASH);
        admin.setRol(Usuario.Rol.ADMIN);
        admin.setLatitud(-17.5528);
        admin.setLongitud(-65.8756);
        admin.setRadioBusquedaKm(0);
        admin.setPreferencias("{\"role\":\"administrator\"}");
        admin.setHistorialBusqueda("[]");
        admin.setActivo(true);
        admin.setFechaCreacion(LocalDateTime.now());
        admin.setFechaActualizacion(LocalDateTime.now());
        usuarioRepository.save(admin);

        log.info("Usuarios insertados: 5");
    }

    private void seedComercios() {
        Usuario mario = usuarioRepository.findByEmail("mario@example.com").orElseThrow();
        Usuario patricia = usuarioRepository.findByEmail("patricia@example.com").orElseThrow();

        Comercio tiendaCentral = new Comercio();
        tiendaCentral.setNombre("Tienda Central Punata");
        tiendaCentral.setDescripcion("Tienda general con variedad de productos");
        tiendaCentral.setDireccion("Calle Principal 123, Punata");
        tiendaCentral.setTelefono("+591-4-123-4567");
        tiendaCentral.setHorarioAtencion("09:00-18:00 (L-V), 09:00-17:00 (S)");
        tiendaCentral.setCategoria(Comercio.Categoria.OTROS);
        tiendaCentral.setLatitud(-17.5528);
        tiendaCentral.setLongitud(-65.8756);
        tiendaCentral.setCalificacion(4.5);
        tiendaCentral.setNumeroReseñas(12);
        tiendaCentral.setActivo(true);
        tiendaCentral.setPropietario(mario);
        tiendaCentral.setFechaCreacion(LocalDateTime.now());
        tiendaCentral.setFechaActualizacion(LocalDateTime.now());
        comercioRepository.save(tiendaCentral);

        Comercio mercadoLocal = new Comercio();
        mercadoLocal.setNombre("Mercado Local");
        mercadoLocal.setDescripcion("Verduras, frutas y alimentos frescos");
        mercadoLocal.setDireccion("Mercado Principal, Punata");
        mercadoLocal.setTelefono("+591-4-123-4568");
        mercadoLocal.setHorarioAtencion("06:00-19:00 (L-D)");
        mercadoLocal.setCategoria(Comercio.Categoria.SUPERMERCADO);
        mercadoLocal.setLatitud(-17.5530);
        mercadoLocal.setLongitud(-65.8750);
        mercadoLocal.setCalificacion(4.7);
        mercadoLocal.setNumeroReseñas(18);
        mercadoLocal.setActivo(true);
        mercadoLocal.setPropietario(patricia);
        mercadoLocal.setFechaCreacion(LocalDateTime.now());
        mercadoLocal.setFechaActualizacion(LocalDateTime.now());
        comercioRepository.save(mercadoLocal);

        Comercio boutique = new Comercio();
        boutique.setNombre("Boutique María");
        boutique.setDescripcion("Ropa y accesorios de moda");
        boutique.setDireccion("Avenida Central 456, Punata");
        boutique.setTelefono("+591-4-123-4569");
        boutique.setHorarioAtencion("10:00-18:00 (L-S)");
        boutique.setCategoria(Comercio.Categoria.TIENDA_ROPA);
        boutique.setLatitud(-17.5535);
        boutique.setLongitud(-65.8760);
        boutique.setCalificacion(4.2);
        boutique.setNumeroReseñas(8);
        boutique.setActivo(true);
        boutique.setPropietario(mario);
        boutique.setFechaCreacion(LocalDateTime.now());
        boutique.setFechaActualizacion(LocalDateTime.now());
        comercioRepository.save(boutique);

        Comercio techstore = new Comercio();
        techstore.setNombre("TechStore Punata");
        techstore.setDescripcion("Electrónica y accesorios tecnológicos");
        techstore.setDireccion("Calle Comercio 789, Punata");
        techstore.setTelefono("+591-4-123-4570");
        techstore.setHorarioAtencion("09:00-19:00 (L-S)");
        techstore.setCategoria(Comercio.Categoria.ELECTRONICA);
        techstore.setLatitud(-17.5540);
        techstore.setLongitud(-65.8770);
        techstore.setCalificacion(4.3);
        techstore.setNumeroReseñas(10);
        techstore.setActivo(true);
        techstore.setPropietario(patricia);
        techstore.setFechaCreacion(LocalDateTime.now());
        techstore.setFechaActualizacion(LocalDateTime.now());
        comercioRepository.save(techstore);

        log.info("Comercios insertados: 4");
    }
}
