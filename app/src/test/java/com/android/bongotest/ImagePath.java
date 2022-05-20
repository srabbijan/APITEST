package com.android.bongotest;




import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class ImagePath {

    @Test
    public void testIsPathValid() {
        String testEmail = "nSNle6UJNNuEbglNvXt67m1a1Yn.jpg";
        assertEquals("invalid", testEmail.length() > 0, ImagePathTest.checkImagePath(testEmail));    }
}