import java.net.*;
import java.io.*;

public class Servidor {
    private static int porta = 4444;
    private static final int MAX_TENTATIVAS = 6;

    public static void main(String args[]) {
        ServerSocket serverSocket = null;
        boolean run = true;
        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            System.out.println("Nao foi possivel escutar na porta " + porta + "." + e);
            System.out.println("Erro: " + e);
            System.exit(1);
        }

        while (run) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("Falha ao aceitar conexao! Erro: " + e);
                System.exit(1);
            }

            System.out.println("Conexão estabelecida com " + clientSocket.getRemoteSocketAddress().toString());

            try {
                BufferedReader inStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter outStream = new PrintWriter(clientSocket.getOutputStream(), true);
                String inputLine;
                ManipuladorDeArquivo arquivo = new ManipuladorDeArquivo();

                while ((inputLine = inStream.readLine()) != null) {
                    inputLine = inputLine.toLowerCase();

                    switch (inputLine) {
                        case "iniciarjogo":
                            iniciarJogo(arquivo, inStream, outStream);
                            break;
                        case "end":
                            outStream.println("goodbye");
                            break;
                        default:
                            outStream.println("Mensagem desconhecida! Digite 'iniciarjogo' ou 'end'");
                    }
                }
                outStream.close();
                inStream.close();
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Erro ao tratar as requisições! Erro:" + e);
                System.exit(1);
            }
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Erro ao encerrar o servidor" + e);
            System.exit(1);
        }
    }

    public static void iniciarJogo(ManipuladorDeArquivo arquivo, BufferedReader inStream, PrintWriter outStream) throws IOException {
        String palavra = arquivo.lerArquivo();
        String palavraDescoberta = "_".repeat(palavra.length());
        int tentativasRestantes = MAX_TENTATIVAS;

        outStream.println("Iniciando jogo... A palavra tem " + palavra.length() + " letras: " + palavraDescoberta);

        while (true) {
            String tentativa = inStream.readLine();
            if (tentativa.isBlank() || tentativa.length() != 1) {
                outStream.println("Tentativa inválida");
                continue;
            }

            tentativa = tentativa.trim().toLowerCase();

            if (tentativa.equals("end")) {
                outStream.println("Encerrando jogo...");
                break;
            }

            char letra = tentativa.charAt(0);

            if(palavraDescoberta.contains(String.valueOf(letra))) {
                outStream.println("Letra '" + letra + "' já foi utilizada");
                continue;
            }

            boolean contem = false;
            StringBuilder novaPalavra = new StringBuilder(palavraDescoberta);

            for (int i = 0; i < palavra.length(); i++) {
                if (palavra.charAt(i) == letra) {
                    novaPalavra.setCharAt(i, letra);
                    contem = true;
                }
            }

            palavraDescoberta = novaPalavra.toString();

            if (contem && !palavraDescoberta.equals(palavra))
                outStream.println("Acertou! Palavra: " + palavraDescoberta);
            else if (!contem) {
                tentativasRestantes--;

                if(tentativasRestantes > 0) {
                    outStream.println("Errou! Você possui " + tentativasRestantes + " tentativas.");
                } else {
                    outStream.println("Fim de jogo! Você não possui mais tentativas.");
                    break;
                }
            }

            if (palavraDescoberta.equals(palavra)) {
                outStream.println("Fim de jogo! Você acertou! A palavra era " + palavraDescoberta);
                break;
            }
        }
    }
}

