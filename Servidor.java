import java.net.*;
import java.io.*;
/*
 *  Servidor TCP - versão simplificada para o laboratório de redes
 *
 *  (C) Prof. Dr. Ulisses Rodrigues Afonseca
 *
 *  Este é um programa servidor simples, sem thread, para atender a um
 *  protocolo genérico implementado na classe Protocolo. O código original
 *  foi tirado do SUN Java Tutorial.
 *
 */
 
public class Servidor {
   private static int porta = 4444;
   private static final String versao = "1.0";
   private static final String discoPrincipal = "/";
   public static void main(String args[]) {
      ServerSocket serverSocket = null;
      boolean run = true;
      try {
         serverSocket = new ServerSocket(4444);
      } catch (IOException e)  {
         System.out.println("Nao foi possivel escutar na porta " + porta + "." + e);
         System.out.println("Erro: " + e);
         System.exit(1);
      }

      while(run) {
          Socket clientSocket = null;
          try {
             clientSocket = serverSocket.accept();
          } catch (IOException e)  {
             System.out.println("Falha ao aceitar conexao! Erro: " + e);
             System.exit(1);
          }
          
          System.out.println("Conexão estabelecida com "+clientSocket.getRemoteSocketAddress().toString());
          
          try {
             BufferedReader inStream  = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter    outStream = new PrintWriter(clientSocket.getOutputStream(),true);
             String inputLine, outputLine;

             while ((inputLine = inStream.readLine()) != null) {
				 
				// aqui vamos tratar a requisição realizada pelo cliente 
				 
				switch (inputLine) {
					case "getVersion":
						outputLine = versao;
						break;
					case "getServerTime":
					    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss");
						java.util.Date hora = java.util.Calendar.getInstance().getTime();
						outputLine = sdf.format(hora);
						break;
					case "getFreeSpace":
					    java.io.File principal = new java.io.File(discoPrincipal);
						long freeespace = principal.getFreeSpace();
						outputLine = "Espaço disponível = "+freeespace;
						break;
					case "end": 
					    outputLine = "goodbye";
						break;
					default:
					    outputLine = "mensagem desconhecida!";
				}
                //aqui vamos enviar a resposta produzida para o cliente
                outStream.println(outputLine);
                outStream.flush();
                if (outputLine.equals("goodbye"))
                   break;
             }
             outStream.close();
             inStream.close();
             clientSocket.close();
          } catch (IOException e) {
                System.out.println("Erro ao tratar as requisições! Erro:"+e);
                System.exit(1);
          }
       }
       try {
        serverSocket.close();
       } catch (IOException e) {
                System.out.println("Erro ao encerrar o servidor"+e);
                System.exit(1);
       }
    }
}

