package com.posadskiy.java.release.v18;

import com.sun.net.httpserver.SimpleFileServer;

import java.net.InetSocketAddress;
import java.nio.file.Path;

public class SimpleWebServer {
    public static void main(String[] args) {
        var server = SimpleFileServer.createFileServer(new InetSocketAddress(8080), Path.of("/Users/dmitrii/Documents/Projects/study-everyday/language/java/release/version_18"), SimpleFileServer.OutputLevel.INFO);
        server.start();
    }
}
