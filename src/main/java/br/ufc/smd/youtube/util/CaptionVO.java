package br.ufc.smd.youtube.util;

public class CaptionVO {
	
	private String idCanal;
	
	private String nomeCanal;
	
	private String idVideo;
	
	private String nomeVideo;
	
	private String idTrack;
	
	private String idTrackMapeado;
	
	public CaptionVO() {
		
	}

	public CaptionVO(String idCanal, String nomeCanal, String idVideo, String nomeVideo, String idTrack,
			String idTrackMapeado) {
		super();
		this.idCanal = idCanal;
		this.nomeCanal = nomeCanal;
		this.idVideo = idVideo;
		this.nomeVideo = nomeVideo;
		this.idTrack = idTrack;
		this.idTrackMapeado = idTrackMapeado;
	}

	public String getIdCanal() {
		return idCanal;
	}

	public void setIdCanal(String idCanal) {
		this.idCanal = idCanal;
	}

	public String getNomeCanal() {
		return nomeCanal;
	}

	public void setNomeCanal(String nomeCanal) {
		this.nomeCanal = nomeCanal;
	}

	public String getIdVideo() {
		return idVideo;
	}

	public void setIdVideo(String idVideo) {
		this.idVideo = idVideo;
	}

	public String getNomeVideo() {
		return nomeVideo;
	}

	public void setNomeVideo(String nomeVideo) {
		this.nomeVideo = nomeVideo;
	}

	public String getIdTrack() {
		return idTrack;
	}

	public void setIdTrack(String idTrack) {
		this.idTrack = idTrack;
	}

	public String getIdTrackMapeado() {
		return idTrackMapeado;
	}

	public void setIdTrackMapeado(String idTrackMapeado) {
		this.idTrackMapeado = idTrackMapeado;
	}

	@Override
	public String toString() {
		return "[idTrackMapeado=" + idTrackMapeado + ", idCanal=" + idCanal + ", nomeCanal=" + nomeCanal + ", idVideo=" + idVideo + ", nomeVideo="
				+ nomeVideo + ", idTrack=" + idTrack + "]";
	}	

}
