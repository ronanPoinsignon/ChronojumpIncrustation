package prog.transmission;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import javafx.concurrent.Task;

public class TacheReception extends Task<String> {

	private static String adresse = "";

	private Socket socket = new Socket();
	private final int port;

	public TacheReception(final int port) {
		this.port = port;
	}

	@Override
	protected String call() throws Exception {
		while(true) {
			try {
				if(!TacheReception.adresse.isEmpty()) {
					socket = new Socket();
					//					System.out.println(TacheReception.adresse + " | " + port);
					socket.connect(new InetSocketAddress(TacheReception.adresse, port), 1000);
					//					System.out.println("connexion");
					lireDonnees();
				}
			}
			catch(final Exception e) {
				//				e.printStackTrace();
				socket.close();
			}
			//			catch(UnknownHostException e) {
			//				socket.close();
			//				//				e.printStackTrace();
			//			}
			//			catch(NoRouteToHostException e) { //en cas de debranchement du fil
			//				socket.close();
			//				//				e.printStackTrace();
			//			}
			//			catch(SocketTimeoutException e) { //en cas d'erreur sur la socket (principalement un runTimeException du au fait que la connexion crash si elle n'est pas effectuée dans les X secondes en fonction de RECHARGEMENT
			//				socket.close();
			//				//				e.printStackTrace();
			//			}
			//			catch(SocketException e) { //en cas de connexion refusée
			//				socket.close();
			//				//				e.printStackTrace();
			//			}
			//			catch (IOException e) {
			//				socket.close();
			//				//				e.printStackTrace();
			//			}
			finally {
				Thread.sleep(500);
			}
		}
	}

	public void lireDonnees() throws IOException {
		byte[] info = new byte[10000];
		InputStream input = null;
		String reponsePrec = "";
		String resultat = null;
		input = socket.getInputStream();
		while(true) {
			input.read(info);
			resultat = new String(info);
			info = new byte[10000];
			resultat = resultat.trim();
			if(resultat != null && !"".equals(resultat) && !resultat.equals(reponsePrec)) {
				System.out.println(resultat);
				updateValue(resultat);
				reponsePrec = resultat;
			}
			//socket.sendUrgentData(1); //remettre cette ligne fait crash l'application après 15 read pour une raison inconnue
		}
	}

	public static void setAdresse(final String adresse) {
		TacheReception.adresse = adresse;
	}
}
