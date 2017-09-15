/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datadixit.limsbi.pojos;

import java.util.ArrayList;

/**
 *
 * @author splims
 */
public class RF extends Link {

    public String SDCCOLUMNID;
    public String SDCCOLUMNID2;
    public String SDCCOLUMNID3;

    public String KeyID;
    public String KeyID2;
    public String KeyID3;

    public RF(String LinkType) {
        super(LinkType);
        //CommunColumns = new ArrayList<>();
    }

//    @Override
//    public String toString() {
//        return "F{" + "SDCCOLUMNID=" + SDCCOLUMNID + ", SDCCOLUMNID2=" + SDCCOLUMNID2 + ", SDCCOLUMNID3=" + SDCCOLUMNID3 + ", KeyID=" + KeyID + ", KeyID2=" + KeyID2 + ", KeyID3=" + KeyID3 + '}';
//    }
}
