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
public class DE extends Link {

	// sdi to sdc => les données de la sdc
	public String TableID;
	public String SdcID;
	public String KeyID1;
	public String KeyID2;
	public String KeyID3;

	public DE(String LinkType) {
		super(LinkType);
		CommunColumns = new ArrayList<String>();
	}

}
