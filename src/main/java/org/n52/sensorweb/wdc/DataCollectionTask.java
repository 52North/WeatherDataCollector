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
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataCollectionTask extends TimerTask {

    private static Logger LOG = LoggerFactory.getLogger(DataCollectionTask.class);

    private final DataCollector dataCollector;
    
    private static final ReentrantLock oneCollectorLock = new ReentrantLock(true);

    public DataCollectionTask(final DataCollector checkerP) {
        dataCollector = checkerP;
    }

    @Override
    public void run() {
        LOG.info("*** Run dataCollector {}", dataCollector);
		 // used to sync access to lastUsedDateFile and to not have more than one collector at a time.
        oneCollectorLock.lock();

        // TODO here we should handle the file writing and appending on a global level
        try {
        	dataCollector.collectWeatherData();
        	LOG.info("*** Ran dataCollector. Next run in '{}' minutes.",dataCollector.getParseIntervalMillis()/60000);
        } 
        finally {
        	oneCollectorLock.unlock();
        }
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
		return String.format("DataCollectionTask [dataCollector=%s]", dataCollector);
	}

}
