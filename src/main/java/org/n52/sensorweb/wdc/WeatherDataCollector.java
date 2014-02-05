/**
 * ﻿Copyright (C) 2013
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */

package org.n52.sensorweb.wdc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.Manifest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeatherDataCollector {

	private static final String DATA_INTERVAL_MIN = "DATA_INTERVAL_MIN";

	private static final long delay = 1000;

	private static final String EXTERNAL_FILE_PATH = System.getProperty("user.home") + File.separator + ".WeatherDataCollector" + File.separator;

	private static final String FILE_NAME = "config.properties";

	private static final String INTERNAL_FILE_PATH = "/";
	
	private static final String COLLECTOR_IMPLEMENTATION = "COLLECTOR_IMPLEMENTATION";

    private static Logger LOG = LoggerFactory.getLogger(WeatherDataCollector.class);

	private final Properties props = new Properties();

    public void init(){
        loadProperties();

        final Timer timer = new Timer("52n-wdc-timer");
        final DataCollector collector;
        try{
        	collector = DataCollector.class.cast(Class.forName(props.getProperty(COLLECTOR_IMPLEMENTATION)).newInstance());
        } catch(final InstantiationException e){
            throw new IllegalStateException(e);
        } catch(final IllegalAccessException e){
            throw new IllegalStateException(e);
        } catch(final ClassNotFoundException e){
            throw new IllegalStateException(e);
        }
        collector.setProperties(props);
        collector.init();
        
        timer.scheduleAtFixedRate(new DataCollectionTask(collector),
        		delay,
        		getPeriod(props));
        
        LOG.info("*** Initialized WeatherDataCollector ***");
    }

	private int getPeriod(final Properties props)
	{
		try {
			final int periodInMinutes = Integer.parseInt(props.getProperty(DATA_INTERVAL_MIN, "4"));
			final int periodInMillis = 1000 * 60 * periodInMinutes;
			LOG.debug("Period: {}m ({}ms)",periodInMinutes,periodInMillis);
			return periodInMillis;
		} catch (final NumberFormatException nfe) {
			LOG.error("Could not read property '{}'. Please provide a valid number!",DATA_INTERVAL_MIN);
			throw nfe;
		}
	}

    private void loadProperties() {
    	
    	try {
			InputStream is;
			String filePath = EXTERNAL_FILE_PATH + FILE_NAME;
			final File file = new File(filePath);
			if (!file.exists()) {
				LOG.info("Load default settings from jar file");
				filePath = INTERNAL_FILE_PATH + FILE_NAME;
				is = getClass().getResourceAsStream(filePath);
			} else if (!file.canRead()) {
				LOG.warn("Could not load settings.");
				LOG.warn("No reading permissions for " + file);
				LOG.info("Load default settings from jar file");
				filePath = INTERNAL_FILE_PATH + FILE_NAME;
				is = getClass().getResourceAsStream(filePath);
			} else {		
				LOG.info("Load settings from " + file);
				is = new FileInputStream(file);
			}
			 
			props .load(is);     
		} catch (final FileNotFoundException e) {
			LOG.error("WeatherDataCollector settings not found.", e);
			System.exit(1);
		} catch (final IOException e) {
			LOG.error("WeatherDataCollector settings not readable.", e);
			System.exit(1);
		}
    	// try storing properties in user.home
    	final File folder = new File(EXTERNAL_FILE_PATH);
		if (!folder.exists()) {
			
			final boolean successful = folder.mkdir();	
			if (!successful) {
				LOG.warn("WeatherDataCollector settings could not be saved.");
				LOG.warn("No writing permissions at " + folder);
				return;
			} 
		}
		
		final File file = new File(EXTERNAL_FILE_PATH + FILE_NAME);
		LOG.info("Save settings at " + file.getAbsolutePath());	
		
		try { //save properties
			final OutputStream os = new FileOutputStream(file);
			props.store(os, null);  
		} catch (final IOException e) {
			LOG.error("WeatherDataCollector settings could not be saved.", e);
		}
    }
    
    public static void main(final String[] args) {
    	logApplicationMetadata();
    	new WeatherDataCollector().init();
    }
    
    /**
	 * Method print all available information from the jar's manifest file. 
	 */
	private static void logApplicationMetadata() {
		LOG.trace("logApplicationMetadata()");
		InputStream manifestStream;
		String logMessage;
		//
		logMessage = "Application started";
		manifestStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/MANIFEST.MF");
		try {
			final Manifest manifest = new Manifest(manifestStream);
			final Attributes attributes = manifest.getMainAttributes();
			final Set<Object> keys = attributes.keySet();
			for (final Object object : keys) {
				if (object instanceof Name) {
					final Name key = (Name) object;
					logMessage += String.format("\n\t\t%s: %s",key,attributes.getValue(key));
				}
			}
			// add heap information
			logMessage += "\n\t\t" + heapSizeInformation();
		}
		catch(final IOException ex) {
			LOG.warn("Error while reading manifest file from application jar file: " + ex.getMessage());
		}
		LOG.info(logMessage);
	}
	
	protected static String heapSizeInformation() {
		final long mb = 1024 * 1024;
		final Runtime rt = Runtime.getRuntime();
		final long maxMemoryMB = rt.maxMemory() / mb;
		final long totalMemoryMB = rt.totalMemory() / mb;
		final long freeMemoryMB = rt.freeMemory() / mb;
		final long usedMemoryMB = (rt.totalMemory() - rt.freeMemory()) / mb;
		return String.format("HeapSize Information: max: %sMB; total now: %sMB; free now: %sMB; used now: %sMB",
				maxMemoryMB,
				totalMemoryMB,
				freeMemoryMB,
				usedMemoryMB);
	}

}
