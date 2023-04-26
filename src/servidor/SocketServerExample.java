package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class SocketServerExample {
	private static ServerSocket server;
	private static int port = 9876;

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		server = new ServerSocket(port);
		System.out.println("Aguardando cliente");
		

		while (true) {
			Socket socket = server.accept();
			System.out.println("recebeu o cliente com IP: " + socket.getInetAddress().getHostAddress());
			
			Thread multe = new Thread(() -> {
				try {
					ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
					String message = (String) ois.readObject();
					System.out.println("Mensagem recebida no servidor: " + message);

					message = multiplicaMatriz(message);
					Thread.sleep(6000);
					System.out.println("Mensagem enviada no servidor: " + message);

					oos.writeObject(message);
					ois.close();
					oos.close();
					socket.close();

				} catch (IOException e) {
					System.out.println("deu pau!! ");
				} catch (ClassNotFoundException e) {
					System.out.println("deu pau!! ");
					e.printStackTrace();
				} catch (InterruptedException e) {
					System.out.println("");
					e.printStackTrace();
				}
			});
			multe.start();
		}
	}

	//calcula produto de duas matrizes quadradas a partir de uma string com as matrizes concatenadas e devolve uma string com matriz resultado
	public static String multiplicaMatriz(String message) {

		String[] separadorStrings = message.replaceAll("\\[", "").replaceAll("]", "").split(",");
		int[] matriz = new int[separadorStrings.length];		
		for (int i = 0; i < separadorStrings.length; i++) {
			try {
				matriz[i] = Integer.parseInt(separadorStrings[i]);
			} catch (Exception e) {
				System.out.println("matriz incvalida! " + e.getMessage());
			}
		}		
		int tamanho=(int) Math.sqrt((matriz.length/2));	
		int[][] matriz1 = new int[tamanho][tamanho];
		int[][] matriz2 = new int[tamanho][tamanho];
		int[] resultado = new int[matriz.length/2];
		
		int k = 0;
		for (int i = 0; i < tamanho; i++) {
			for (int j = 0; j < tamanho; j++) {
				matriz1[i][j] = matriz[k];
				k++;
			}
		}
		for (int i = 0; i < tamanho; i++) {
			for (int j = 0; j < tamanho; j++) {
				matriz2[i][j] = matriz[k];
				k++;
			}
		}		
		k=0;
		int w=0;
		for (int i = 0; i < tamanho; i++) {
			for (int j = 0; j < tamanho; j++) {
				for (int x = 0; x < tamanho; x++) {
					w += matriz1[i][x] * matriz2[x][j];
				}
				resultado[k]=w;
				k++;
				w=0;
			}
		}
		System.out.println("");
		String vetorVolta = Arrays.stream(resultado).mapToObj(String::valueOf).reduce((x, y) -> x + "," + y).get();
		return vetorVolta;
	}
}
