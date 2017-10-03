package datadixit.limsbi.action;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;


public class Test0 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			
			Path path = Paths.get("/opt/wks/ddtool/WebContent/res/test.properties");
			Charset charset = StandardCharsets.UTF_8;

			String content = new String(Files.readAllBytes(path), charset);
//			String newContent = StringUtils.replace(content, "rNNN", "r1234");
			String newContent = content.replaceAll("report-\\d{1,}", "report-12356");
			Files.write(path, newContent.getBytes(charset));
			
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
        
		
	}

}
