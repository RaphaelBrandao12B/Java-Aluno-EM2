 Sistema de Impressão via DLL (Projeto IntelliJ + JNA)

## 👥 Participantes do Projeto

* Ana Clara
* Eyshila
* Hellen de Sá
* Matheus Souza
* Raphael Brandão

---

# 📌 1. Visão Geral do Sistema

Este projeto foi desenvolvido em **Java**, utilizando o **IntelliJ IDEA**, com o objetivo de controlar uma **impressora térmica Elgin** por meio de funções nativas disponibilizadas em uma **DLL**, acessada através da biblioteca **JNA (Java Native Access)**.

O sistema oferece um **menu interativo no console**, permitindo realizar várias operações, como:

* Abrir e fechar conexão
* Imprimir texto
* Imprimir QR Code
* Imprimir código de barras
* Imprimir XML SAT e XML de cancelamento
* Emitir beep
* Abrir gaveta automática

Ele é totalmente funcional e executa comandos reais utilizando hardware conectado ao computador.

---

# 🛠️ 2. Tecnologias Utilizadas

* **Java 17 (ou superior)**
* **IntelliJ IDEA**
* **JNA – Java Native Access**
* **DLL da impressora Elgin**
* **Swing (JFileChooser)** para seleção de arquivos
* **Scanner** para entrada de dados
* **FileInputStream** para leitura de XML

---

# 🔗 3. Integração com a DLL (JNA)

A interface abaixo representa a ligação entre o Java e a DLL real:

```java
public interface ImpressoraDLL extends Library {
    ImpressoraDLL INSTANCE = (ImpressoraDLL) Native.load(
        "C:\\Users\\User\\Downloads\\Java-Aluno EM\\Java-Aluno EM\\Java-Aluno EM\\E1_Impressora01.dll",
        ImpressoraDLL.class
    );

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
```

Essa interface contém todas as funções da impressora disponíveis para uso.

---

# 🧩 4. Funcionamento Interno do Sistema

O projeto é estruturado em torno de um menu principal que controla todas as funções.

## ✔ Entrada do usuário

A entrada é capturada com:

```java
Scanner scanner = new Scanner(System.in);
```

E simplificada por uma função auxiliar:

```java
private static String capturarEntrada(String mensagem) { ... }
```

---

## ✔ Configuração da Conexão

O usuário informa:

* Tipo de conexão (USB, Serial, etc.)
* Modelo da impressora
* Porta de comunicação (ex: COM3)
* Parâmetro adicional

Isso prepara o ambiente antes de abrir a conexão.

---

## ✔ Abrir Conexão

Chamada realizada via DLL:

```java
ImpressoraDLL.INSTANCE.AbreConexaoImpressora(tipo, modelo, conexao, parametro);
```

Se o retorno for **0**, a conexão foi aberta com sucesso.

---

## ✔ Impressão de Texto

```java
ImpressoraDLL.INSTANCE.ImpressaoTexto(texto, 2, 3, 4);
```

Após a impressão, o papel avança automaticamente.

---

## ✔ Impressão de QR Code

```java
ImpressoraDLL.INSTANCE.ImpressaoQRCode(dados, 1, 2);
```

* `1` = tamanho
* `2` = nível de correção de erro

---

## ✔ Impressão de Código de Barras

```java
ImpressoraDLL.INSTANCE.ImpressaoCodigoBarras(1, dados, 2, 3, 4);
```

---

## ✔ Impressão de XML SAT e Cancelamento

O sistema usa `JFileChooser` para abrir o XML:

```java
JFileChooser fileChooser = new JFileChooser();
```

O XML é lido:

```java
String conteudoXML = lerArquivoComoString(path);
```

E enviado para a impressora:

```java
ImpressoraDLL.INSTANCE.ImprimeXMLSAT(conteudoXML, 0);
```

---

## ✔ Abertura da Gaveta

Opção padrão:

```java
AbreGavetaElgin();
```

Ou manual:

```java
AbreGaveta(pino, ti, tf);
```

---

## ✔ Beep (Sinal Sonoro)

```java
SinalSonoro(qtd, ti, tf);
```

---

# 📁 5. Estrutura do Projeto no IntelliJ

```
📦 ProjetoImpressora
 ├── src
 │    └── Main.java
 ├── DLL
 │    └── E1_Impressora01.dll
 ├── README.md
```

O IntelliJ foi utilizado para:

* executar o projeto
* gerenciar dependências
* compilar automaticamente
* permitir testes rápidos das funções da DLL

---

# ▶️ 6. Como Executar no IntelliJ

1. Abra o IntelliJ
2. Importe a pasta do projeto
3. Verifique o caminho da DLL no código
4. Execute o arquivo **Main.java**
5. O menu abrirá no console

---

# 🚀 7. Como Enviar para o GitHub

1. No IntelliJ → **Git → Enable Version Control**
2. Clique em **Add** para rastrear arquivos
3. Vá em **Git → Commit**
4. Escreva uma mensagem
5. Clique em **Commit and Push**
6. Pronto — o projeto estará no GitHub

---

# ✔️ 8. Conclusão

Este projeto demonstra uma integração completa entre **Java + JNA + DLL da impressora Elgin**, trazendo um sistema funcional, robusto e totalmente aplicável em ambientes reais.

Se quiser, posso também gerar:

* README com imagens
* README estilizado para GitHub
* README em inglês
* Diagrama explicando o fluxo do sistema
