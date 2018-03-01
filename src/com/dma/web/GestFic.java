package com.dma.web;

import java.io.*;
import java.util.*;

public class GestFic {

	private String sourceFile;
	private String targetFile;
	private String sourceEncoding = "UTF-8";
	private String targetEncoding = "UTF-8";
	private BufferedReader reader;
	private BufferedWriter writer;
	PrintStream stdOut = System.out;
	PrintStream stdErr = System.err;
	
	public void writeMap(Map<String, List<String>> m){
		try{
			for(Map.Entry<String, List<String>> e : m.entrySet()){
				writer.write(e.getKey() + "=" + e.getValue() + "\n");
			}
			writer.flush();
		}
		catch(Exception e){
			System.err.println(e);
			e.printStackTrace(System.err);
		}
	}
	
	public void writeHashSet(HashSet<String> input){
		try{
			Iterator<String> i = input.iterator();
			while(i.hasNext()){
				writer.write(i.next() + "\r\n");
			}
			writer.flush();
		}
		catch(Exception e){
			System.err.println(e);
			e.printStackTrace(System.err);
		}
	}
	
	public void writeString(String s){
		try{
			writer.write(s + "\n");
			writer.flush();
			//buf.close();
		}
		catch(Exception e){
			System.err.println(e);
			e.printStackTrace(System.err);
		}
	}

	public void write(BufferedReader br){
		try{
			String line;
			while((line = br.readLine())!=null){
			    writer.write(line);
			}		}
		catch(Exception e){
			System.err.println(e);
			e.printStackTrace(System.err);
		}
	}
	
	public void writeList(List<String> lines){
		try{
			for(int i = 0; i < lines.size(); i++){
				if(i == lines.size() - 1){writer.write(lines.get(i) + "\n");}
				else{writer.write(lines.get(i) + "\n");}
			}
			writer.write("\n");
			writer.flush();
			//buf.close();
		}
		catch(Exception e){
			System.err.println(e);
			e.printStackTrace(System.err);
		}
	}
	
	public String getString(){
		StringBuffer result = new StringBuffer();
		
		try{
			String line = "";
			while((line = reader.readLine()) != null){
				result.append(line + "\n");
			}
		}
		catch(Exception e){
			System.err.println(e);
			e.printStackTrace(System.err);
		}
		
		return result.toString();
	}
	
	public List<String> getList(){
		List<String> result = new ArrayList<String>();
		
		try{
			String line = "";
			while((line = reader.readLine()) != null){
				result.add(line);
			}
		}
		catch(Exception e){
			System.err.println(e);
			e.printStackTrace(System.err);
		}
		return result;
	}
	
	public List<List<String>> getListFromCSV(){
		List<List<String>> result = new ArrayList<List<String>>();
		try{
			String line = "";
			while((line = reader.readLine()) != null){
				line = line.trim();
				List<String> list = Arrays.asList(line.split(","));
				result.add(list);
			}
		}
		catch(Exception e){
			System.err.println(e);
			e.printStackTrace(System.err);
		}
		return result;
	}
	
	public Reader getReader() {
		return reader;
	}
	public Writer getWritter() {
		return writer;
	}
	public String getSource() {
		return sourceFile;
	}
	public void setSource(String sourceFile) {
		this.sourceFile = sourceFile;
	}
	public String getTarget() {
		return targetFile;
	}
	public void setTarget(String targetFile) {
		this.targetFile = targetFile;
	}
	public String getSourceEncoding() {
		return sourceEncoding;
	}
	public void setSourceEncoding(String sourceEncoding) {
		this.sourceEncoding = sourceEncoding;
	}
	public String getTargetEncoding() {
		return targetEncoding;
	}
	public void setTargetEncoding(String targetEncoding) {
		this.targetEncoding = targetEncoding;
	}
	
	public boolean openOutput(String targetFile){
		this.targetFile = targetFile;
		return openOutput();
	}
	
	public boolean openOutput(){
		try{
			//File file = new File(targetFile);
			//if(!file.exists()){file.createNewFile();}
			OutputStream out = new BufferedOutputStream(new FileOutputStream(targetFile));
			writer = new BufferedWriter(new OutputStreamWriter(out, targetEncoding));
		}
		catch(Exception e){
			stdErr.println(e.toString());
			e.printStackTrace(stdErr);
			return false;
			
		}
		return true;
	}
	
	public boolean closeOutput(){
		try{
			writer.flush();
			writer.close();
		}
		catch(Exception e){
			stdErr.println(e);
			e.printStackTrace(stdErr);
		}
		return true;
	}
	
	public boolean openInput(String sourceFile){
		this.sourceFile = sourceFile;
		return openInput();
	}
	
	public boolean openInput(){
		try{
			InputStream in = new BufferedInputStream(new FileInputStream(sourceFile));
			reader = new BufferedReader(new InputStreamReader(in, sourceEncoding));
			
		}
		catch(Exception e){
			stdErr.println(e);
			e.printStackTrace(stdErr);
		}
		return true;
	}
	
	public boolean closeInput(){
		try{
			reader.close();
		}
		catch(Exception e){
			stdErr.println();
			e.printStackTrace(stdErr);
		}
		return true;
	}
	
	public void convert(){
		openInput();
		openOutput();
		
		try{
			int c;
			
			while((c = reader.read()) != -1){
				writer.write(c);
			}
		}
		catch(Exception e){
			stdErr.println(e);
			e.printStackTrace(stdErr);
		}
		
		closeInput();
		closeOutput();
	}
	
}
