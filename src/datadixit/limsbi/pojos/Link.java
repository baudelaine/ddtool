/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datadixit.limsbi.pojos;

import java.util.ArrayList;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 *
 * @author splims
 */
@JsonIgnoreProperties({ "LinkedFinale" })
public class Link {

	public String LinkType;
	public boolean isF2F;
	public boolean isUseFieldName;

	public QuerySubject LinkedFinale;
	public String FinalFolder;

	public ArrayList<String> CommunColumns;

	public Link(String LinkType) {
		this.LinkType = LinkType;
		isF2F = false;
		isUseFieldName = false;
		FinalFolder = null;
		CommunColumns = new ArrayList<String>();
	}

}
