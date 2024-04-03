package com.posadskiy.design.pattern.creational.factorymethod;

public class Runner {

    public static void main(String[] args) {
        new ControllerV1FirmwareLoader().installFirmware();
        new ControllerV2FirmwareLoader().installFirmware();
        new ControllerV3FirmwareLoader().installFirmware();
    }
}
