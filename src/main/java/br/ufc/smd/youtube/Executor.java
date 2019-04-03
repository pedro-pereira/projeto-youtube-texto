package br.ufc.smd.youtube;

import java.util.ArrayList;

public class Executor {

	public static void main(String[] args) {
		
		// -------------------- MOMENTO #1 --------------------
		
		// 148 Videos
		String idCanal = "UCrh8jk3lkIoSBsNJy_t83zw";

		Search2.geraYoutubeComKey();
		Search2.codigoProximaPagina = null;
		Search2.listaDeVideos = new ArrayList<String>();
		
		do {
			Search2.gerarListaDeVideos(idCanal, null);
		} while (Search2.codigoProximaPagina != null);	
		
		StringBuilder sb = new StringBuilder();
		System.out.println("Tamanho da lista: " + Search2.listaDeVideos.size());
		for (String video : Search2.listaDeVideos) {
			System.out.println("Video: " + video);
			sb.append(video).append("\n");
		}
		
		Arquivo.escrever("d:\\listaDeVideos.txt", sb.toString());
		
		
		// -------------------- MOMENTO #2 --------------------
		
		Captions2.geraYoutubeComOAuth2();

	}

}
