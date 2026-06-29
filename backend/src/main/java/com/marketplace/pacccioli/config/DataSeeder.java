package com.marketplace.pacccioli.config;

import com.marketplace.pacccioli.model.*;
import com.marketplace.pacccioli.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final ComercioRepository comercioRepository;
    private final ProductoRepository productoRepository;
    private final InteraccionRepository interaccionRepository;
    private final CarritoRepository carritoRepository;
    private final CarritoItemRepository carritoItemRepository;
    private final PedidoRepository pedidoRepository;
    private final PedidoItemRepository pedidoItemRepository;
    private final ReservaRepository reservaRepository;

    private static final String PASSWORD_HASH = "$2a$10$GRLdNijSQmLFd4Z9xB5h.eKl3l4YzHPyKR2p5l8ZXQz3QMzWEW.YO";

    @Override
    @Transactional
    public void run(String... args) {
        if (productoRepository.count() > 0) {
            log.info("Base de datos ya tiene productos, se omite DataSeeder");
            return;
        }

        log.info("Insertando datos de prueba...");
        limpiarDatos();
        seedUsuarios();
        seedComercios();
        seedProductos();
        seedInteracciones();
        log.info("Datos de prueba insertados exitosamente");
    }

    private void limpiarDatos() {
        carritoItemRepository.deleteAll();
        carritoRepository.deleteAll();
        pedidoItemRepository.deleteAll();
        pedidoRepository.deleteAll();
        reservaRepository.deleteAll();
        interaccionRepository.deleteAll();
        productoRepository.deleteAll();
        comercioRepository.deleteAll();
        usuarioRepository.deleteAll();
        log.info("Datos existentes eliminados");
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

    private void seedProductos() {
        Comercio tiendaCentral = comercioRepository.findById(1L).orElseThrow();
        Comercio mercadoLocal = comercioRepository.findById(2L).orElseThrow();
        Comercio boutique = comercioRepository.findById(3L).orElseThrow();
        Comercio techstore = comercioRepository.findById(4L).orElseThrow();

        productoRepository.save(crearProducto("Paraguas Premium", "Paraguas resistente al agua, 3 paneles apertura automática",
                45.99, 20, tiendaCentral, "[\"lluvia\",\"proteccion\",\"imprescindible\"]", 4.8, true, true, true));
        productoRepository.save(crearProducto("Chaqueta Térmica", "Chaqueta acolchada para abrigo en clima frío",
                89.99, 15, tiendaCentral, "[\"frío\",\"abrigo\",\"invierno\"]", 4.7, true, true, false));
        productoRepository.save(crearProducto("Sandalias Cómodas", "Sandalias para clima cálido",
                34.50, 30, tiendaCentral, "[\"calor\",\"verano\",\"comodidad\"]", 4.4, true, false, false));
        productoRepository.save(crearProducto("Gafas de Sol UV", "Gafas de protección solar",
                52.00, 25, tiendaCentral, "[\"soleado\",\"proteccion\",\"moda\"]", 4.6, true, true, true));

        productoRepository.save(crearProducto("Tomates Frescos", "Tomates de temporada, producción local",
                3.50, 100, mercadoLocal, "[\"alimentos\",\"fresco\",\"saludable\"]", 4.9, true, false, false));
        productoRepository.save(crearProducto("Lechuga Orgánica", "Lechuga verde orgánica, sin pesticidas",
                2.75, 80, mercadoLocal, "[\"alimentos\",\"fresco\",\"organico\"]", 4.8, true, false, false));
        productoRepository.save(crearProducto("Papas Locales", "Papas de variedad local, ideales para cocinar",
                1.50, 200, mercadoLocal, "[\"alimentos\",\"basico\",\"fresco\"]", 4.7, true, false, false));
        productoRepository.save(crearProducto("Manzanas Frescas", "Manzanas variedad roja, cosecha reciente",
                4.20, 150, mercadoLocal, "[\"alimentos\",\"fruta\",\"saludable\"]", 4.5, true, false, false));

        productoRepository.save(crearProducto("Suéter de Lana", "Suéter tejido de lana para abrigarse",
                65.00, 10, boutique, "[\"frío\",\"abrigo\",\"comodidad\"]", 4.6, true, true, true));
        productoRepository.save(crearProducto("Shorts Deportivos", "Shorts cómodos para clima cálido",
                28.99, 25, boutique, "[\"calor\",\"deporte\",\"verano\"]", 4.3, true, false, false));

        productoRepository.save(crearProducto("Power Bank 20000mAh", "Batería externa con carga rápida",
                55.00, 18, techstore, "[\"tecnologia\",\"practico\",\"viaje\"]", 4.7, true, true, true));
        productoRepository.save(crearProducto("Cable USB-C", "Cable de carga USB tipo C",
                12.50, 50, techstore, "[\"tecnologia\",\"basico\",\"accesorio\"]", 4.4, true, false, true));

        log.info("Productos insertados: 12");
    }

    private Producto crearProducto(String nombre, String descripcion, double precio, int stock,
                                    Comercio comercio, String etiquetas, double calificacion,
                                    boolean activo, boolean permiteReserva, boolean permitePagoAdelantado) {
        Producto p = new Producto();
        p.setNombre(nombre);
        p.setDescripcion(descripcion);
        p.setPrecio(BigDecimal.valueOf(precio));
        p.setStock(stock);
        p.setUrlImagen("https://via.placeholder.com/200?text=" + java.net.URLEncoder.encode(nombre, java.nio.charset.StandardCharsets.UTF_8));
        p.setCategoria(Producto.Categoria.OTROS);
        p.setEtiquetasInteligentes(etiquetas);
        p.setCalificacionPromedio(calificacion);
        p.setConteoVisualizaciones(0);
        p.setConteoCompras(0);
        p.setActivo(activo);
        p.setPermiteReserva(permiteReserva);
        p.setPermitePagoAdelantado(permitePagoAdelantado);
        p.setComercio(comercio);
        p.setFechaCreacion(LocalDateTime.now());
        p.setFechaActualizacion(LocalDateTime.now());
        return p;
    }

    private void seedInteracciones() {
        Usuario juan = usuarioRepository.findByEmail("juan@example.com").orElseThrow();
        Usuario maria = usuarioRepository.findByEmail("maria@example.com").orElseThrow();
        Producto paraguas = productoRepository.findById(1L).orElseThrow();
        Producto tomates = productoRepository.findById(5L).orElseThrow();
        Producto chaqueta = productoRepository.findById(2L).orElseThrow();
        Producto lechuga = productoRepository.findById(6L).orElseThrow();
        Producto papas = productoRepository.findById(7L).orElseThrow();
        Producto gafas = productoRepository.findById(4L).orElseThrow();
        Producto powerbank = productoRepository.findById(11L).orElseThrow();
        Producto cable = productoRepository.findById(12L).orElseThrow();

        interaccionRepository.save(crearInteraccion(juan, paraguas, Interaccion.TipoInteraccion.VISUALIZACION, Interaccion.FuenteInteraccion.RECOMENDACION, 45.99));
        interaccionRepository.save(crearInteraccion(juan, paraguas, Interaccion.TipoInteraccion.CLICK, Interaccion.FuenteInteraccion.FEED, 45.99));
        interaccionRepository.save(crearInteraccion(juan, paraguas, Interaccion.TipoInteraccion.COMPRA, Interaccion.FuenteInteraccion.DIRECTO, 45.99));
        interaccionRepository.save(crearInteraccion(juan, tomates, Interaccion.TipoInteraccion.VISUALIZACION, Interaccion.FuenteInteraccion.BUSQUEDA, 3.50));
        interaccionRepository.save(crearInteraccion(juan, tomates, Interaccion.TipoInteraccion.COMPRA, Interaccion.FuenteInteraccion.DIRECTO, 3.50));
        interaccionRepository.save(crearInteraccion(juan, chaqueta, Interaccion.TipoInteraccion.VISUALIZACION, Interaccion.FuenteInteraccion.RECOMENDACION, 89.99));
        interaccionRepository.save(crearInteraccion(maria, powerbank, Interaccion.TipoInteraccion.VISUALIZACION, Interaccion.FuenteInteraccion.BUSQUEDA, 55.00));
        interaccionRepository.save(crearInteraccion(maria, powerbank, Interaccion.TipoInteraccion.CLICK, Interaccion.FuenteInteraccion.FEED, 55.00));
        interaccionRepository.save(crearInteraccion(maria, cable, Interaccion.TipoInteraccion.VISUALIZACION, Interaccion.FuenteInteraccion.RECOMENDACION, 12.50));
        interaccionRepository.save(crearInteraccion(maria, tomates, Interaccion.TipoInteraccion.VISUALIZACION, Interaccion.FuenteInteraccion.BUSQUEDA, 3.50));
        interaccionRepository.save(crearInteraccion(juan, lechuga, Interaccion.TipoInteraccion.VISUALIZACION, Interaccion.FuenteInteraccion.RECOMENDACION, 2.75));
        interaccionRepository.save(crearInteraccion(juan, papas, Interaccion.TipoInteraccion.VISUALIZACION, Interaccion.FuenteInteraccion.RECOMENDACION, 1.50));
        interaccionRepository.save(crearInteraccion(juan, gafas, Interaccion.TipoInteraccion.VISUALIZACION, Interaccion.FuenteInteraccion.FEED, 52.00));

        log.info("Interacciones insertadas: 13");
    }

    private Interaccion crearInteraccion(Usuario usuario, Producto producto,
                                          Interaccion.TipoInteraccion tipo,
                                          Interaccion.FuenteInteraccion fuente,
                                          double precio) {
        Interaccion i = new Interaccion();
        i.setUsuario(usuario);
        i.setProducto(producto);
        i.setTipoInteraccion(tipo);
        i.setFuente(fuente);
        i.setPrecioMomento(BigDecimal.valueOf(precio));
        i.setLatitudUsuario(usuario.getLatitud());
        i.setLongitudUsuario(usuario.getLongitud());
        i.setFechaInteraccion(LocalDateTime.now());
        return i;
    }
}