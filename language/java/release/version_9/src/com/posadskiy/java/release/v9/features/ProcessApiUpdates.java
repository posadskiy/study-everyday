package com.posadskiy.java.release.v9.features;

import java.util.stream.Collectors;

/**
 * JEP 102: Process API Updates
 * <a href="https://openjdk.org/jeps/102">Documentation</a>
 */
public class ProcessApiUpdates {

    public static void main(String[] args) {
        // Currently run processes info
        ProcessHandle.allProcesses()
            .map(ProcessHandle::info)
            .collect(Collectors.toList());

        // List of commands
        ProcessHandle.allProcesses()
            .filter(ProcessHandle::supportsNormalTermination)
            .map(processHandle -> processHandle.info().command())
            .collect(Collectors.toList());

        // Destroy all the processes (really do that)
        ProcessHandle.allProcesses()
            .skip(20) // to skip current which couldn't be destroyed,
            .forEach(ProcessHandle::destroy);
    }
}
