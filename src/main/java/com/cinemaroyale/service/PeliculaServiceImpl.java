package com.cinemaroyale.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        pelicula.setTrailer(dto.getTrailer());
        pelicula.setAnio(dto.getAnio());
        pelicula.setCreadoPor(usuario);

        peliculaRepository.save(pelicula);

        return mapToDTO(pelicula);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PeliculaResponseDTO> listar() {
        List<Pelicula> peliculas = peliculaRepository.findAll();
        if (peliculas.isEmpty()) return new ArrayList<>();

        // Una sola query para obtener todos los generos de todas las peliculas
        List<Integer> ids = peliculas.stream()
                .map(Pelicula::getIdPelicula)
                .collect(Collectors.toList());

        List<Object[]> generoData = peliculaGeneroRepository.findGeneroDataByPeliculaIds(ids);

        Map<Integer, List<String>> generoNombres = new HashMap<>();
        Map<Integer, List<Integer>> generoIdsMap = new HashMap<>();

        for (Object[] row : generoData) {
            Integer idPelicula = (Integer) row[0];
            String nombre = (String) row[1];
            Integer idGenero = (Integer) row[2];
            generoNombres.computeIfAbsent(idPelicula, k -> new ArrayList<>()).add(nombre);
            generoIdsMap.computeIfAbsent(idPelicula, k -> new ArrayList<>()).add(idGenero);
        }

        return peliculas.stream()
                .map(p -> mapToDTOWithGeneros(p,
                        generoNombres.getOrDefault(p.getIdPelicula(), new ArrayList<>()),
                        generoIdsMap.getOrDefault(p.getIdPelicula(), new ArrayList<>())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
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
        pelicula.setTrailer(dto.getTrailer());
        pelicula.setAnio(dto.getAnio());
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

        peliculaGeneroRepository.deleteByPeliculaId(id);
        peliculaRepository.deleteById(id);
    }

    private PeliculaResponseDTO mapToDTOWithGeneros(
            Pelicula p, List<String> generos, List<Integer> generosIds) {
        PeliculaResponseDTO dto = buildBaseDTO(p);
        dto.setGeneros(generos);
        dto.setGenerosIds(generosIds);
        return dto;
    }

    private PeliculaResponseDTO mapToDTO(Pelicula p) {
        PeliculaResponseDTO dto = buildBaseDTO(p);
        dto.setGeneros(peliculaGeneroRepository.findGenerosByPeliculaId(p.getIdPelicula()));
        dto.setGenerosIds(peliculaGeneroRepository.findGeneroIdsByPeliculaId(p.getIdPelicula()));
        return dto;
    }

    private PeliculaResponseDTO buildBaseDTO(Pelicula p) {
        PeliculaResponseDTO dto = new PeliculaResponseDTO();
        dto.setIdPelicula(p.getIdPelicula());
        dto.setNombre(p.getNombre());
        dto.setDuracion(p.getDuracion());
        dto.setClasificacion(p.getClasificacion().getNombre());
        dto.setDescripcion(p.getDescripcion());
        dto.setPoster(p.getPoster());
        dto.setTrailer(p.getTrailer());
        dto.setAnio(p.getAnio());
        dto.setCreadoPor(p.getCreadoPor().getNombre());
        if (p.getClasificacion() != null) {
            dto.setIdClasificacion(p.getClasificacion().getId_clasificacion());
        }
        return dto;
    }







    
}
