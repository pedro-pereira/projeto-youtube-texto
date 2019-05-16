package br.ufc.smd.youtube;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import br.ufc.smd.youtube.util.CaptionVO;
import br.ufc.smd.youtube.util.Excel;

public class Search2 {

	private static final String PROPERTIES_FILENAME = "youtube.properties";

	private static final long NUMBER_OF_VIDEOS_RETURNED = 5;
	
	public static List<String> listaDeVideos = new ArrayList<String>();

	private static YouTube youtube;
	
	private static String apiKey;
	
	public static void geraYoutubeComKey() {
		Properties properties = new Properties();
		try {
			InputStream in = Search2.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
			properties.load(in);
			
			apiKey = properties.getProperty("youtube.apikey");

		} catch (IOException e) {
			System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause() + " : " + e.getMessage());
			System.exit(1);
		}

		try {
			youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, 
										  Auth.JSON_FACTORY, 
										  new HttpRequestInitializer() {
												public void initialize(HttpRequest request) throws IOException {}
										  }
										 ).setApplicationName("projetoyoutubeemtexto").build();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public static void gerarListaDeVideos(String idCanal) {
		try {
			YouTube.Search.List search = youtube.search().list("id, snippet");

			search.setKey(apiKey);
			search.setChannelId(idCanal);
			search.setType("video");
			search.setVideoDuration("medium");
			search.setRegionCode("BR");
			search.setVideoCaption("closedCaption");
			search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
			search.setFields("nextPageToken,items(id/kind,id/videoId, snippet/title, snippet/channelTitle)");

			SearchListResponse searchResponse;
			searchResponse = search.execute();
			
			List<SearchResult> searchResultList = searchResponse.getItems();
		
			// VO para salvar na planilha
			List<CaptionVO> listaCaptions = new ArrayList<CaptionVO>();

			// Trabalho o retorno para gerar a lista de videos
			Iterator<SearchResult> iteratorSearchResults = searchResultList.iterator();
			if (!iteratorSearchResults.hasNext()) {
				System.out.println(" There aren't any results for your query. Canal: " + idCanal);
				return;
			} else {
				while (iteratorSearchResults.hasNext()) {
					
					CaptionVO vo = new CaptionVO();
					
					SearchResult singleVideo = iteratorSearchResults.next();
					ResourceId rId = singleVideo.getId();
					
					vo.setIdCanal(idCanal);
					vo.setNomeCanal(singleVideo.getSnippet().getChannelTitle());

					if (rId.getKind().equals("youtube#video")) {
						vo.setIdVideo(rId.getVideoId());
						vo.setNomeVideo(singleVideo.getSnippet().getTitle());
						listaCaptions.add(vo);
					
						listaDeVideos.add(rId.getVideoId());
						System.out.println("Canal: " + idCanal + " - Video: " + rId.getVideoId());
					}
				}
			}
			
			Excel.criaPlanilha(listaCaptions);
			
		} catch (IOException e) {
			System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause() + " : " + e.getMessage());
			System.exit(1);
		}
	}

	
	/*
	public static void main(String[] args) {
		
		String idCanal = "UCrh8jk3lkIoSBsNJy_t83zw";
		
		geraYoutubeComKey();
		
		listaDeVideos = new ArrayList<String>();
		
		do {
			gerarListaDeVideos(idCanal, codigoProximaPagina);
		} while (codigoProximaPagina != null);
		
		System.out.println("Tamanho da lista: " + listaDeVideos.size());
		for (String video : listaDeVideos) {
			System.out.println("Video: " + video);
		}
	}
	*/
}

/*
public static List<SearchResult> gerarListaDeVideos(String idCanal, String proximaPagina) {
	
	List<SearchResult> searchResultList = null;
	codigoProximaPagina = null;
	
	try {
		// Define the API request for retrieving search results.
		YouTube.Search.List search = youtube.search().list("id, snippet");

		// Set your developer key from the Google Developers Console for non-authenticated requests.
		search.setKey(apiKey);
		search.setChannelId(idCanal);
		search.setType("video");
		search.setVideoDuration("medium");
		search.setRegionCode("BR");
		search.setVideoCaption("closedCaption");
		
		// To increase efficiency, only retrieve the fields that the application uses.
		search.setFields("nextPageToken,items(id/kind,id/videoId)");
		search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
		
		search.setPageToken(proximaPagina);

		// Call the API and print results.
		SearchListResponse searchResponse;
				
		searchResponse = search.execute();
		searchResultList = searchResponse.getItems();
		
		codigoProximaPagina = searchResponse.getNextPageToken();	

	} catch (IOException e) {
		System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause() + " : " + e.getMessage());
		System.exit(1);
	}
	
	return searchResultList;
}
*/
 
