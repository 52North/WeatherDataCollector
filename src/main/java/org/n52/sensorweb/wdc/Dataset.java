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

import java.text.SimpleDateFormat;

/**
 * A data set contains the measurements of certain values with the same 
 * timestamp. In terms of tables or data bases this represents one row.
 * It can be translated to an easy to append to CSV files 
 * {@link String}. 
 * 
 */
public interface Dataset {

	/**
	 * Returns the data of this {@link Dataset} as a {@link String} matching 
	 * the header definition provided by {@link #getCSVHeader()}.
	 * 
	 * @param timestampFormat a {@link SimpleDateFormat} for formatting the 
	 * 				timestamp of this data set.
	 * 
	 * @return a {@link String} representation of this data set
	 */
	String toCSVString(SimpleDateFormat timestampFormat);

	/**
	 * Returns the CSV file header for this data set.
	 * 
	 * @return a {@link String} with the CSV file header.
	 */
	String getCSVHeader();

}