package br.ufc.smd.youtube.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class Excel {

	private static final String nomeArquivo = "listaDeVideos.xls";

	private static FileOutputStream arquivoSaida;
	private static FileInputStream arquivoEntrada;

	private static HSSFWorkbook workbook;
	private static HSSFSheet sheetTracks;

	public static List<CaptionVO> listaDeVideos;

	private static void criaArquivo(String nomeDoArquivo) {
		try {
			arquivoSaida = new FileOutputStream(new File(nomeDoArquivo));
			System.out.println("Arquivo Excel criado com sucesso!");

			workbook = new HSSFWorkbook();
			sheetTracks = workbook.createSheet("Tracks");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Arquivo não encontrado!");
		}
	}

	public static void criaPlanilha(List<CaptionVO> listaCaptions) {
		criaArquivo(nomeArquivo);

		int rownum = 0;
		
		Row row = sheetTracks.createRow(rownum++);

		row.createCell(0).setCellValue("Canal ID");
		row.createCell(1).setCellValue("Canal Nome");
		row.createCell(2).setCellValue("Video ID");
		row.createCell(3).setCellValue("Vídeo Nome");
		
		for (CaptionVO caption : listaCaptions) {
			row = sheetTracks.createRow(rownum++);
			int cellnum = 0;

			Cell cellIdCanal = row.createCell(cellnum++);
			cellIdCanal.setCellValue(caption.getIdCanal());

			Cell cellNomeCanal = row.createCell(cellnum++);
			cellNomeCanal.setCellValue(caption.getNomeCanal());

			Cell cellIdVideo = row.createCell(cellnum++);
			cellIdVideo.setCellValue(caption.getIdVideo());

			Cell cellNomeVideo = row.createCell(cellnum++);
			cellNomeVideo.setCellValue(caption.getNomeVideo());

			Cell cellIdTrack = row.createCell(cellnum++);
			cellIdTrack.setCellValue(caption.getIdTrack());

			Cell cellIdTrackMapeado = row.createCell(cellnum++);
			cellIdTrackMapeado.setCellValue(caption.getIdTrackMapeado());
		}

		try {
			workbook.write(arquivoSaida);
			arquivoSaida.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erro na edição do arquivo!");
		}
	}

	public static void carregaPlanilha() {
		try {
			arquivoEntrada = new FileInputStream(new File(nomeArquivo));
			workbook = new HSSFWorkbook(arquivoEntrada);

			HSSFSheet sheetTracksLeitura = workbook.getSheetAt(0);

			listaDeVideos = new ArrayList<CaptionVO>();

			for (int i = 1; i < sheetTracksLeitura.getPhysicalNumberOfRows(); i++) {
				Row row = sheetTracksLeitura.getRow(i);
				CaptionVO vo = new CaptionVO();
				vo.setIdCanal(row.getCell(0).getStringCellValue());
				vo.setNomeCanal(row.getCell(1).getStringCellValue());
				vo.setIdVideo(row.getCell(2).getStringCellValue());
				vo.setNomeVideo(row.getCell(3).getStringCellValue());
				listaDeVideos.add(vo);
			}
			arquivoEntrada.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Arquivo Excel não encontrado!");

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erro na edição do arquivo!");
		}
	}

	public static void editaPlanilha(List<CaptionVO> listaCaptions) {
		try {
			criaArquivo(nomeArquivo);

			int rownum = 0;
			
			Row row = sheetTracks.createRow(rownum++);

			row.createCell(0).setCellValue("Canal ID");
			row.createCell(1).setCellValue("Canal Nome");
			row.createCell(2).setCellValue("Video ID");
			row.createCell(3).setCellValue("Vídeo Nome");
			row.createCell(4).setCellValue("Track Mapa");
			
			for (CaptionVO caption : listaCaptions) {
				row = sheetTracks.createRow(rownum++);
				int cellnum = 0;

				Cell cellIdCanal = row.createCell(cellnum++);
				cellIdCanal.setCellValue(caption.getIdCanal());

				Cell cellNomeCanal = row.createCell(cellnum++);
				cellNomeCanal.setCellValue(caption.getNomeCanal());

				Cell cellIdVideo = row.createCell(cellnum++);
				cellIdVideo.setCellValue(caption.getIdVideo());

				Cell cellNomeVideo = row.createCell(cellnum++);
				cellNomeVideo.setCellValue(caption.getNomeVideo());

				Cell cellIdTrackMapeado = row.createCell(cellnum++);
				cellIdTrackMapeado.setCellValue(caption.getIdTrackMapeado());
			}

			workbook.write(arquivoSaida);
			arquivoSaida.close();
			System.out.println("Arquivo Excel editado com sucesso!");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Arquivo Excel não encontrado!");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erro na edição do arquivo!");
		}
	}
}