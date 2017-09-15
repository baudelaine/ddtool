/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datadixit.limsbi.pojos;

import java.util.ArrayList;
import oracle.sql.ARRAY;

/**
 *
 * @author splims
 */
public class S extends Link {

	public String TableID; // parent tableid
	public String SdcID; // parent sdcid
	public String KeyID1;
	public String KeyID2;
	public String KeyID3;

	// enfin si c'est une sdi to sdi
	// array list des columns en commun
	public ArrayList<String> CommunColumns;

	public S(String LinkType) {
		super(LinkType);
		CommunColumns = new ArrayList<String>();
	}

}
