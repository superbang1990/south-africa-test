package org.marsclub.test;

import org.junit.Test;

public class FloorplanLogExtractorTest {

    @Test
    public void testExtract() throws Exception {
        String filename = "E:\\south-africa-test\\src\\main\\resources\\Logs-2026-02-23 20_33_46.txt";
        new FloorplanLogExtractor().extract(filename);
    }
}
