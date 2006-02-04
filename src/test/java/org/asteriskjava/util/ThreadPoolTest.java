/*
 *  Copyright 2004-2006 Stefan Reuter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.asteriskjava.util;

import junit.framework.TestCase;

public class ThreadPoolTest extends TestCase
{
    private ThreadPool pool;
    private int sleepTime = 100;

    protected void setUp() throws Exception
    {
        super.setUp();

        pool = new ThreadPool("test", 2);
    }
    
    protected void tearDown()
    {
        pool.shutdown();
    }

    public void testAddJob() throws Exception
    {
        MyJob job1 = new MyJob();
        MyJob job2 = new MyJob();
        MyJob job3 = new MyJob();

        pool.addJob(job1);
        pool.addJob(job2);
        pool.addJob(job3);

        // wait for all jobs to finish
        while (true)
        {
            if (job1.end != -1 && job2.end != -1 && job3.end != -1)
            {
                break;
            }
            try
            {
                Thread.sleep(sleepTime / 10);
            }
            catch(InterruptedException e)
            {
                // ok...
            }
        }

        assertTrue("Job3 started before Job 1 or Job 2 ended.", job3.start >= job1.end || job3.start >= job2.end);
    }
    
    public void testUncaughtException() throws Exception
    {
        Runnable job;
        
        job = new Runnable()
        {
            public void run()
            {
                throw new RuntimeException();
            }
        };
        
        pool.addJob(job);
        pool.addJob(job);
        
        Thread.sleep(50);
    }

    class MyJob implements Runnable
    {
        public long start = -1;
        public long end = -1;

        public void run()
        {
            start = System.currentTimeMillis();
            try
            {
                Thread.sleep(sleepTime);
            }
            catch (InterruptedException e)
            {
            }
            end = System.currentTimeMillis();
        }
    }
}
