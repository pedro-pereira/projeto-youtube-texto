package br.ufc.smd.youtube;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Arquivo {

	public static void main(String[] args) {
		String conteudo = ler("captionFile.srt");
		escrever("d:\\tabuada.txt", conteudo);
	}
	
	public static String ler(String nomeDoArquivo) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(nomeDoArquivo));
			String linha;
			StringBuilder builder = new StringBuilder();
			
			while ((linha = br.readLine()) != null) {
				if (validaLinha(linha)) {
					System.out.println(linha);
					builder.append(linha + " ");
				}
			}

			br.close();
			return builder.toString();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static void escrever(String nomeDoArquivo, String conteudo) {
		try {
			FileWriter arq = new FileWriter(nomeDoArquivo);
			PrintWriter gravarArq = new PrintWriter(arq);
			gravarArq.printf(conteudo);
			arq.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean validaLinha(String linha) {
		if (!linha.isEmpty() && Character.isAlphabetic(linha.charAt(0))) {
			return true;
		}
		return false;
	}
}