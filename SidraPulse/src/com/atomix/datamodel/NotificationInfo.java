package com.atomix.datamodel;

public class NotificationInfo {
	private int announcement;
	private int classified;
	private int event;
	private int gallery;
	private int offerAndPromotion;
	private int forum;

	public int getAnnouncement() {
		return announcement;
	}

	public void setAnnouncement(int announcement) {
		this.announcement = announcement;
	}

	public int getClassified() {
		return classified;
	}

	public void setClassified(int classified) {
		this.classified = classified;
	}

	public int getEvent() {
		return event;
	}

	public void setEvent(int event) {
		this.event = event;
	}

	public int getGallery() {
		return gallery;
	}

	public void setGallery(int gallery) {
		this.gallery = gallery;
	}

	public int getOfferAndPromotion() {
		return offerAndPromotion;
	}

	public void setOfferAndPromotion(int offerAndPromotion) {
		this.offerAndPromotion = offerAndPromotion;
	}

	public int getForum() {
		return forum;
	}

	public void setForum(int forum) {
		this.forum = forum;
	}

}
