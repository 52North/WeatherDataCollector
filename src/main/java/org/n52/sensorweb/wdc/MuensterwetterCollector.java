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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.Manifest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MuensterwetterCollector {

	private static final String DATA_INTERVAL_MIN = "DATA_INTERVAL_MIN";

	private static final String CONFIG_FILE = "/config.properties";

	private static final long delay = 1000;

    private static Logger LOG = LoggerFactory.getLogger(MuensterwetterCollector.class);

    public void init(){
        final InputStream configStream = getClass().getResourceAsStream(CONFIG_FILE);

        final Properties props = loadProperties(configStream);

        final Timer timer = new Timer("52north-timer");
        timer.scheduleAtFixedRate(new DataCollectionTask(new MuensterwetterRealTimeParser(props)),
        		delay,
        		getPeriod(props));
        
        LOG.info("*** Initialized MuensterwetterCollector ***");
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

    private Properties loadProperties(final InputStream configStream) {
        final Properties p = new Properties();
        try {
            p.load(configStream);
        } catch (final IOException e) {
            LOG.error("Load properties failed");
        }
        return p;
    }
    
    public static void main(final String[] args) {
    	logApplicationMetadata();
    	new MuensterwetterCollector().init();
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
