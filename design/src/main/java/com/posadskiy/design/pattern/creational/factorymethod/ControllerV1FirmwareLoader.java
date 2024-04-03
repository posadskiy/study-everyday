package com.posadskiy.design.pattern.creational.factorymethod;

import java.io.File;

public class ControllerV1FirmwareLoader extends UnitFirmwareLoader {
    @Override
    public File downloadFirmware() {
        return new File("location-on-usb-drive");
    }
}
