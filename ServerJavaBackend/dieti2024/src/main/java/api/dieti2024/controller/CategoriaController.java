package api.dieti2024.controller;

import api.dieti2024.dto.CategoriaDTO;
import api.dieti2024.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class CategoriaController {

    @Autowired
    CategoriaService categoriaServizio;
    @GetMapping("/getGerarchiaCategorie")
    public List<CategoriaDTO> getGerarchiaCategorie(){

        return categoriaServizio.costruisciGerarchiaCategorie();
    }
}
