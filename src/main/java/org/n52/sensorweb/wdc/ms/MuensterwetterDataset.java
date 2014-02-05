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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.n52.sensorweb.wdc.Dataset;

public class MuensterwetterDataset implements Dataset {

	private static final String CSV_HEADER = "timestamp,temperature,relativeHumidity,windSpeedMperSec,windSpeedKmh,"
			+ "windSpeedBft,windDirectionDegree,windDirection,windMaxGust,"
			+ "airPressure,visibility,weatherCode,weatherCodeText,globalRadiation";

	private double temperature;

    private double relativeHumidity;

    private double windSpeedMperSec;

    private double windSpeedKmh;

    private double windSpeedBft;

    private double windDirectionDegree;

    private String windDirection;

    private double windMaxGust;

    private double airPressure;

    private double visibility;

    private String weatherCode;

    private String weatherCodeText;

    private double globalRadiation;

    private Date time;

    public MuensterwetterDataset() {}

    public void setTemperature(final double temperatureP) {
        temperature = temperatureP;
    }

    public void setRelativeHumidity(final double relativeHumidityP) {
        relativeHumidity = relativeHumidityP;
    }

    public void setWindSpeedMperSec(final double windSpeedP) {
        windSpeedMperSec = windSpeedP;
    }

    public void setWindDirectionDegree(final double windDirectionDegreeP) {
        windDirectionDegree = windDirectionDegreeP;
    }

    public void setWindDirection(final String windDirectionP) {
        windDirection = windDirectionP;
    }

    public void setAirPressure(final double airPressureP) {
        airPressure = airPressureP;
    }

    public void setVisibility(final double visibilityP) {
        visibility = visibilityP;
    }

    public void setWeatherCode(final String weatherCodeP) {
        weatherCode = weatherCodeP;
    }

    public void setWeatherCodeText(final String weatherCodeTextP) {
        weatherCodeText = weatherCodeTextP;
    }

    public void setGlobalRadiation(final double globalRadiationP) {
        globalRadiation = globalRadiationP;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(final Date timeP) {
        time = timeP;
    }

    public void setWindSpeedKmh(final double windSpeedKmhP) {
        windSpeedKmh = windSpeedKmhP;
    }

    public void setWindSpeedBft(final double windSpeedBftP) {
        windSpeedBft = windSpeedBftP;
    }

    public void setWindMaxGust(final double windMaxGustP) {
        windMaxGust = windMaxGustP;
    }

   	@Override
	public String toCSVString(final SimpleDateFormat sdf){
		return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
				sdf.format(time),
				temperature,
				relativeHumidity,
				windSpeedMperSec,
				windSpeedKmh,
				windSpeedBft,
				windDirectionDegree,
				windDirection,
				windMaxGust,
				airPressure,
				visibility,
				weatherCode,
				weatherCodeText,
				globalRadiation);
	}

	@Override
	public String getCSVHeader(){
		return CSV_HEADER;
	}

	@Override
	public String toString()
	{
		return String
				.format("MuensterwetterDataset [temperature=%s, relativeHumidity=%s, windSpeedMperSec=%s, windSpeedKmh=%s, windSpeedBft=%s, windDirectionDegree=%s, windDirection=%s, windMaxGust=%s, airPressure=%s, visibility=%s, weatherCode=%s, weatherCodeText=%s, globalRadiation=%s, time=%s]",
						temperature, relativeHumidity, windSpeedMperSec, windSpeedKmh, windSpeedBft, windDirectionDegree, windDirection, windMaxGust, airPressure, visibility, weatherCode,
						weatherCodeText, globalRadiation, time);
	}

}
