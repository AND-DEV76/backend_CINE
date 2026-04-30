package com.cinemaroyale.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import com.cinemaroyale.dto.PeliculaRequestDTO;
import com.cinemaroyale.dto.PeliculaResponseDTO;
import com.cinemaroyale.model.Clasificacion;
import com.cinemaroyale.model.Pelicula;
import com.cinemaroyale.model.Usuario;
import com.cinemaroyale.repository.ClasificacionRepository;
import com.cinemaroyale.repository.PeliculaGeneroRepository;
import com.cinemaroyale.repository.PeliculaRepository;
import com.cinemaroyale.repository.UsuarioRepository;


@Service
public class PeliculaServiceImpl implements PeliculaService {

    @Autowired
    private PeliculaRepository peliculaRepository;

    @Autowired
    private ClasificacionRepository clasificacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
private PeliculaGeneroRepository peliculaGeneroRepository;


    @Override
    public PeliculaResponseDTO crear(PeliculaRequestDTO dto) {

        Clasificacion clasificacion = clasificacionRepository.findById(dto.getIdClasificacion())
                .orElseThrow(() -> new RuntimeException("Clasificación no encontrada"));

        Usuario usuario = usuarioRepository.findById(dto.getCreadoPor())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String nombreImagen = guardarImagen(dto.getPoster());

        Pelicula pelicula = new Pelicula();
        pelicula.setNombre(dto.getNombre());
        pelicula.setDuracion(dto.getDuracion());
        pelicula.setClasificacion(clasificacion);
        pelicula.setDescripcion(dto.getDescripcion());
        pelicula.setPoster(nombreImagen);
        pelicula.setCreadoPor(usuario);

        peliculaRepository.save(pelicula);

        return mapToDTO(pelicula);
    }

    @Override
    public List<PeliculaResponseDTO> listar() {
        return peliculaRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PeliculaResponseDTO obtenerPorId(Integer id) {
        Pelicula pelicula = peliculaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Película no encontrada"));

        return mapToDTO(pelicula);
    }

    @Override
    public PeliculaResponseDTO actualizar(Integer id, PeliculaRequestDTO dto) {

        Pelicula pelicula = peliculaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Película no encontrada"));

        Clasificacion clasificacion = clasificacionRepository.findById(dto.getIdClasificacion())
                .orElseThrow(() -> new RuntimeException("Clasificación no encontrada"));

        Usuario usuario = usuarioRepository.findById(dto.getCreadoPor())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        pelicula.setNombre(dto.getNombre());
        pelicula.setDuracion(dto.getDuracion());
        pelicula.setClasificacion(clasificacion);
        pelicula.setDescripcion(dto.getDescripcion());
        pelicula.setCreadoPor(usuario);

        if (dto.getPoster() != null && !dto.getPoster().isEmpty()) {
            String nombreImagen = guardarImagen(dto.getPoster());
            pelicula.setPoster(nombreImagen);
        }

        peliculaRepository.save(pelicula);

        return mapToDTO(pelicula);
    }

    @Override
    public void eliminar(Integer id) {

           // 🔥 1. BORRAR RELACIONES (tabla intermedia)
    peliculaGeneroRepository.deleteByPeliculaId(id);
        peliculaRepository.deleteById(id);
    }

    // 🔥 GUARDAR IMAGEN
   private String guardarImagen(MultipartFile archivo) {

    if (archivo == null || archivo.isEmpty()) {
        return null;
    }

    String ruta = System.getProperty("user.dir") + "/uploads/";
    String nombreArchivo = UUID.randomUUID() + "_" + archivo.getOriginalFilename();

    try {
        File carpeta = new File(ruta);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        File destino = new File(ruta + nombreArchivo);
        archivo.transferTo(destino);

        return nombreArchivo;

    } catch (IOException e) {
        e.printStackTrace(); // 🔥 IMPORTANTE
        throw new RuntimeException("Error al guardar imagen");
    }
}


private PeliculaResponseDTO mapToDTO(Pelicula p) {

    PeliculaResponseDTO dto = new PeliculaResponseDTO();

    dto.setIdPelicula(p.getIdPelicula());
    dto.setNombre(p.getNombre());
    dto.setDuracion(p.getDuracion());
    dto.setClasificacion(p.getClasificacion().getNombre());
    dto.setDescripcion(p.getDescripcion());
    dto.setPoster(p.getPoster());
    dto.setCreadoPor(p.getCreadoPor().getNombre());

    // 🔥 AQUÍ YA NO FALLA
    List<String> generos = peliculaGeneroRepository
            .findGenerosByPeliculaId(p.getIdPelicula());

    dto.setGeneros(generos);


    

    return dto;



    

}






    
}
