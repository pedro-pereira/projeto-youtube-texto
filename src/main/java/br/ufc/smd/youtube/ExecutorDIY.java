package br.ufc.smd.youtube;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.List;

import br.ufc.smd.youtube.util.CaptionVO;
import br.ufc.smd.youtube.util.Excel;

public class ExecutorDIY {

	public static void main(String[] args) {

		try {
			String acaoString = lerAcaoDoUsuario();
			System.out.println("Voce escolheu " + acaoString + ".");

			Acao acao = Acao.valueOf(acaoString.toUpperCase());
			switch (acao) {

			// -------------------- MOMENTO #1 - Gera lista de videos --------------------
			case GERA_VIDEOS:
				List<String> listaDeCanais = Arquivo.geraLista("listaDeCanais.txt");

				Search2.geraYoutubeComKey();

				for (String idCanal : listaDeCanais) {
					Search2.gerarListaDeVideos(idCanal);
				}
				break;
			
			// -------------------- MOMENTO #2 - Gera lista de tracks de caption --------------------
			case GERA_TRACKS:
				Excel.carregaPlanilha();
				new Thread() {
					@Override
					public void run() {
						try {
							
							List<CaptionVO> listaDeVideos = Excel.listaDeVideos;

							String texto;
							int i = 1;
							for (CaptionVO video : listaDeVideos) {
								texto = DIYCaptionsAdapter.obterTexto(video.getIdVideo());
								Arquivo.escrever("tracks/track_" + i + ".txt", texto);
								
								video.setIdTrackMapeado(String.valueOf(i));
								System.out.println("Video: " + video);
								i = i + 1;
								texto = null;
							}
							
							Excel.editaPlanilha(listaDeVideos);
							
						} catch (URISyntaxException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}.start();
				break;
				
				// -------------------- MOMENTO #3 - Trata marcas no texto --------------------
				case TRATA_TRACK:
					File pasta = new File("tracks/");
					File[] listaDeArquivos = pasta.listFiles();

					for (File arquivo : listaDeArquivos) {
					    if (arquivo.isFile()) {
					    	Arquivo.trataTextoDoArquivo(pasta.getName() + "/" + arquivo.getName());
					    }
					}
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
		System.out.print("As opções são: 'gera_videos', 'gera_tracks', 'trata_track': ");
		BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
		acao = bReader.readLine();

		return acao;
	}

}