package com.posadskiy.java.release.v9.features;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;

public class EnhancedDeprecation {
    private final static Logger log = System.getLogger("default");

    public static void main(String[] args) {
        final ForRemoval forRemoval = new ForRemoval();
        log.log(Level.INFO, forRemoval.hashCode());
    }

}

@Deprecated(forRemoval = true, since = "01.09.2023")
class ForRemoval {

}
