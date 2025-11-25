
// Nome dos participantes: Raphael Brandão, Ana Clara, Matheus Souza, Hellen de Sá, Eyshila.

import com.sun.jna.Library;   // Importa a interface base para trabalhar com DLLs via JNA
import com.sun.jna.Native;    // Permite carregar uma DLL e vincular suas funções
import java.util.Scanner;     // Utilizado para capturar entrada do usuário
import javax.swing.JFileChooser; // Usado para abrir arquivos XML via explorador de arquivos
import java.io.File;          // Representação de arquivos no sistema
import java.io.IOException;   // Tratamento de erros de entrada/saída
import java.nio.charset.StandardCharsets; // Define charset para leitura de arquivos
import java.io.FileInputStream; // Para ler arquivos XML byte a byte

public class Main {

    // Interface que representa a DLL da impressora — usando JNA
    public interface ImpressoraDLL extends Library {

        // Carregamento da DLL — caminho deve existir no computador do usuário
        ImpressoraDLL INSTANCE = (ImpressoraDLL) Native.load(
                "C:\\Users\\matheus_ricardo\\Desktop\\Java-Aluno EM\\E1_Impressora01.dll",
                ImpressoraDLL.class
        );

        // Lista das funções da DLL que poderão ser chamadas pelo Java
        int AbreConexaoImpressora(int tipo, String modelo, String conexao, int param);
        int FechaConexaoImpressora();
        int ImpressaoTexto(String dados, int posicao, int estilo, int tamanho);
        int Corte(int avanco);
        int ImpressaoQRCode(int dados, int tamanho, int nivelCorrecao);
        int ImpressaoCodigoBarras(int tipo, String dados, int altura, int largura, int HRI);
        int AvancaPapel(int linhas);
        int StatusImpressora(int param);
        int AbreGavetaElgin();
        int AbreGaveta(int pino, int ti, int tf);
        int SinalSonoro(int qtd, int tempoInicio, int tempoFim);
        int ModoPagina();
        int LimpaBufferModoPagina();
        int ImprimeModoPagina();
        int ModoPadrao();
        int PosicaoImpressaoHorizontal(int posicao);
        int PosicaoImpressaoVertical(int posicao);
        int ImprimeXMLSAT(String dados, int param);
        int ImprimeXMLCancelamentoSAT(String dados, String assQRCode, int param);
    }

    // Variáveis de controle da conexão da impressora
    private static boolean conexaoAberta = false;
    private static int tipo;
    private static String modelo;
    private static String conexao;
    private static int parametro;
    private static final Scanner scanner = new Scanner(System.in); // Scanner único para capturar entradas

    // Função auxiliar para capturar entrada com mensagem personalizada
    private static String capturarEntrada(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }

    // Função para configurar os parâmetros de conexão antes de abrir a porta
    public static void configurarConexao() {
        if (!conexaoAberta) {
            tipo = Integer.parseInt(capturarEntrada("Digite o tipo de conexão (ex: 1 para USB, 2 para serial, etc.): "));
            modelo = capturarEntrada("Digite o modelo da impressora (ex: 'i9'): ");
            conexao = capturarEntrada("Digite a porta de conexão (ex: 'COM3' no Windows): ");
            parametro = Integer.parseInt(capturarEntrada("Digite o parâmetro adicional (ex: 0 para padrão): "));
            System.out.println("Parâmetros de conexão configurados com sucesso.");
        } else {
            System.out.println("Parâmetros de conexão já configurados.");
        }
    }

    // Função que abre a conexão com a impressora (chamando a DLL)
    public static void abrirConexao() {
        if (!conexaoAberta) {
            int retorno = ImpressoraDLL.INSTANCE.AbreConexaoImpressora(tipo, modelo, conexao, parametro);

            if (retorno == 0) {
                conexaoAberta = true;
                System.out.println("Conexão aberta com sucesso!");
            } else {
                System.out.println("Erro ao abrir conexão. Código: " + retorno);
            }
        } else {
            System.out.println("A conexão já está aberta!");
        }
    }

    // Fecha conexão com a impressora
    public static void fecharConexao() {
        if (conexaoAberta) {
            int retorno = ImpressoraDLL.INSTANCE.FechaConexaoImpressora();

            if (retorno == 0) {
                System.out.println("Conexão fechada com sucesso!");
                conexaoAberta = false;
            } else {
                System.out.println("Erro ao fechar conexão. Código: " + retorno);
            }
        } else {
            System.out.println("Nenhuma conexão está aberta.");
        }
    }

    // Impressão de texto comum
    public static void imprimirTexto() {
        if (conexaoAberta) {

            System.out.println("Digite o texto que deseja imprimir:");
            String texto = scanner.nextLine();

            int retorno = ImpressoraDLL.INSTANCE.ImpressaoTexto(String.valueOf(1), 2,3, 4);

            if (retorno == 0) {
                System.out.println("Texto impresso com sucesso!");
                ImpressoraDLL.INSTANCE.AvancaPapel(5);
            } else {
                System.out.println("Erro ao imprimir texto. Código: " + retorno);
            }

        } else {
            System.out.println("Erro: Conexão não está aberta.");
        }
    }

    // Impressão de QR Code
    public static void imprimirQRCode() {
        if (conexaoAberta) {
            System.out.println("Digite o texto para o QR Code:");
            String dados = scanner.nextLine();
            int retorno = ImpressoraDLL.INSTANCE.ImpressaoQRCode(1, 2, 3);

            if (retorno == 0) {
                System.out.println("QR Code impresso!");
                ImpressoraDLL.INSTANCE.AvancaPapel(5);
            } else {
                System.out.println("Erro ao imprimir QR Code. Código: " + retorno);
            }

        } else {
            System.out.println("Erro: Conexão não está aberta.");
        }
    }

    // Impressão de código de barras
    public static void imprimirCodBarras() {
        if (conexaoAberta) {

            System.out.println("Escolha o tipo de código de barras:");
            System.out.println("1=UPC-A, 2=UPC-E, 3=EAN-13, 4=EAN-8, 5=CODE 39, 6=ITF, 7=CODEBAR, 8=CODE 93, 9=CODE 128");
            int tipo = Integer.parseInt(scanner.nextLine());


            System.out.println("Exibir texto abaixo? (0=não, 1=sim):");
            int hri = Integer.parseInt(scanner.nextLine());

            int retorno = ImpressoraDLL.INSTANCE.ImpressaoCodigoBarras(1, String.valueOf(2), 3, 4, 5);

            if (retorno == 0) {
                System.out.println("Código de barras impresso!");
                ImpressoraDLL.INSTANCE.AvancaPapel(5);
            } else {
                System.out.println("Erro ao imprimir código. Código: " + retorno);
            }

        } else {
            System.out.println("Erro: Conexão não está aberta.");
        }
    }

    // Abre gaveta padrão Elgin
    public static void abrirGavetaElgin() {
        if (conexaoAberta) {
            int retorno = ImpressoraDLL.INSTANCE.AbreGavetaElgin();

            System.out.println(retorno == 0 ? "Gaveta aberta!" : "Erro ao abrir gaveta. Código: " + retorno);
        } else {
            System.out.println("Erro: Conexão não está aberta.");
        }
    }

    // Abre gaveta com parâmetros manuais
    public static void abrirGaveta() {
        if (conexaoAberta) {

            System.out.println("Digite o pino (0 ou 1):");
            int pino = Integer.parseInt(scanner.nextLine());

            System.out.println("Tempo de início do pulso:");
            int ti = Integer.parseInt(scanner.nextLine());

            System.out.println("Tempo final do pulso:");
            int tf = Integer.parseInt(scanner.nextLine());

            int retorno = ImpressoraDLL.INSTANCE.AbreGaveta(pino, ti, tf);

            System.out.println(retorno == 0 ? "Gaveta aberta!" : "Erro ao abrir gaveta. Código: " + retorno);

        } else {
            System.out.println("Erro: Conexão não está aberta.");
        }
    }

    // Emite sinal sonoro (beep)
    public static void sinalSonoro() {
        if (conexaoAberta) {

            System.out.println("Quantidade de beeps:");
            int qtd = Integer.parseInt(scanner.nextLine());

            System.out.println("Tempo inicial (ms):");
            int ti = Integer.parseInt(scanner.nextLine());

            System.out.println("Tempo final (ms):");
            int tf = Integer.parseInt(scanner.nextLine());

            int retorno = ImpressoraDLL.INSTANCE.SinalSonoro(qtd, ti, tf);

            System.out.println(retorno == 0 ? "Beep executado!" : "Erro ao executar beep. Código: " + retorno);

        } else {
            System.out.println("Erro: Conexão não está aberta.");
        }
    }

    // ========================= MENU PRINCIPAL ===============================

    public static void main(String[] args) {
        while (true) {

            // Menu apresentado ao usuário
            System.out.println("\n*************************************************");
            System.out.println("**************** MENU IMPRESSORA ****************");
            System.out.println("*************************************************\n");

            System.out.println("1  - Configurar Conexao");
            System.out.println("2  - Abrir Conexao");
            System.out.println("3  - Impressao Texto");
            System.out.println("4  - Impressao QRCode");
            System.out.println("5  - Impressao Cod Barras");
            System.out.println("6  - Impressao XML SAT");
            System.out.println("7  - Impressao XML Canc SAT");
            System.out.println("8  - Abrir Gaveta Elgin");
            System.out.println("9  - Abrir Gaveta");
            System.out.println("10 - Sinal Sonoro");
            System.out.println("0  - Fechar Conexao e Sair");

            String escolha = capturarEntrada("\nDigite a opção desejada: ");

            // Opção 0 sempre encerra o programa
            if (escolha.equals("0")) {
                fecharConexao();
                System.out.println("Programa encerrado.");
                break;
            }

            // Escolha da operação
            switch (escolha) {
                case "1": configurarConexao(); break;
                case "2": abrirConexao(); break;
                case "3": imprimirTexto(); break;
                case "4": imprimirQRCode(); break;
                case "5": imprimirCodBarras(); break;

                case "6": // Impressão XML SAT
                    if (conexaoAberta) {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setCurrentDirectory(new File(".")); // abre na pasta atual
                        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Arquivos XML", "xml"));

                        int result = fileChooser.showOpenDialog(null);

                        if (result == JFileChooser.APPROVE_OPTION) {
                            File selectedFile = fileChooser.getSelectedFile();
                            String path = selectedFile.getAbsolutePath();

                            try {
                                String conteudoXML = lerArquivoComoString(path);
                                int ret = ImpressoraDLL.INSTANCE.ImprimeXMLSAT(conteudoXML, 0);
                                ImpressoraDLL.INSTANCE.Corte(5);
                                System.out.println(ret == 0 ? "XML impresso!" : "Erro ao imprimir XML SAT. Código: " + ret);
                            } catch (IOException e) {
                                System.out.println("Erro ao ler arquivo XML: " + e.getMessage());
                            }
                        }

                    } else {
                        System.out.println("Erro: Conexão não está aberta.");
                    }
                    break;

                case "7": // Impressão XML Cancelamento SAT
                    if (conexaoAberta) {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setCurrentDirectory(new File("."));
                        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Arquivos XML", "xml"));

                        String assQRCode = "Q5DLkpdRijIRG... (código reduzido para exemplo)";

                        int result = fileChooser.showOpenDialog(null);

                        if (result == JFileChooser.APPROVE_OPTION) {

                            File selectedFile = fileChooser.getSelectedFile();
                            String path = selectedFile.getAbsolutePath();

                            try {
                                String conteudoXML = lerArquivoComoString(path);
                                int ret = ImpressoraDLL.INSTANCE.ImprimeXMLCancelamentoSAT(conteudoXML, assQRCode, 0);
                                ImpressoraDLL.INSTANCE.Corte(5);
                                System.out.println(ret == 0 ? "Cancelamento impresso!" : "Erro ao imprimir cancelamento. Código: " + ret);

                            } catch (IOException e) {
                                System.out.println("Erro ao ler arquivo XML: " + e.getMessage());
                            }

                        }
                    } else {
                        System.out.println("Erro: Conexão não está aberta.");
                    }
                    break;

                case "8": abrirGavetaElgin(); break;
                case "9": abrirGaveta(); break;
                case "10": sinalSonoro(); break;

                default:
                    System.out.println("OPÇÃO INVÁLIDA!");
            }
        }

        scanner.close(); // Fecha scanner ao encerrar o programa
    }

    // Função auxiliar para ler arquivos XML e convertê-los em string
    private static String lerArquivoComoString(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        byte[] data = fis.readAllBytes();
        fis.close();
        return new String(data, StandardCharsets.UTF_8);
    }

}
