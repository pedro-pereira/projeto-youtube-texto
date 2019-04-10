/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

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

/**
 * Print a list of videos matching a search term.
 *
 * @author Jeremy Walker
 */
public class Search2 {

	/**
	 * Define a global variable that identifies the name of a file that contains the developer's API key.
	 */
	private static final String PROPERTIES_FILENAME = "youtube.properties";

	private static final long NUMBER_OF_VIDEOS_RETURNED = 10;
	
	public static List<String> listaDeVideos = new ArrayList<String>();
	
	public static String codigoProximaPagina;

	/**
	 * Define a global instance of a Youtube object, which will be used to make YouTube Data API requests.
	 */
	private static YouTube youtube;
	
	private static String apiKey;
	
	public static void geraYoutubeComKey() {
		// Read the developer key from the properties file.
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
			// This object is used to make YouTube Data API requests. The last argument is required, but since we don't need anything
			// initialized when the HttpRequest is initialized, we override the interface and provide a no-op function.
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
	
	/**
	 * @param proximaPagina
	 */
	public static void gerarListaDeVideos(String idCanal) {
		try {
			// Define the API request for retrieving search results.
			YouTube.Search.List search = youtube.search().list("id, snippet");

			// Set your developer key from the Google Developers Console for non-authenticated requests.
			search.setKey(apiKey);
			search.setChannelId(idCanal);
			search.setType("video");
			
			//search.setVideoCaption("closedCaption");
			//search.setVideoLicense("creativeCommon");

			// Restrict the search results to only include videos. 
			// See:https://developers.google.com/youtube/v3/docs/search/list#type
			// search.setType("channel,video");

			// To increase efficiency, only retrieve the fields that the application uses.
			search.setFields("nextPageToken,items(id/kind,id/videoId)");
			search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);			

			// Call the API and print results.
			SearchListResponse searchResponse;
			List<SearchResult> searchResultList = null;
		
			searchResponse = search.execute();
			searchResultList = searchResponse.getItems();

			// Trabalho o retorno para gerar a lista de videos
			Iterator<SearchResult> iteratorSearchResults = searchResultList.iterator();
			if (!iteratorSearchResults.hasNext()) {
				System.out.println(" There aren't any results for your query.");
				return;
			} else {
				while (iteratorSearchResults.hasNext()) {
					SearchResult singleVideo = iteratorSearchResults.next();
					ResourceId rId = singleVideo.getId();

					// Confirm that the result represents a video. Otherwise, the item will not contain a video ID.
					if (rId.getKind().equals("youtube#video")) {
						listaDeVideos.add(rId.getVideoId());
					}
				}
			}
		} catch (IOException e) {
			System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause() + " : " + e.getMessage());
			System.exit(1);
		}
	}
	
	/**
	 * Precisa ser melhorado
	 * @param proximaPagina
	 */
	public static void gerarListaDeVideos(String idCanal, String proximaPagina) {
		try {
			// Define the API request for retrieving search results.
			YouTube.Search.List search = youtube.search().list("id, snippet");

			// Set your developer key from the Google Developers Console for non-authenticated requests.
			search.setKey(apiKey);
			// 148 Videos
			search.setChannelId(idCanal);

			// Restrict the search results to only include videos. 
			// See:https://developers.google.com/youtube/v3/docs/search/list#type
			// search.setType("channel,video");

			// To increase efficiency, only retrieve the fields that the application uses.
			search.setFields("nextPageToken,items(id/kind,id/videoId)");
			search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

			// Call the API and print results.
			SearchListResponse searchResponse;
			List<SearchResult> searchResultList = null;
		
			if(proximaPagina == null) {
				searchResponse = search.execute();
				searchResultList = searchResponse.getItems();
				
				System.out.println("--------------------- Proxima página: " + searchResponse.getNextPageToken());
				
				codigoProximaPagina = searchResponse.getNextPageToken();
			}
			
			if(proximaPagina != null) {
				
				search.setPageToken(proximaPagina);
				searchResponse = search.execute();
				searchResultList.addAll(searchResponse.getItems());
				System.out.println("--------------------- Entrei na proxima página, com tamanho " + searchResultList.size());
			}

			// Trabalho o retorno para gerar a lista de videos
			Iterator<SearchResult> iteratorSearchResults = searchResultList.iterator();
			if (!iteratorSearchResults.hasNext()) {
				System.out.println(" There aren't any results for your query.");
				return;
			} else {
				while (iteratorSearchResults.hasNext()) {
					SearchResult singleVideo = iteratorSearchResults.next();
					ResourceId rId = singleVideo.getId();

					// Confirm that the result represents a video. Otherwise, the item will not contain a video ID.
					if (rId.getKind().equals("youtube#video")) {
						listaDeVideos.add(rId.getVideoId());
					}
				}
			}
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
