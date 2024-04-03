package com.posadskiy.design.pattern.creational.factorymethod;

import java.io.File;

public class ControllerV3FirmwareLoader extends UnitFirmwareLoader {
    @Override
    public File downloadFirmware() {
        return new File("location-on-amazon-s3-storage");
    }
}
