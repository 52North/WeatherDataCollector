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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtil {
	
	// 4k buffer
    private static final int BUFFER_SIZE = 1024 * 4;

    private static final String DOWNLOAD_CHARSET = "UTF-8";
    
    private static final Logger LOG = LoggerFactory.getLogger(HttpUtil.class);
	
    /**
     * @param url the URL to the file to download
     * @return the content of the file as String. An empty String will be returned, if anything bad happened.
     */
    public static String downloadFile(final URL url) {
    	try (final InputStream is = url.openStream();
            final ByteArrayOutputStream bout = new ByteArrayOutputStream(BUFFER_SIZE);){
           
           final byte data[] = new byte[BUFFER_SIZE];
           int x = 0;
           while ((x = is.read(data, 0, BUFFER_SIZE)) >= 0) {
               bout.write(data, 0, x);
           }

           final String s = bout.toString(DOWNLOAD_CHARSET);

           return s;
       } catch (final IOException e) {
           LOG.error("Could not open stream to " + url.toString(), e);
           return "";
       }
   }

}
