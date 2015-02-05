package com.atomix.datamodel;

public class GalleryInfo {
	private int id;
	private String albumTitle;
	private String photoNumber;
	private String videoNumber;
	private String albumImageUrl;
	private int totalAlbum;
	private String date;

	public String getAlbumTitle() {
		return albumTitle;
	}

	public void setAlbumTitle(String albumTitle) {
		this.albumTitle = albumTitle;
	}

	public String getPhotoNumber() {
		return photoNumber;
	}

	public void setPhotoNumber(String photoNumber) {
		this.photoNumber = photoNumber;
	}

	public String getVideoNumber() {
		return videoNumber;
	}

	public void setVideoNumber(String videoNumber) {
		this.videoNumber = videoNumber;
	}

	public String getAlbumImageUrl() {
		return albumImageUrl;
	}

	public void setAlbumImageUrl(String albumImageUrl) {
		this.albumImageUrl = albumImageUrl;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTotalAlbum() {
		return totalAlbum;
	}

	public void setTotalAlbum(int totalAlbum) {
		this.totalAlbum = totalAlbum;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
