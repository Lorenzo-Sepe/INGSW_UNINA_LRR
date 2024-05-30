package api.dieti2024.service;

import api.dieti2024.dto.CategoriaDTO;
import api.dieti2024.model.Categoria;
import api.dieti2024.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;


    /* ---------------------------Versione vecchia da cancellare----------------------------------------------------------
    public List<CategoriaDTO> costruisciGerarchiaCategorieDeprecato(){

        List<CategoriaDTO> listaCategorieDaRestituire = new ArrayList<>();

        List<String> categorieRadici =  categoriaRepository.getCategorieRadice();

        List<String> figli;


            Per ogni categoria radice estraiamo la lista dei suoi figli


        for(String categoriaRadice : categorieRadici){

            CategoriaDTO categoriaRadiceDTO = new CategoriaDTO(categoriaRadice);

            if( haDeifigli(categoriaRadiceDTO) ){

                figli = categoriaRepository.getCategorieFiglie(categoriaRadice);


                for(String figlio : figli){

                    CategoriaDTO figlioDTO = new CategoriaDTO(figlio);

                    costruisciGerarchiaRicorsivamente(figlioDTO);

                    categoriaRadiceDTO.getChildren().add(figlioDTO);
                }
            }

            listaCategorieDaRestituire.add(categoriaRadiceDTO);
        }

        return listaCategorieDaRestituire;
    }

    private void costruisciGerarchiaRicorsivamente(CategoriaDTO categoriaParametro){

        if(haDeifigli(categoriaParametro)){

            List<String> figli = categoriaRepository.getCategorieFiglie(categoriaParametro.getLabel());

            for (String figlio : figli){

                CategoriaDTO figlioDTO = new CategoriaDTO(figlio);

                costruisciGerarchiaRicorsivamente(figlioDTO);

                categoriaParametro.getChildren().add(figlioDTO);
            }
        }
    }

    private boolean haDeifigli(CategoriaDTO categoriaParametro) {
        return categoriaRepository.getNumeroFigli(categoriaParametro.getLabel()) > 0;
    }

    */

    /////////////////////////////////////////////////v2////////////////////////////////////////////////////////////////////////////
    public List<CategoriaDTO> costruisciGerarchiaCategorie(){

        List<CategoriaDTO> gerarchiaDaRestituire = new ArrayList<>();

        List<Categoria> tutteLeCategorie = categoriaRepository.findAll();

        List<String> categorieRadice = getCategorieRadice(tutteLeCategorie);

        List<String> figli;

        /*
            Per ogni categoria radice estraiamo la lista dei suoi figli

         */

        for(String categoriaRadice : categorieRadice){

            CategoriaDTO categoriaRadiceDTO = new CategoriaDTO(categoriaRadice);

            if(haDeiFigliV2(categoriaRadice,tutteLeCategorie)){

                figli = getFigli(categoriaRadice, tutteLeCategorie);

                /*
                    Per ogni figlio della categoria Radice viene fatto la stessa cosa ricorsivamente
                 */

                for(String figlio : figli){

                    CategoriaDTO figlioDTO = new CategoriaDTO(figlio);

                    costruisciGerarchiaRicorsivamenteV2(figlioDTO,tutteLeCategorie);

                    categoriaRadiceDTO.getChildren().add(figlioDTO);
                }
            }

            gerarchiaDaRestituire.add(categoriaRadiceDTO);
        }

        return gerarchiaDaRestituire;
    }

    private List<String> getCategorieRadice(List<Categoria> tutteLeCategorie){

        ArrayList<String> listaCategorieRadice = new ArrayList<>();

        for(Categoria categoria : tutteLeCategorie){

            if(categoria.getPadre() == null){

                listaCategorieRadice.add(categoria.getNome());
            }
        }

        return listaCategorieRadice;
    }

    private boolean haDeiFigliV2(String categoriaDaVerificare, List<Categoria>tutteLeCategorie){

        for(Categoria categoria : tutteLeCategorie){

            if(categoria.getPadre() != null){

                if(categoria.getPadre().equals(categoriaDaVerificare)){

                    return true;
                }
            }
        }

        return false;
    }

    private List<String> getFigli(String categoriaPadre, List<Categoria> tutteLeCategorie){

        ArrayList<String> figli = new ArrayList<>();

        for(Categoria categoria : tutteLeCategorie){

            if(categoria.getPadre() != null){

                if(categoria.getPadre().equals(categoriaPadre)){

                    figli.add(categoria.getNome());
                }
            }
        }

        return figli;
    }

    private void costruisciGerarchiaRicorsivamenteV2(CategoriaDTO categoriaDtoParametro, List<Categoria> tutteLeCategorie){

        if(haDeiFigliV2(categoriaDtoParametro.getLabel(),tutteLeCategorie)){

            List<String> figli = getFigli(categoriaDtoParametro.getLabel(),tutteLeCategorie);

            for (String figlio : figli){

                CategoriaDTO figlioDTO = new CategoriaDTO(figlio);

                costruisciGerarchiaRicorsivamenteV2(figlioDTO,tutteLeCategorie);

                categoriaDtoParametro.getChildren().add(figlioDTO);
            }
        }
    }
}
