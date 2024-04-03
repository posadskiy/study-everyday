package com.posadskiy.design.pattern.creational.factorymethod;

import java.io.File;

public class ControllerV2FirmwareLoader extends UnitFirmwareLoader {
    @Override
    public File downloadFirmware() {
        return new File("location-in-the-local-network");
    }
}
