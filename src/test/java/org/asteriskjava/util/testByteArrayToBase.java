package org.asteriskjava.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * test byte array class
 */
public class testByteArrayToBase {
    @Test
    public void testByteToBase(){
        byte[] inputBytes = {0x4A, 0x61, 0x76, 0x61}; // byte values
        String expectedBase64 = "SmF2YQ==";
        String resultBase64 = Base64.byteArrayToBase64(inputBytes); // to convert to the result
        assertEquals(expectedBase64, resultBase64); // final comparison
    }
}
