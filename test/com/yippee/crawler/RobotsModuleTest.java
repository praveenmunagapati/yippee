package com.yippee.crawler;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.yippee.util.Configuration;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class RobotsModuleTest {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(RobotsModuleTest.class);

    RobotsModule rm;
    private String[] allowed = {
            "http://crawltest.cis.upenn.edu",
    };

    @Before
    public void setUp() {
        Configuration.getInstance().setBerkeleyDBRoot("db/test");
        rm = new RobotsModule();
    }

    @Test
    public void testModuleFetchesRobotsTxt() throws MalformedURLException {
        assertTrue(rm.alowedToCrawl(new URL("http://crawltest.cis.upenn.edu")));
    }

    @Test
    public void testForbidden() throws MalformedURLException {
        assertFalse(rm.alowedToCrawl(new URL("http://crawltest.cis.upenn.edu/marie/private")));
    }

    @Test
    public void testGetCrawlDelay() {
        fail("Test not implemented");
    }

}
