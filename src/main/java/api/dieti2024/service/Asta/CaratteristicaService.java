package api.dieti2024.service.Asta;

import api.dieti2024.model.Caratteristica;
import api.dieti2024.repository.CaratteristicaRepository;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CaratteristicaService {

    @Autowired
    private CaratteristicaRepository caratteristicaRepository;

    public List<Caratteristica> getCaratteristicaDaCategoria(String categoria){

        List<Caratteristica> listaDaRestituire = caratteristicaRepository.getCaratteristicheDaCategoria(categoria);

        return listaDaRestituire;
    }
}
