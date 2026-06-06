/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers;

import com.github.noramibu.bettershulkers.material.enchantment.MaterialCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterShulkers {
    public static final Logger LOGGER = LoggerFactory.getLogger("Better Shulkers");

    public static void init() {
        MaterialCollector.initialize();
    }
}
