// Nome dos participantes: Raphael Brandão, Ana Clara, Matheus Souza, Hellen de Sá, Eyshila.

// Importações necessárias para usar a DLL (JNA), arquivos e entrada do usuário
import com.sun.jna.Library;
import com.sun.jna.Native;
import java.util.Scanner;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.io.FileInputStream;

public class Main {

    // =====================================================================
    // INTERFACE DA DLL VIA JNA
    // =====================================================================
    // Aqui fazemos o mapeamento das funções da DLL E1_Impressora01.dll
    // para que possam ser chamadas diretamente pelo Java.
    public interface ImpressoraDLL extends Library {

        // Carregamento da DLL usando caminho absoluto
        // IMPORTANTE: ajustar esse caminho para o caminho REAL no seu computador.
        ImpressoraDLL INSTANCE = (ImpressoraDLL) Native.load(
                "C:\\\\Users\\\\richard.spanhol\\\\Downloads\\\\Java-Aluno Graduacao\\\\E1_Impressora01.dll",
                ImpressoraDLL.class
        );

        // Métodos da DLL mapeados para uso em Java
        int AbreConexaoImpressora(int tipo, String modelo, String conexao, int param);
        int FechaConexaoImpressora();
        int ImpressaoTexto(String dados, int posicao, int estilo, int tamanho);
        int Corte(int avanco);
        int ImpressaoQRCode(String dados, int tamanho, int nivelCorrecao);
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

    // =====================================================================
    // VARIÁVEIS DE CONTROLE
    // =====================================================================
    private static boolean conexaoAberta = false; // Verifica se existe conexão ativa
    private static int tipo;                      // Tipo da conexão (USB, serial, etc.)
    private static String modelo;                 // Modelo da impressora
    private static String conexao;                // Porta de comunicação
    private static int parametro;                 // Parâmetro extra
    private static final Scanner scanner = new Scanner(System.in);

    // Captura a entrada do usuário com uma mensagem
    private static String capturarEntrada(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }

    // =====================================================================
    // CONFIGURAÇÃO DA CONEXÃO
    // =====================================================================
    public static void configurarConexao() {
        if (!conexaoAberta) {
            tipo = Integer.parseInt(capturarEntrada("Digite o tipo de conexão (ex: 1 para USB, 2 para serial, etc.): "));
            modelo = capturarEntrada("Digite o modelo da impressora (ex: 'i9'): ");
            conexao = capturarEntrada("Digite a porta de conexão (ex: 'COM3' para Windows): ");
            parametro = Integer.parseInt(capturarEntrada("Digite o parâmetro adicional (ex: 0 para padrão): "));
            System.out.println("Parâmetros de conexão configurados com sucesso.");
        } else {
            System.out.println("Parâmetros de conexão já configurados.");
        }
    }

    // =====================================================================
    // ABRIR CONEXÃO COM A IMPRESSORA
    // =====================================================================
    public static void abrirConexao() {
        if(!conexaoAberta){
            int retorno = ImpressoraDLL.INSTANCE.AbreConexaoImpressora(tipo, modelo, conexao, parametro);

            if (retorno == 0 ) {
                conexaoAberta = true;
                System.out.println("Conexão aberta com sucesso!");
            }
            else {
                System.out.println("Erro ao abrir conexão. Código: " + retorno);
            }
        }
        else {
            System.out.println("A conexão já está aberta!");
        }
    }

    // =====================================================================
    // FECHAR CONEXÃO COM A IMPRESSORA
    // =====================================================================
    public static void fecharConexao() {
        if(conexaoAberta){
            int retorno = ImpressoraDLL.INSTANCE.FechaConexaoImpressora();

            if (retorno == 0){
                conexaoAberta = false;
                System.out.println("Conexão fechada com sucesso!");
            }
            else {
                System.out.println("Erro ao fechar conexão. Código: " + retorno);
            }

        } else {
            System.out.println("Nenhuma conexão aberta.");
        }
    }

    // =====================================================================
    // IMPRESSÃO DE TEXTO
    // =====================================================================
    public static void imprimirTexto() {
        if (conexaoAberta) {

            System.out.println("Digite a posição (0 = esquerda, 1 = centralizado, 2 = direita):");
            int posicao = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Digite o estilo (0 = normal, 1 = negrito, 2 = expandido, etc.):");
            int estilo = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Digite o tamanho da fonte (entre 1 e 8):");
            int tamanho = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Digite o texto para impressão:");
            String texto = scanner.nextLine();

            int retorno = ImpressoraDLL.INSTANCE.ImpressaoTexto(texto, posicao, estilo, tamanho);

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

    // =====================================================================
    // IMPRESSÃO DE QR CODE
    // =====================================================================
    public static void imprimirQRCode() {
        if (conexaoAberta) {

            System.out.println("Digite o texto do QR Code:");
            String dados = scanner.nextLine();

            System.out.println("Digite o tamanho (1 a 8):");
            int tamanho = Integer.parseInt(scanner.nextLine());

            System.out.println("Digite o nível de correção (0=L, 1=M, 2=Q, 3=H):");
            int nivel = Integer.parseInt(scanner.nextLine());

            int retorno = ImpressoraDLL.INSTANCE.ImpressaoQRCode(dados, tamanho, nivel);

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

    // =====================================================================
    // IMPRESSÃO DE CÓDIGO DE BARRAS
    // =====================================================================
    public static void imprimirCodBarras() {

        if (conexaoAberta) {

            System.out.println("Digite o tipo do código de barras:");
            System.out.println("1 = UPC-A\n2 = UPC-E\n3 = EAN 13\n4 = EAN 8\n5 = CODE 39\n6 = ITF\n7 = CODEBAR\n8 = CODE 93\n9 = CODE 128");
            int tipo = Integer.parseInt(scanner.nextLine());

            System.out.println("Digite os dados:");
            String dados = scanner.nextLine();

            System.out.println("Altura do código:");
            int altura = Integer.parseInt(scanner.nextLine());

            System.out.println("Largura (1 a 3):");
            int largura = Integer.parseInt(scanner.nextLine());

            System.out.println("Exibir texto? (0 = não, 1 = sim)");
            int HRI = Integer.parseInt(scanner.nextLine());

            int retorno = ImpressoraDLL.INSTANCE.ImpressaoCodigoBarras(tipo, dados, altura, largura, HRI);

            if (retorno == 0) {
                System.out.println("Código de barras impresso!");
                ImpressoraDLL.INSTANCE.AvancaPapel(5);
            } else {
                System.out.println("Erro ao imprimir. Código: " + retorno);
            }

        } else {
            System.out.println("Erro: Conexão não está aberta.");
        }
    }

    // =====================================================================
    // ABRIR GAVETA (ELGIN)
    // =====================================================================
    public static void abrirGavetaElgin() {
        if (conexaoAberta) {
            int retorno = ImpressoraDLL.INSTANCE.AbreGavetaElgin();

            if (retorno == 0) {
                System.out.println("Gaveta aberta!");
            } else {
                System.out.println("Erro ao abrir gaveta. Código: " + retorno);
            }

        } else {
            System.out.println("Erro: Conexão não está aberta.");
        }
    }

    // =====================================================================
    // ABRIR GAVETA COM PARÂMETROS
    // =====================================================================
    public static void abrirGaveta() {

        if (conexaoAberta) {

            System.out.println("Pino (0 ou 1):");
            int pino = Integer.parseInt(scanner.nextLine());

            System.out.println("Tempo inicial (ms):");
            int ti = Integer.parseInt(scanner.nextLine());

            System.out.println("Tempo final (ms):");
            int tf = Integer.parseInt(scanner.nextLine());

            int retorno = ImpressoraDLL.INSTANCE.AbreGaveta(pino, ti, tf);

            if (retorno == 0) {
                System.out.println("Gaveta aberta!");
            } else {
                System.out.println("Erro. Código: " + retorno);
            }

        } else {
            System.out.println("Erro: Conexão não está aberta.");
        }
    }

    // =====================================================================
    // SINAL SONORO
    // =====================================================================
    public static void sinalSonoro() {

        if (conexaoAberta) {

            System.out.println("Quantidade de beeps:");
            int qtd = Integer.parseInt(scanner.nextLine());

            System.out.println("Tempo inicial (ms):");
            int tempoInicio = Integer.parseInt(scanner.nextLine());

            System.out.println("Tempo final (ms):");
            int tempoFim = Integer.parseInt(scanner.nextLine());

            int retorno = ImpressoraDLL.INSTANCE.SinalSonoro(qtd, tempoInicio, tempoFim);

            if (retorno == 0) {
                System.out.println("Sinal sonoro executado!");
            } else {
                System.out.println("Erro. Código: " + retorno);
            }

        } else {
            System.out.println("Erro: Conexão não está aberta.");
        }
    }

    // =====================================================================
    // MÉTODO PRINCIPAL — MENU DO SISTEMA
    // =====================================================================
    public static void main(String[] args) {
        while (true) {
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

            if (escolha.equals("0")) {
                fecharConexao();
                System.out.println("Programa encerrado.");
                break;
            }

            switch (escolha) {
                case "1": configurarConexao(); break;
                case "2": abrirConexao(); break;
                case "3": imprimirTexto(); break;
                case "4": imprimirQRCode(); break;
                case "5": imprimirCodBarras(); break;
                case "6": imprimirXMLSAT(); break;
                case "7": imprimirXMLCancelamento(); break;
                case "8": abrirGavetaElgin(); break;
                case "9": abrirGaveta(); break;
                case "10": sinalSonoro(); break;
                default: System.out.println("Opção inválida.");
            }
        }

        scanner.close();
    }

    // =====================================================================
    // LEITURA DE ARQUIVOS XML
    // =====================================================================
    private static String lerArquivoComoString(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        byte[] data = fis.readAllBytes();
        fis.close();
        return new String(data, StandardCharsets.UTF_8);
    }

    // =====================================================================
    // IMPRESSÃO DE XML SAT
    // =====================================================================
    private static void imprimirXMLSAT() {
        if (!conexaoAberta) {
            System.out.println("Erro: Conexão não está aberta.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Arquivos XML", "xml"));

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            try {
                String conteudoXML = lerArquivoComoString(selectedFile.getAbsolutePath());
                int retorno = ImpressoraDLL.INSTANCE.ImprimeXMLSAT(conteudoXML, 0);
                ImpressoraDLL.INSTANCE.Corte(5);

                System.out.println(retorno == 0 ?
                        "Impressão XML SAT concluída!" :
                        "Erro ao imprimir XML SAT. Código: " + retorno);

            } catch (Exception e) {
                System.out.println("Erro ao ler XML: " + e.getMessage());
            }
        }
    }

    // =====================================================================
    // IMPRESSÃO DE XML DE CANCELAMENTO
    // =====================================================================
    private static void imprimirXMLCancelamento() {
        if (!conexaoAberta) {
            System.out.println("Erro: Conexão não está aberta.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Arquivos XML", "xml"));

        String assQRCode =
                "Q5DLkpdRijIRGY6YSSNsTWK1TztHL1vD0V1Jc4spo/CEUqICEb9SFy82ym8EhBRZjbh3btsZhF+sjHqEMR159i4agru9x6KsepK/q0E2e5xlU5cv3m1woYfgHyOkWDNcSdMsS6bBh2Bpq6s89yJ9Q6qh/J8YHi306ce9Tqb/drKvN2XdE5noRSS32TAWuaQEVd7u+TrvXlOQsE3fHR1D5f1saUwQLPSdIv01NF6Ny7jZwjCwv1uNDgGZONJdlTJ6p0ccqnZvuE70aHOI09elpjEO6Cd+orI7XHHrFCwhFhAcbalc+ZfO5b/+vkyAHS6CYVFCDtYR9Hi5qgdk31v23w==";

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {

            File selectedFile = fileChooser.getSelectedFile();

            try {
                String xml = lerArquivoComoString(selectedFile.getAbsolutePath());
                int retorno = ImpressoraDLL.INSTANCE.ImprimeXMLCancelamentoSAT(xml, assQRCode, 0);
                ImpressoraDLL.INSTANCE.Corte(5);

                System.out.println(retorno == 0 ?
                        "Impressão XML Cancelamento concluída!" :
                        "Erro ao imprimir XML Cancelamento. Código: " + retorno);

            } catch (Exception e) {
                System.out.println("Erro ao ler XML: " + e.getMessage());
            }
        }
    }
}
