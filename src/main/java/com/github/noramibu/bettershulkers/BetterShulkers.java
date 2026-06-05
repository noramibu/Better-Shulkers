package com.github.noramibu.bettershulkers;

import com.github.noramibu.bettershulkers.enchantment.MaterialCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterShulkers {
    public static final Logger LOGGER = LoggerFactory.getLogger("Better Shulkers");

    public static void init() {
        MaterialCollector.initialize();
    }
}
