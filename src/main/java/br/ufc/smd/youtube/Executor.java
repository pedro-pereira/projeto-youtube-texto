package br.ufc.smd.youtube;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Executor {
	
	// 148 Videos
	// String idCanal = "UCrh8jk3lkIoSBsNJy_t83zw";

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
					for(String video: Search2.listaDeVideos) {
						sb.append(video);
						sb.append("\n");
					}

					Arquivo.escrever("listaDeVideos.txt", sb.toString());
				break;
				
				case GERA_TRACKS:
					// -------------------- MOMENTO #2 - Gera lista de tracks de caption --------------------
				
					Captions2.geraYoutubeComOAuth2();
					
					List<String> tracks = new ArrayList<String>();
				
					List<String> listaDeVideos = Arquivo.geraLista("listaDeVideos.txt");
				
					for(String video: listaDeVideos) {
						tracks.add(Captions2.capturaIdPrimeiroTrack(video));
					}
				
					sb = new StringBuilder();
					for(String track: tracks) {
						sb.append(track);
						sb.append("\n");
					}

					Arquivo.escrever("listaDeTracks.txt", sb.toString());
				break;
				
				case DOWNLOAD_TRACK:
					// -------------------- MOMENTO #3 - Gerar arquivos de transcricao --------------------
					
					Captions2.geraYoutubeComOAuth2();
					
					List<String> listaDeTracks = Arquivo.geraLista("listaDeTracks.txt");
			
					for(String track: listaDeTracks) {
						Captions2.downloadCaption(track, "tracks/caption_" + track + ".srt");
					}
				break;
				
				case TRATA_TRACK:
					FileFilter filter = new FileFilter() {
					    public boolean accept(File file) {
					        return file.getName().endsWith(".srt");
					    }
					};
					File dir = new File("tracks");
					File[] files = dir.listFiles(filter);
					
					String conteudo;
					for (int i = 0; i < files.length; i++) {
						conteudo = Arquivo.ler("tracks/" + files[i].getName());
						Arquivo.escrever("tracks/arquivo_tratado_" + i + ".txt", conteudo);
						conteudo = null;
					}
					
				break;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String lerAcaoDoUsuario() throws IOException {

		String acao = "";

		System.out.print("Escolha a acao a ser executada: ");
		System.out.print("As opções são: 'gera_videos', 'gera_tracks', 'download_track', 'trata_track'");
		BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
		acao = bReader.readLine();

		return acao;
	}
	

}