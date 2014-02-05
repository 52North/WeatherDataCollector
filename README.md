WeatherDataCollector
====================

The WeatherDataCollector (WDC) collects weather data from remote sites and translates the received data into a
format that can be used with the 52°North SOS-Importer to be pushed into any running Sensor Observation Service
instance.


## Development

[![Build Status](https://travis-ci.org/52North/WeatherDataCollector.png?branch=master)](https://travis-ci.org/52North/WeatherDataCollector)

If you want to join the WDC developer team, take a look at the CONTRIBUTE.md file.

WDC is written in Java and organized as a Maven single module project.


## Configuration

The application in configured using a properties file. After startup, the application checks for the existence
of this file in  `USER_HOME/.WeatherDataCollector/config.properties`. If this file is not present, the contained
version will be written to this location for enabling configuration at runtime.


## License

OSS is published under Apache Software License, Version 2.0.

### Java Libraries

See NOTICE file.
