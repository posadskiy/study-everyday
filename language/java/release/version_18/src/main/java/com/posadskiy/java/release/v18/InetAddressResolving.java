package com.posadskiy.java.release.v18;

import lombok.extern.log4j.Log4j2;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

@Log4j2
public class InetAddressResolving {

    public static void main(String[] args) {
        final InetAddress[] allByName;
        try {
            allByName = InetAddress.getAllByName("google.com");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        Arrays.stream(allByName).forEach(value -> {
            log.info("Address: " + value.getHostAddress() + ", hostname: " + value.getHostName() + ", canonical hostname: " + value.getCanonicalHostName());
        });
    }
}
