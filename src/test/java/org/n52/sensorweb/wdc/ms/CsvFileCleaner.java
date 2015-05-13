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
package org.n52.sensorweb.wdc.ms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class CsvFileCleaner {
	
	private static final Logger LOG = LoggerFactory.getLogger(CsvFileCleaner.class);

	@Test
	public void fixFile() {
		String[] args = new String[]{ "./src/test/resources/","dd.MM.yyyy HH:mm" };
		File folder = new File(args[0]);
		if (!folder.isDirectory() || !folder.canRead() || !folder.canWrite()) {
			LOG.error("Can not read/write folder '{}'.", folder.getAbsolutePath());
			return;
		}
		DateTimeFormatter dateTimeFormatter;
		try {
			dateTimeFormatter = DateTimeFormat.forPattern(args[1]);
		} catch (IllegalArgumentException iae) {
			LOG.error("Could not parse 'timestamppattern': '{}'.", args[1]);
			return;
		}
		
		// get all files from folder via "csv" extension
		File[] files = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isFile()
						&& pathname.canRead()
						&& pathname.getName().endsWith(".csv");
			}
		});
		if (files.length < 1) {
			LOG.error("No CSV files found in folder '{}'.", folder.getAbsolutePath());
			return;
		}
		Arrays.sort(files);
		// for each file
		for (final File file : files) {
			File newFile = new File(file.getAbsolutePath() + ".nlf");
			try (
					BufferedReader br = new BufferedReader(new FileReader(file));
					BufferedWriter bw = new BufferedWriter(new FileWriter(newFile));
				){
				String previousLine = "";
				String currentLine;
				if (!newFile.exists() && !newFile.createNewFile()) {
					LOG.error("Could not create intermediate file '{}'.",
							newFile.getAbsolutePath());
					return;
				}
				// for each line
				// read line
				while ((currentLine = br.readLine()) != null) {
					if (currentLine.contains("Regen, leicht")) {
						currentLine = currentLine.replaceFirst("Regen, leicht", "\"Regen, leicht\"");
					}
					// if current line starts with "," 
					if (currentLine.charAt(0) == ',') {
						// than combine with previous
						previousLine += currentLine;
					// else
						// write previousLine to new file
						// write currentLine to new file
					} else {
						if (!previousLine.isEmpty()) {
							bw.write(previousLine);
							bw.newLine();
						}
						previousLine = currentLine;
					}
				}
				// if previousLine != null
				if (!previousLine.isEmpty()) {
					// write previousLine to new file
					bw.write(previousLine);
					bw.newLine();
				}
				// flush new file with "nlf" (new line fix) extension
				bw.flush();
			} catch (IOException ioe) {
				LOG.error("Could not read file '{}' or write file '{}'. Exception: '{}'",
						file.getName(),
						newFile.getName(),
						ioe.getLocalizedMessage());
				return;
			}
		
		}
		
		files = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isFile()
						&& pathname.canRead()
						&& pathname.getName().endsWith(".csv.nlf");
			}
		});
		if (files.length < 1) {
			LOG.error("No '.csv.nlf' files found in folder '{}'.", folder.getAbsolutePath());
			return;
		}
		Arrays.sort(files);
		// for each file
		DateTimeZone localZone = DateTimeZone.forID("Etc/GMT-1");
		for (final File file : files) {
			File newFile = new File(file.getAbsolutePath() + ".tsf");
			try (
					BufferedReader br = new BufferedReader(new FileReader(file));
					BufferedWriter bw = new BufferedWriter(new FileWriter(newFile));
				){
				String currentLine;
				if (!newFile.exists() && !newFile.createNewFile()) {
					LOG.error("Could not create intermediate file '{}'.",
							newFile.getAbsolutePath());
					return;
				}
				// for each line
				// read line
				// if line = null
					// finish
				while ((currentLine = br.readLine()) != null) {
					// if line starts not starts with "t" 
					if (currentLine.charAt(0) != 't') {
						// get substring until first ","
						String timeStamp = currentLine.substring(0,currentLine.indexOf(','));
						// transform from UTC+1 to UTC
						timeStamp = dateTimeFormatter.withZone(localZone ).parseDateTime(timeStamp).toDateTime(DateTimeZone.UTC).toString();
						// replace substring until first "," with new time stamp
						currentLine = timeStamp.concat(currentLine.substring(currentLine.indexOf(',')));
					}
					bw.write(currentLine);
					bw.newLine();
				}
				// flush new file with "tsf" (time stamp fix) extension
				bw.flush();
			} catch (IOException ioe) {
				LOG.error("Could not read file '{}' or write file '{}'. Exception: '{}'",
						file.getName(),
						newFile.getName(),
						ioe.getLocalizedMessage());
				return;
			}
		}
	}
}
