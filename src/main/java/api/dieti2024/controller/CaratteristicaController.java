package api.dieti2024.controller;

import api.dieti2024.model.Caratteristica;
import api.dieti2024.service.Asta.CaratteristicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/public")
public class CaratteristicaController {

    @Autowired
    private CaratteristicaService caratteristicaService;

    @GetMapping("/getCaratteristicheDaCategoria")
    public List<Caratteristica> getCaratteristicheDaCategoria(@RequestParam("categoria") String categoria) {

        return caratteristicaService.getCaratteristicaDaCategoria(categoria);
    }
}
