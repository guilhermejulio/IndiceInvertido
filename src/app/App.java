import java.io.IOException;
import java.util.Scanner;


/**
 * Trabalho 04 - Indice Invertido - Lab. 03 - Guilherme J.
 * 
 * Estrutura: 
 * 
 *  Classe Index --> Contem um TreeMap com par chave/valor representando --> Palavra,Ocorrencias(Termo)
 *  Classe Termo --> armazena as ocorrencias de determinada palavra
 * 
 *  Criação do indice --> feita usando tokenizer e removendo todos os acentos das palavras; 
 *  Busca --> Com o indice montado na memoria a partir do arquivo --> Pesquisa por um ou mais termos
 */
public class App {

    public static Index indice; //Indice onde contem os termos e as ocorrencias.

    /**
     * Menu de opção com escolhas do contexto
     * @param leitor
     * @return
     */
    public static int menu(Scanner leitor){
        System.out.println();
        System.out.println("\tIndice INVERTIDO!");
        System.out.println("\n0. Fim do programa");
        System.out.println("1. Criar/Carregar indice invertido (DOS ARQUIVOS NA PASTA TXT)");
        System.out.println("2. Realizar uma busca (1 ou mais termos)");
        System.out.println("3. Imprimir quantidade de palavras existentes no dicionario");
        System.out.println("\nOpcao:");
        int opcao = Integer.parseInt(leitor.nextLine());
        return opcao;
    }

    public static void main(String[] args){
        int opcao;
        Scanner entrada = new Scanner(System.in);
        Scanner teclado = new Scanner(System.in);


        try{
            do{
                opcao = menu(entrada);
                switch(opcao){
                    case 1: 
                        System.out.println("\n\nIndice sendo criado/carregado na pasta, nome do arquivo --> IndiceInvertido.bin");
                        indice = new Index(); //cria o indice dos arquivos da pasta TXT
                        System.out.println("\n\nIndice criado/carregado com sucesso!\nPrescione <enter>...");
                        teclado.nextLine();
                        break;
                    case 2:
                        if(indice != null){
                            System.out.println("\n\nDigite a(s) palavra(s) que deseja pesquisar (DIGITE SEM ACENTO!): ");
                            String pesquisa = teclado.nextLine();
                            System.out.println("\n\nResultado:");
                            indice.pesquisarTermos(pesquisa);
                            teclado.nextLine();
                        }else{
                            System.out.println("\n\nO indice não foi criado/carregado!! selecione a opção <1> primeiro");
                            teclado.nextLine();
                        }
                        
                        break;
                    case 3:
                        if(indice!=null){
                            System.out.println("\nExiste um total de: "+indice.getQtdTermos()+" palavras diferentes no dicionario...");
                            teclado.nextLine();
                        }else{
                            System.out.println("\n\nO indice não foi criado/carregado!! selecione a opção <1> primeiro");
                            teclado.nextLine();
                        }
                        break;
                    case 0:
                        System.out.println("Adeus!!!");
                        break;
                    default:
                        System.out.println("\nOpção invalida, selecione novamente:");
                        break;
                }

            }while(opcao!=0);

        }catch (IOException ex) {
            System.out.println("Ocorreu um erro: "+ex.getMessage());
        }
        
        
        
    }
}
