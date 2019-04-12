package br.ufc.smd.youtube;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.List;

public class Executor2 {

	public static void main(String[] args) {

		try {
			String acaoString = lerAcaoDoUsuario();
			System.out.println("Voce escolheu " + acaoString + ".");

			Acao acao = Acao.valueOf(acaoString.toUpperCase());
			switch (acao) {

			case GERA_VIDEOS:

				// -------------------- MOMENTO #1 - Gera lista de videos --------------------

				List<String> listaDeCanais = Arquivo.geraLista("listaDeCanais.txt");

				Search2.geraYoutubeComKey();

				for (String idCanal : listaDeCanais) {
					Search2.gerarListaDeVideos(idCanal);
				}

				StringBuilder sb = new StringBuilder();
				for (String video : Search2.listaDeVideos) {
					sb.append(video);
					sb.append("\n");
				}

				Arquivo.escrever("listaDeVideos.txt", sb.toString());
				break;

			case GERA_TRACKS:
				// -------------------- MOMENTO #2 - Gera lista de tracks de caption --------------------
	
				new Thread() {
					@Override
					public void run() {
						try {	
							List<String> listaDeVideos = Arquivo.geraLista("listaDeVideos.txt");

							String texto;
							int i = 1;
							for (String video : listaDeVideos) {
								texto = DIYCaptionsAdapter.obterTexto(video);
								Arquivo.escrever("tracks/track_" + i + ".txt", texto);
								System.out.println("Sequencial: " + i + " - Video: " + video);
								i = i + 1;
								texto = null;
							}						
						} catch (URISyntaxException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}.start();
				break;
			default:
				break;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String lerAcaoDoUsuario() throws IOException {

		String acao = "";

		System.out.print("Escolha a acao a ser executada: ");
		System.out.print("As opções são: 'gera_videos' ou 'gera_tracks' ");
		BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
		acao = bReader.readLine();

		return acao;
	}

}