package com.marketplace.pacccioli.controller;

import com.marketplace.pacccioli.dto.ApiResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/upload")
public class UploadController {

    private static final Set<String> EXTENSIONES_PERMITIDAS = Set.of("jpg", "jpeg", "png", "webp", "gif");
    private static final long MAX_SIZE_BYTES = 5 * 1024 * 1024; // 5MB

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> subirArchivo(@RequestParam("archivo") MultipartFile archivo) {
        try {
            if (archivo.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponseDTO<>(false, "No se envio ningun archivo", null));
            }

            String nombreOriginal = archivo.getOriginalFilename();
            if (nombreOriginal == null || nombreOriginal.isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponseDTO<>(false, "Nombre de archivo invalido", null));
            }

            String extension = "";
            int idx = nombreOriginal.lastIndexOf('.');
            if (idx > 0) {
                extension = nombreOriginal.substring(idx + 1).toLowerCase();
            }

            if (!EXTENSIONES_PERMITIDAS.contains(extension)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponseDTO<>(false, "Tipo de archivo no permitido. Solo: jpg, jpeg, png, webp, gif",
                                List.of("Extension recibida: " + extension)));
            }

            if (archivo.getSize() > MAX_SIZE_BYTES) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponseDTO<>(false, "El archivo excede el tamaño maximo de 5MB", null));
            }

            String nombreUnico = UUID.randomUUID().toString() + "." + extension;
            Path rutaDirectorio = Paths.get(uploadDir, "productos").toAbsolutePath().normalize();
            Files.createDirectories(rutaDirectorio);
            Path rutaArchivo = rutaDirectorio.resolve(nombreUnico);
            archivo.transferTo(rutaArchivo.toFile());

            String url = "/uploads/productos/" + nombreUnico;

            log.info("Archivo subido exitosamente: {} -> {}", nombreOriginal, url);

            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Archivo subido exitosamente",
                    Map.of("url", url, "nombreOriginal", nombreOriginal)));

        } catch (IOException e) {
            log.error("Error al subir archivo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al guardar el archivo",
                            List.of(e.getMessage())));
        } catch (Exception e) {
            log.error("Error inesperado al subir archivo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Error al subir archivo",
                            List.of(e.getMessage())));
        }
    }
}
