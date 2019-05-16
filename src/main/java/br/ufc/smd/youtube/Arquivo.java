package br.ufc.smd.youtube;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Arquivo {
	
	public static List<String> lista;

	public static String ler(String nomeDoArquivo) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(nomeDoArquivo));
			String linha;
			StringBuilder builder = new StringBuilder();
			
			while ((linha = br.readLine()) != null) {
				if (validaLinha(linha)) {
					// System.out.println(linha);
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
	
	public static List<String> geraLista(String nomeDoArquivo) {
		List<String> retorno  = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(nomeDoArquivo));
			String linha;

			retorno = new ArrayList<String>();
			
			while ((linha = br.readLine()) != null) {
				retorno.add(linha);
			}

			br.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return retorno;
	}
	
	public static void escrever(String nomeDoArquivo, String conteudo) {
		try {
			FileWriter arq = new FileWriter(nomeDoArquivo);
			PrintWriter gravarArq = new PrintWriter(arq);
			gravarArq.print(conteudo);
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
	
	public static String trataTextoDoArquivo(String nomeDoArquivo) {
		String texto = ler(nomeDoArquivo);
		texto = texto.replaceAll("\\[.*?\\]","");
		texto = texto.replaceAll("\\(.*?\\)","");
		texto = texto.trim().replaceAll("\\s{2,}", " ");
		System.out.println(texto);
		try {
			FileOutputStream arquivoSaida = new FileOutputStream(new File(nomeDoArquivo));
			arquivoSaida.write(texto.getBytes());
			arquivoSaida.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return texto;
	}
}
