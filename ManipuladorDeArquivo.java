import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class ManipuladorDeArquivo {
  private final String caminhoArquivo = "palavras.txt";
  private final int MAX_PALAVRAS = 10;

  public ManipuladorDeArquivo() {}

  public String lerArquivo() {
    Random aleatorio = new Random();
    int valor = aleatorio.nextInt(MAX_PALAVRAS);
    String linha;

    try {
      BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo));
      String palavra = "";
      int i = 0;

      while((linha = reader.readLine()) != null) {
        if(i == valor) palavra = linha;
        i++;
      }
      return palavra;

    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
