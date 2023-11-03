package org.asteriskjava.config;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * category test case
 */
public class CategoryTest {
    private static Category category;
    @BeforeAll // to gether details before any test run
    public static void getCategoryValue() {
        category = new Category("testString");
    }
    @Test
    public void testRawFormat() {
        String result = category.rawFormat(new StringBuilder()).toString();
        assertEquals("[testString]", result);
    }
}
