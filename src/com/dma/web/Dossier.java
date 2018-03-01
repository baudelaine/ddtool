package com.dma.web;


public class Dossier{

	String FolderName = "";
	String QSRef = "";
	String QS = "";
	String reoderBefore = "";
	
	public Dossier(String folderName, String qSRef, String reoderBefore) {
		super();
		FolderName = folderName;
		QSRef = qSRef;
		this.reoderBefore = reoderBefore;
	}
	public String getFolderName() {
		return FolderName;
	}
	public void setFolderName(String folderName) {
		FolderName = folderName;
	}
	public String getQSRef() {
		return QSRef;
	}
	public void setQSRef(String qSRef) {
		QSRef = qSRef;
	}
	public String getReoderBefore() {
		return reoderBefore;
	}
	public void setReoderBefore(String reoderBefore) {
		this.reoderBefore = reoderBefore;
	}
	
}
