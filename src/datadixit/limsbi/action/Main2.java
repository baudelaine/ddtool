//package datadixit.limsbi.action;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.nio.CharBuffer;
//import java.nio.charset.Charset;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import datadixit.limsbi.svc.CognosSVC;
//import datadixit.limsbi.svc.ProjectSVC;
//import datadixit.limsbi.svc.TaskerSVC;
//
//
//public class Main2 {
//
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//		
//		try{

		/*	
			<namespace>
				<name locale="en-gb">DATA</name>
				<name locale="en">DATA</name>
				<lastChanged>2017-10-18T17:04:18</lastChanged>
				<lastChangedBy>admin</lastChangedBy>
				<querySubject status="valid">
					<name locale="en-gb">S_SAMPLE</name>
					<name locale="en">S_SAMPLE_SEB</name>
					<lastChanged>2017-10-18T17:06:08</lastChanged>
					<lastChangedBy>admin</lastChangedBy>
					<definition>
						<modelQuery>
							<sql type="cognos">Select <column>*</column>from<table/>
							</sql>
						</modelQuery>
					</definition>
					<queryItem>
						<name locale="en-gb">S_SAMPLEID</name>
						<name locale="en">S_SAMPLEID_SEB</name>
						<lastChanged>2017-10-18T17:06:19</lastChanged>
						<lastChangedBy>admin</lastChangedBy>
						<expression>
							<refobj>[FINAL].[S_SAMPLE].[S_SAMPLEID]</refobj>
						</expression>
						<usage>identifier</usage>
						<datatype>characterLength16</datatype>
						<precision>40</precision>
						<size>82</size>
						<regularAggregate>unsupported</regularAggregate>
						<semiAggregate>unsupported</semiAggregate>
					</queryItem>			
					<queryItem>
						<name locale="en-gb">SAMPLEDESC</name>
						<name locale="en">SAMPLEDESC_SEB</name>
						<lastChanged>2017-10-18T17:17:55</lastChanged>
						
		 	*/
			
			
//			Path path = Paths.get("/mnt/dd3/models/model-14/model/xml");
//			InputStream is = new ByteArrayInputStream(Files.readAllBytes(path));	
			
//			Path path = Paths.get("/mnt/dd3/models/model-14/model.xml");
//			Charset charset = StandardCharsets.UTF_8;
//			String content = new String(Files.readAllBytes(path), charset);
//			String regex = "<namespace>\\"
//				<name locale="en-gb">DATA</name>
//				<name locale="en">DATA</name>";
//			Pattern pattern = Pattern.compile(regex,Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
//			Matcher matcher = pattern.matcher(content);
//			while(matcher.find()){
//				System.out.println(matcher.group(0));
//			}
<<<<<<< HEAD
			
		}
		catch(Exception e){
			e.printStackTrace(System.err);
		}
=======
//			
//		}
//		catch(Exception e){
//			e.printStackTrace(System.err);
//		}
>>>>>>> 70e173b2e4a1fc42c578f86312fb3bb7fa1997c7
		
		
//	}

//}
