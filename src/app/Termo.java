import java.util.ArrayList;

public class Termo {
    ArrayList<Integer> ocorrencias;

    /**
     * Metodo construtor usado quando acontece a criação do indice
     * @param arquivo
     */
    public Termo(Integer arquivo){

        ocorrencias = new ArrayList<Integer>();

        ocorrencias.add(arquivo);
    }

    /**
     * Metodo construtor usado quando acontece a montagem do indice, ou seja já existe arquivo indice
     * @param ocorrencias
     */
    public Termo(ArrayList<Integer> ocorrencias){
        this.ocorrencias = ocorrencias;
    }

    /**
     * verificar se ja existe ocorrencia para a palavra no arquivo atual
     * @param arquivo
     * @return
     */
    public boolean existeOcorrencia(Integer arquivo){
        return this.ocorrencias.contains(arquivo);
    }


    /**
     * salvar ocorrencia, o arquivo representa em qual arquivo a palavra está contida
     */
    public void salvarOcorrencia(Integer arquivo){
        this.ocorrencias.add(arquivo);
    }

    /***
     * Metodo que devolve uma String com as ocorrencias do termo.
     * @return
     */
    public String retornaOcorrencias(){
        String retorno="";
        for(Integer i: this.ocorrencias){
            retorno+=i+"/";
        }

        return retorno;
    }


    
}
