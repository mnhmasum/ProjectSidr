package com.atomix.datamodel;

import java.util.Comparator;

public class OfferPromotionCategories implements Comparable<OfferPromotionCategories> {

	private int id;
	private String categoryName;
	private int createdBy;
	private int updatedBy;
	private int isDelete;
	private String createdAt;
	private String updatedAt;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

 
	public static Comparator<OfferPromotionCategories> CategoryNameComparator 
                          = new Comparator<OfferPromotionCategories>() {
	    public int compare(OfferPromotionCategories offerPromotionCategory1, OfferPromotionCategories offerPromotionCategory2) {
 
	      String categoryName1 = offerPromotionCategory1.getCategoryName().toUpperCase();
	      String categoryName2 = offerPromotionCategory2.getCategoryName().toUpperCase();
 
	      //ascending order
	      return categoryName1.compareTo(categoryName2);
 
	      //descending order
	      //return categoryName2.compareTo(categoryName1);
	    }
 
	};

	@Override
	public int compareTo(OfferPromotionCategories another) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	
	

}
