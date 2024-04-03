package com.posadskiy.design.pattern.creational.factorymethod;

import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.util.logging.Logger;

@Log4j2
public abstract class UnitFirmwareLoader {
    public static final Logger logger = Logger.getGlobal();

    public void installFirmware() {
        var file = downloadFirmware();
        logger.info("Downloaded firmware from " + file.getPath());

        if (file.canExecute()) {
            logger.info("Installing firmware from " + file.getPath());
            // firmware installation is here
        }
    }

    public abstract File downloadFirmware();
}
