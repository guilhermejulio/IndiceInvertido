import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * 0) DEFINIR A CLASSE DO TERMO E A ESTRUTURA DO "DICIONÁRIO" 
 *      Termo => Lista de quais arquivos aquele termo ocorre
 *      Index ==> Contem a palavra + termo         Index<Palavra,Termo> // <Palavra,Quantas Vezes Ocorre>
 *      
 */

public class Index {
    public TreeMap <String, Termo> dicionario;

    public List<String> stopwords; //lista de stopwords a partir do arquivo disponibilizado

    private static int qtdTermos=0;

    private String[] listaArquivos; 

    

    /***
     * Construtor do indice invertido;
     *   Inicialmente atribui o dicionario, e salva o nome dos arquivos em uma lista;
     *   
     *      Verificação --> Se o indice já existe, ou seja, já existe um arquivo binario de indice
     *        Se existe, não é necessario criar outro arquivo, apenas montar os dados na memoria [dicionario]
     *        Se não existe, é necessario criar o arquivo de indice invertido.
     * 
     * @throws IOException
     */
    public Index() throws IOException {
        dicionario = new TreeMap<String, Termo>();
        listaArquivos = salvarNomesArquivos();
        inicializarStopwords();

        if(new File("IndiceInvertido.bin").exists()){ //se o arquivo indice ja existe
            montarIndice();
        }else{
            criarIndice();
        }

    }


    /**
     * Metodo faz a leitura do arquivo binario de indice invertido existente e coloca os dados na memoria [dicionario]
     * 
     * Estrutura do arquivo ==> No inicio -> um inteiro contendo o numero de termos
     *      
     *   Em seguida ==> estrutura dos dados --> palavra/Quantidade de ocorrencias da palavra/Ocorrencias
     *      Exemplo ficticio: guilherme/5/1/2/3/4/5/ obs: as "/" são só para ilustrar, não existem no arquivo binario
     *        
     * @throws IOException
     */
    public void montarIndice() throws IOException {
        RandomAccessFile File = new RandomAccessFile("IndiceInvertido.bin", "rw");
        
        qtdTermos = File.readInt();

        // estrutura salva -->   palavra/qtdOcorrencias/ocorrencia/ocorrencia/ocorrencia

        for(int i=1; i<= qtdTermos;i++){
            String palavra = File.readUTF();
            ArrayList<Integer> ocorrencias = new ArrayList<Integer>();
            
            int qtdocorrencias = File.readInt();

            for(int cont =1; cont <= qtdocorrencias; cont++){
                ocorrencias.add(File.readInt());
            }

            dicionario.put(palavra, new Termo(ocorrencias));
        }

        File.close();
    }

    /**
     * Metodo que inicializa Stopwords, a partir do arquivo stopwords.txt
     */
    public void inicializarStopwords() {
        String linha;
        stopwords = new ArrayList<String>();

        try (BufferedReader br = new BufferedReader(new FileReader("stopwords.txt"))) {
            linha = br.readLine();
            while (linha != null) {

                stopwords.add(linha);
                linha = br.readLine();
            }

        } catch (final IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }

    }

    /**
     * 1) CRIAR/OBTER A LISTA DE ARQUIVOS/DOCUMENTOS
     * @return
     */
    public String[] salvarNomesArquivos() {
        return (new File("TXT").list());
    }

    

    /***
     * Metodo de criação do indice usando Tokenizer
     * 
     * @throws IOException
     */
    public void criarIndice() throws IOException {
        StringTokenizer parser;     //identificador de tokens (termos)
        String tokenAux;
        
        //ENQUANTO HOUVER ARQUIVOS NÃO LIDOS 
        for(int i=0; i< listaArquivos.length; i++){
            //ABRIR O PRÓXIMO ARQUIVO
            try(BufferedReader br = new BufferedReader(new FileReader("TXT\\" + listaArquivos[i]))){
                //ENQUANTO TIVER LINHA NO ARQUIVO
                
                //LER UMA LINHA
                String linha = br.readLine();

                while(linha != null){

                    parser = new StringTokenizer(linha, "\" . ,() * / \\ = - + : ; —  |");
                    
                    //SEPARAR OS TERMOS E PARA CADA TERMO
                    while(parser.hasMoreTokens()){  
                                        
                                //termo sem acento para buscar e salvar sem erros
                        tokenAux = removerAcento(parser.nextToken().toLowerCase());   
                        
                        if(tokenAux != " " && tokenAux != ""){
                            //VERIFICAR SE É UMA STOPWORD
                            if(!stopwords.contains(tokenAux)){ 
                                //VERIFICAR SE EXISTE NO DICIONÁRIO
                                if(dicionario.containsKey(tokenAux)){ 
                                    //INSERIR A OCORRÊNCIA
                                    if(!dicionario.get(tokenAux).existeOcorrencia(i+1)){ 
                                        dicionario.get(tokenAux).salvarOcorrencia(i+1);
                                    }
                                }else{ // CRIAR NO DICIONÁRIO
                                    dicionario.put(tokenAux, new Termo(i+1));
                                    qtdTermos++;
                                }
                            }
                        }

                        
                    }
                    
                    linha = br.readLine();
                }
            }catch (final IOException e) {
                System.out.println("Erro: " + e.getMessage());
            }


        }

        //SALVAR O ÍNDICE INVERTIDO EM ARQUIVO BINÁRIO  
        
        saveToFile(); //O treemap já salva ordenado, então não é necessario ordenar
        

         
                
    }
    
    /**
     * Metodo que pesquisa termos no dicioanrio a partir de uma entrada
     * @param line
     */
    public void pesquisarTermos(String line){ //pesquisa termos a partir de uma entrada passada
        ArrayList<String> listaPesquisa = new ArrayList<String>();
        
        StringTokenizer parser;     //identificador de tokens (termos)
        String tokenAux;
        line = line.toLowerCase();
        parser = new StringTokenizer(line, "\" . ,() * / \\ = - + : ; —  |");

        while(parser.hasMoreTokens()){
            tokenAux = parser.nextToken().toLowerCase();

            
            if(!stopwords.contains(tokenAux)){
                if(!listaPesquisa.contains(tokenAux)){
                    listaPesquisa.add(tokenAux);
                }
            }
        }

        for(String s: listaPesquisa){
            if(dicionario.containsKey(s)){
                System.out.println("\nA palavra --> \""+s+"\" existe no(s) arquivo(s) --> "+dicionario.get(s).retornaOcorrencias());
            }
            else{
                System.out.println("\nA palavra --> \""+s+"\" não existe em nenhum arquivo!!");
            }
            

        }

    }

    /**
     * Metodo responsavel por salvar os termos no arquivo binario
     * @return
     * @throws IOException
     */
    public boolean saveToFile() throws IOException {
        RandomAccessFile File = new RandomAccessFile(new File("IndiceInvertido.bin"), "rw");
        File.writeInt(qtdTermos);

        
        for(Map.Entry E : dicionario.entrySet()){
            File.writeUTF((String) E.getKey()); //salvei a palavra

            File.writeInt(dicionario.get(E.getKey()).ocorrencias.size()); //salvei quantas vezes ela ocorre

            for(Integer i : dicionario.get(E.getKey()).ocorrencias){
                File.writeInt(i); //salvei cada vez que ocorre
            }
            // estrutura salva -->   palavra/qtdocorrencias/ocorrencia/ocorrencia/ocorrencia
        }

        return true;

    }


    /**
     * Metodo para remover acento de uma palavra usando rgex
     * @param palavra
     * @return
     */
    public static String removerAcento(String palavra){
        return Normalizer.normalize(palavra, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }


	public Integer getQtdTermos() {
		return qtdTermos;
	}

    
    
}
