/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dma.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Properties;

import sapphire.util.Logger;

/**
 *
 * @author Iqbal
 */
public class ConfigProperties {

	public static String model;
	public static String modelXML;
	public static String dispatcher;
	public static String PathToXML;
	public static String PublishFolder;

	public static String DefaultLocale;
	public static String ActiveLocale;
	public static String PathToActionLog;
	public static String PathToActions;
	public static String PackageName;
	public static String PathToLocales;

	public static String login;
	public static String pwd;
	public static String nm;

	public static String MODEL_XML;
	// don't add en-gb to the list, it's always enabled by default
	public static ArrayList<String> locales;

	// set properties
	static {
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream("/opt/wks/ddtool/WebContent/res/conf.properties");

			// load a properties file
			prop.load(input);

			// get the property value
			model = prop.getProperty("model");
			modelXML = prop.getProperty("modelXML");
			System.out.println("++++++++++++++++++++++ model=" + model);
			dispatcher = prop.getProperty("dispatcher");
			PathToXML = prop.getProperty("PathToXML");
			System.out.println("++++++++++++++++++++++ PathToXML=" + PathToXML);
			DefaultLocale = prop.getProperty("DefaultLocale");
			ActiveLocale = prop.getProperty("ActiveLocale");
			PublishFolder = prop.getProperty("PublishFolder");
			PathToActionLog = prop.getProperty("PathToActionLog");
			PathToActions = prop.getProperty("PathToActions");
			PackageName = prop.getProperty("PackageName");
			PathToLocales = prop.getProperty("PathToLocales");

			login = prop.getProperty("login");
			pwd = prop.getProperty("pwd");
			nm = prop.getProperty("nm");

			// locales
			locales = new ArrayList<String>(Arrays.asList(prop.getProperty("locales").split(";")));

			// System.out.println(login + " - " + pwd + " - " + nm);
		} catch (IOException ex) {
			lg(ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					lg(e.getMessage());
					e.printStackTrace();
				}
			}
		}

	}

	public ConfigProperties() {

	}

	public static void lg(String msg) {
		Logger.logInfo(" BuildModel.java ", msg);
	}
}
