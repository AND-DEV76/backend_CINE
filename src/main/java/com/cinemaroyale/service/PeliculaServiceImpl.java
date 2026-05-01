package com.cinemaroyale.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
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

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public PeliculaResponseDTO crear(PeliculaRequestDTO dto) {

        Clasificacion clasificacion = clasificacionRepository.findById(dto.getIdClasificacion())
                .orElseThrow(() -> new RuntimeException("Clasificación no encontrada"));

        Usuario usuario = usuarioRepository.findById(dto.getCreadoPor())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String posterUrl = cloudinaryService.uploadImage(dto.getPoster());

        Pelicula pelicula = new Pelicula();
        pelicula.setNombre(dto.getNombre());
        pelicula.setDuracion(dto.getDuracion());
        pelicula.setClasificacion(clasificacion);
        pelicula.setDescripcion(dto.getDescripcion());
        pelicula.setPoster(posterUrl); 
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
            if (pelicula.getPoster() != null) {
                cloudinaryService.deleteImage(pelicula.getPoster());
            }
            String posterUrl = cloudinaryService.uploadImage(dto.getPoster());
            pelicula.setPoster(posterUrl);
        }

        peliculaRepository.save(pelicula);

        return mapToDTO(pelicula);
    }

    @Override
    public void eliminar(Integer id) {
        Pelicula pelicula = peliculaRepository.findById(id).orElse(null);
        if (pelicula != null && pelicula.getPoster() != null) {
            cloudinaryService.deleteImage(pelicula.getPoster());
        }
        
        // 🔥 1. BORRAR RELACIONES (tabla intermedia)
        peliculaGeneroRepository.deleteByPeliculaId(id);
        
        peliculaRepository.deleteById(id);
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
