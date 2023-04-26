package cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Random;

public class SoquetClientExample {

	public static void main(String[] args)
			throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {
		InetAddress host = InetAddress.getLocalHost();

		Socket socket = null;
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		
		Random gerador = new Random();
		int tamanho = gerador.nextInt(2, 6);
		System.out.println("Matriz enviada tamanho: " +tamanho+"x"+tamanho);

		int[][] vetor1 = null, vetor2 = null;
		vetor1 = randonica(tamanho);
		vetor2 = randonica(tamanho);
		int[][] vetor3 = null;
		String vetorIda = null;

		vetorIda = conversordesaida(vetor1, vetor2);

		try {
			socket = new Socket(host.getHostName(), 9876);
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject("" + vetorIda);
			System.out.println("Messagem enviada no cliente: " + vetorIda);
		} catch (java.net.ConnectException e) {
			System.out.println("Servidor inoperante! ");
			System.exit(0);
		}
		ois = new ObjectInputStream(socket.getInputStream());
		String message = (String) ois.readObject();
		System.out.println("Messagem recebida no cliente: " + message);

		vetor3 = conversordechegada(message);

		imprimir(vetor3);
		ois.close();
		oos.close();
		Thread.sleep(100);
	}

	// converte dois vetores int em uma string concatenada
	public static String conversordesaida(int[][] vetor11, int[][] vetor22) {
		int[] vetor = new int[((vetor11.length*vetor11.length)+(vetor22.length*vetor22.length))];
		int k = 0;
		for (int i = 0; i < vetor11.length; i++) {
			for (int j = 0; j < vetor11.length; j++) {
				vetor[k] = vetor11[i][j];
				k++;
			}
		}
		for (int i = 0; i < vetor22.length; i++) {
			for (int j = 0; j < vetor22.length; j++) {
				vetor[k] = vetor22[i][j];
				k++;
			}
		}	
		
		String vetorIda = null;
		vetorIda = Arrays.stream(vetor).mapToObj(String::valueOf).reduce((x, y) -> x + "," + y).get();
		return vetorIda;
	}

	// converte uma string em um vetor int
	public static int[][] conversordechegada(String message) {
		String[] separadorStrings = message.replaceAll("\\[", "").replaceAll("]", "").split(",");
		int tamanho= (int) Math.sqrt(separadorStrings.length);
		int[][] matriz = new int[tamanho][tamanho];
		int k = 0;
		for (int i = 0; i < tamanho; i++) {
			for (int j = 0; j < tamanho; j++) {
				try {
					matriz[i][j] = Integer.parseInt(separadorStrings[k]);
					k++;
				} catch (Exception e) {
					System.out.println("Erro nos dados recebidos do servidor: " + e.getMessage());
				}
			}
		}
		
		return matriz;
	}

	// imprime uma matriz
	public static void imprimir(int[][] matriz) {
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz.length; j++) {
				System.out.print("   " + matriz[i][j]);
			}
			System.out.println("   ");
		}
	}

	// gera uma matriz aleatoria
	public static int[][] randonica(int tamanho) {
		Random gerador = new Random();
		int[][] matriz = new int[tamanho][tamanho];

		for (int i = 0; i < tamanho; i++) {
			for (int j = 0; j < tamanho; j++) {
				matriz[i][j] = gerador.nextInt(-99, 99);
			}
		}
		return matriz;
	}
}

