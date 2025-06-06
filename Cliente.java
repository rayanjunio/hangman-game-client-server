import java.io.*;
import java.net.*;
/*
 *  Cliente TCP - versão simplificada para o laboratório de redes
 *
 *
 *  Este é um programa cliente simples que ler uma string via teclado e
 *  envia o texto como requisição para um servidor. O código foi
 *  adaptador do SUN Java Tutorial.
 *
 */

public class Cliente {
    public static void main(String[] args) {
        java.util.Scanner teclado = new java.util.Scanner(System.in);
        String enderecoServidor;
        int portaServidor;

        System.out.print("Endereço do servidor: ");
        enderecoServidor = teclado.next();
        System.out.print("Porta do servidor: ");
        portaServidor = teclado.nextInt();

        teclado.nextLine();

        try {
            Socket commsock = new Socket(enderecoServidor, portaServidor);
            PrintStream output = new PrintStream(commsock.getOutputStream());
            BufferedReader input = new BufferedReader(new InputStreamReader(commsock.getInputStream()));
            String fromServer, fromUser;

            System.out.print("Digite uma das opções: 'iniciarjogo' ou 'end':");
            fromUser = teclado.nextLine();
            output.println(fromUser);

            fromServer = input.readLine();
            System.out.println("Resposta do servidor: " + fromServer);

            while (true) {
                if(fromUser.equalsIgnoreCase("end")) break;
                if(fromServer.contains("Fim")) break;

                System.out.println("Digite uma letra ou 'end' para desistir: ");
                fromUser = teclado.nextLine();
                output.println(fromUser.trim());

                fromServer = input.readLine();
                System.out.println("Resposta do servidor: " + fromServer);
            }

            output.close();
            input.close();
            commsock.close();
        } catch (UnknownHostException e) {
            System.err.println("Erro ao conectar ao servidor: " + e);
        } catch (Exception e) {
            System.err.println("Erro:  " + e);
        }
    }
}
