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

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParserTask extends TimerTask {

    private static Logger LOG = LoggerFactory.getLogger(ParserTask.class);

    private final Parser parser;

    public ParserTask(final Parser checkerP) {
        parser = checkerP;
    }

    @Override
    public void run() {
        LOG.info("*** Run parser {}", parser);

        parser.parse();

        LOG.info("*** Ran parser. Next run in '{}' minutes.",parser.getParseIntervalMillis()/60000);
    }

    @Override
    public boolean cancel() {
        LOG.info("Cancelling {}",this);
        return super.cancel();
    }

    @Override
    protected void finalize() throws Throwable {
    	LOG.debug("Finalizing {}",this);
        super.finalize();
    }

	@Override
	public String toString()
	{
		return String.format("ParserTask [parser=%s]", parser);
	}

}
