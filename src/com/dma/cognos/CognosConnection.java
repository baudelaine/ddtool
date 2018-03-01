package com.dma.cognos;
/*import javax.xml.namespace.QName;
 import com.cognos.developer.schemas.bibus._3.BiBusHeader;
 import com.cognos.developer.schemas.bibus._3.ContentManagerService_PortType;
 import com.cognos.developer.schemas.bibus._3.ContentManagerService_ServiceLocator;
 import com.cognos.developer.schemas.bibus._3.SearchPathSingleObject;
 import com.cognos.developer.schemas.bibus._3.XmlEncodedXML;
 import com.cognos.org.apache.axis.client.Stub;
 import com.cognos.org.apache.axis.message.SOAPHeaderElement;
 import java.io.*;
 */

import javax.xml.namespace.QName;

import com.cognos.developer.schemas.bibus._3.BiBusHeader;
import com.cognos.developer.schemas.bibus._3.ContentManagerService_PortType;
import com.cognos.developer.schemas.bibus._3.ContentManagerService_ServiceLocator;
import com.cognos.developer.schemas.bibus._3.CookieVar;
import com.cognos.developer.schemas.bibus._3.HdrSession;
import com.cognos.developer.schemas.bibus._3.SearchPathSingleObject;
import com.cognos.developer.schemas.bibus._3.XmlEncodedXML;
//import com.cognos.org.apache.axis.client.Stub;
import com.cognos.org.apache.axis.client.Stub;
import com.cognos.org.apache.axis.message.SOAPHeaderElement;

//import javax.servlet.http.*;
public class CognosConnection {

	private String passportID = null;
	private String crn = null;
	private String ceassa = null;
	private String usersession = null;
	private String passport = null;
	private String userCapabilities = null;
	private ContentManagerService_PortType cmService = null;
	private String namespaceID = null;
	private String endPoint = null;
	private BiBusHeader cmBiBusHeader = null;

	public void setProperties(String namespace, String dispacher) {
		this.namespaceID = namespace;
		this.endPoint = dispacher;

	}

	public void logOn(String username, String connectid) throws Exception {
		String userID = username;
		String password = "lmsbpwd";

		ContentManagerService_ServiceLocator cmServiceLocator = null;
		cmService = null;

		cmServiceLocator = new ContentManagerService_ServiceLocator();
		cmService = cmServiceLocator.getcontentManagerService(new java.net.URL(endPoint));

		StringBuffer credentialXML = new StringBuffer();
		credentialXML.append("<credential>");
		credentialXML.append("<namespace>").append(namespaceID).append("</namespace>");
		credentialXML.append("<username>").append(userID).append("</username>");
		credentialXML.append("<password>").append(password).append("</password>");
		credentialXML.append("<connectid>").append(connectid).append("</connectid>");
		credentialXML.append("</credential>");

		String encodedCredentials = credentialXML.toString();

		cmService.logon(new XmlEncodedXML(encodedCredentials), new SearchPathSingleObject[] {});

		// TODO Set the BiBusHeader
		SOAPHeaderElement temp = ((Stub) cmService).getResponseHeader("http://developer.cognos.com/schemas/bibus/3/",
				"biBusHeader");

		cmBiBusHeader = (BiBusHeader) temp
				.getValueAsType(new QName("http://developer.cognos.com/schemas/bibus/3/", "biBusHeader"));
		((Stub) cmService).setHeader("http://developer.cognos.com/schemas/bibus/3/", "biBusHeader", cmBiBusHeader);

		passportID = cmBiBusHeader.getCAM().getCAMPassport().getId();
		crn = cmBiBusHeader.getHdrSession().getSetCookieVars()[0].getValue();
		ceassa = cmBiBusHeader.getHdrSession().getSetCookieVars()[1].getValue();
		usersession = cmBiBusHeader.getHdrSession().getSetCookieVars()[2].getValue();
		passport = cmBiBusHeader.getHdrSession().getSetCookieVars()[3].getValue();
		userCapabilities = cmBiBusHeader.getHdrSession().getSetCookieVars()[4].getValue();
	}

	public void logOff(String cam_passport) throws Exception {
		ContentManagerService_ServiceLocator cmServiceLocator = null;
		cmService = null;

		cmServiceLocator = new ContentManagerService_ServiceLocator();
		cmService = cmServiceLocator.getcontentManagerService(new java.net.URL(endPoint));

		BiBusHeader bibus = new BiBusHeader();
		CookieVar newBiBusCookieVars[] = new CookieVar[1];

		newBiBusCookieVars[0] = new CookieVar();
		newBiBusCookieVars[0].setName("cam_passport");
		newBiBusCookieVars[0].setValue(cam_passport);

		HdrSession hdrSession = new HdrSession();
		hdrSession.setCookieVars(newBiBusCookieVars);
		bibus.setHdrSession(hdrSession);

		((Stub) cmService).setHeader("http://developer.cognos.com/schemas/bibus/3/", "biBusHeader", bibus);

		// logoff from Cognos
		cmService.logoff();

	}

	public String getCRN() {

		return crn;
	}

	public String getCeaSsa() {

		return ceassa;
	}

	public String getUserSession() {

		return usersession;
	}

	public String getPassport() {

		return passport;
	}

	public String getUserCapabilities() {

		return userCapabilities;
	}

	public String getPassportID() {

		return passportID;
	}

	public BiBusHeader getBiBusHeader() {
		return cmBiBusHeader;
	}

	public void setPassportId(String passportId) {
		this.passportID = passportId;
	}
}
