/*
 * (c) 2004 Stefan Reuter
 *
 * Created on Oct 28, 2004
 */
package org.asteriskjava.live;

/**
 * @author srt
 * @version $Id$
 */
public class MiscTest extends AsteriskServerTestCase
{
    public void testGlobalVariables() throws Exception
    {
        server.setGlobalVariable("AJ_TEST_VAR", "foobar");
        assertEquals("foobar", server.getGlobalVariable("AJ_TEST_VAR"));
    }

    public void testFunctions() throws Exception
    {
        System.err.println(server.getGlobalVariable("DB(/Agents,1301)"));
    }
}
