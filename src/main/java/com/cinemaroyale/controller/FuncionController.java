package com.cinemaroyale.controller;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


import com.cinemaroyale.dto.FuncionRequestDTO;
import com.cinemaroyale.dto.FuncionResponseDTO;
import com.cinemaroyale.service.FuncionService;



@RestController
@RequestMapping("/api/funciones")
@CrossOrigin(origins = "*")
public class FuncionController {

    private final FuncionService service;

    public FuncionController(FuncionService service) {
        this.service = service;
    }

    @PostMapping
    public FuncionResponseDTO crear(@RequestBody FuncionRequestDTO dto) {
        return service.crear(dto);
    }

    @GetMapping
    public List<FuncionResponseDTO> listar() {
        return service.listar();
    }

    @PutMapping("/{id}")
    public FuncionResponseDTO actualizar(
            @PathVariable Integer id,
            @RequestBody FuncionRequestDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        service.eliminar(id);
    }
    


}
