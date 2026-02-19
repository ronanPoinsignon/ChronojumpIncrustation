package prog.transmission.tache;

import javafx.concurrent.Task;
import prog.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;

public abstract class AbstractTacheReception<T> extends Task<T> {

	private static String adresse = Utils.getLiveIp();

	private Socket socket = new Socket();
	private final int port;

	public AbstractTacheReception(final int port) {
		this.port = port;
	}

	@Override
	protected T call() throws Exception {
		while(true) {
			try {
				if(!AbstractTacheReception.adresse.isEmpty()) {
					socket = new Socket();
					socket.connect(new InetSocketAddress(AbstractTacheReception.adresse, port), 1000);
					lireDonnees();
				}
			}
			catch(UnknownHostException e) {
//				e.printStackTrace();
//				socket.close();
			}
			catch(NoRouteToHostException e) { //en cas de debranchement du fil
//				e.printStackTrace();
//				socket.close();
			}
			catch(SocketTimeoutException e) { //en cas d'erreur sur la socket (principalement un runTimeException du au fait que la connexion crash si elle n'est pas effectuée dans les X secondes en fonction de RECHARGEMENT
//				e.printStackTrace();
//				socket.close();
			}
			catch(SocketException e) { //en cas de connexion refusée
//				e.printStackTrace();
//				socket.close();
			}
			catch (IOException e) {
//				e.printStackTrace();
//				socket.close();
			}
			catch(final Exception e) {
				e.printStackTrace();
//				socket.close();
			}
			finally {
				Thread.sleep(500);
			}
		}
	}

	public void lireDonnees() throws IOException {
		char[] info = new char[10000];
		InputStream input;
        String resultat;
		input = socket.getInputStream();
		InputStreamReader isr = new InputStreamReader(input, StandardCharsets.ISO_8859_1);
		while(true) {
			int readResult = isr.read(info);
			if(readResult == -1) {
				throw new SocketTimeoutException("Server stopped.");
			}

			resultat = new String(info);
			info = new char[10000];
			resultat = parseQuote(resultat.trim());
			if(!resultat.isEmpty()) {
				updateValue(convert(resultat));
            }
		}
	}

	/**
	 * Remplace les doubles quotes dans les strings dans les formats json pour éviter une erreur lors de la lecture
	 * @param value
	 * @return
	 */
	private String parseQuote(String value) {
		if(value == null) {
			return null;
		}

		String toReplace = "″";
		return value.replaceAll("(?<![:(, *){])\"(?![,:}])", toReplace);
	}

	protected abstract T convert(String value);
	public abstract void reset();

	public static void setAdresse(final String adresse) {
		AbstractTacheReception.adresse = adresse;
	}
}
