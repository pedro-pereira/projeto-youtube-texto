package br.ufc.smd.youtube;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class DIYCaptionsAdapter {

	public static String obterTexto(String idVideo) throws URISyntaxException, IOException {

		String paginaHtml = "";

		CloseableHttpClient httpclient = HttpClients.createDefault();

		URI uri = new URIBuilder()
					.setScheme("https")
					.setHost("www.diycaptions.com")
					.setPath("/php/get-automatic-captions-as-txt.php")
					.setParameter("id", idVideo)
					.setParameter("language", "asr")
					.build();
		
		HttpGet httpget = new HttpGet(uri);
		CloseableHttpResponse response = httpclient.execute(httpget);

		paginaHtml = EntityUtils.toString(response.getEntity());
		Document documentoHtml = Jsoup.parse(paginaHtml);
		Elements divComTexto = documentoHtml.body().getElementsByAttributeValue("contenteditable", "true");
		
		return divComTexto.text();
	}

	/*
	public static void main(String[] args) {
		try {
			System.out.println(DIYCaptionsAdapter.obterTexto("7IDmRC0yyM0"));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	*/
}
