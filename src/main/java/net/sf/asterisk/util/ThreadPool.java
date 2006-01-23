/*
 * Copyright  2004-2005 Stefan Reuter
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
package net.sf.asterisk.util;

import java.util.LinkedList;
import java.util.List;

/**
 * A fixed sized thread pool.
 * 
 * @author srt
 * @version $Id: ThreadPool.java,v 1.6 2005/10/25 23:10:33 srt Exp $
 */
public class ThreadPool
{
    private final Log logger = LogFactory.getLog(getClass());
    private boolean running;
    private int numThreads;
    private String name;
    private List jobs;

    /**
     * Creates a new ThreadPool of numThreads size. These Threads are waiting
     * for jobs to be added via the addJob method.
     * 
     * @param name the name to use for the thread group and worker threads.
     * @param numThreads the number of threads to create.
     */
    public ThreadPool(String name, int numThreads)
    {
        PoolThreadGroup group;

        this.name = name;
        this.numThreads = numThreads;
        jobs = new LinkedList();
        running = true;

        group = new PoolThreadGroup(this.name);

        // create and start the threads
        for (int i = 0; i < this.numThreads; i++)
        {
            TaskThread thread;

            thread = new TaskThread(group, this.name + "-TaskThread-" + i);
            thread.start();
        }
        logger.debug("ThreadPool created with " + this.numThreads + " threads.");
    }

    /**
     * Gets a job from the queue. If none is availble the calling thread is
     * blocked until one is added.
     * 
     * @return the next job to service, <code>null</code> if the worker thread
     *         should be shut down.
     */
    Runnable obtainJob()
    {
        Runnable job = null;

        synchronized (jobs)
        {
            while (job == null && running)
            {
                try
                {
                    if (jobs.size() == 0)
                    {
                        jobs.wait();
                    }
                }
                catch (InterruptedException ie)
                {
                }

                if (jobs.size() > 0)
                {
                    job = (Runnable) jobs.get(0);
                    jobs.remove(0);
                }
            }
        }

        if (running)
        {
            // Got a job from the queue
            return job;
        }
        else
        {
            // Shut down the pool
            return null;
        }
    }

    /**
     * Adds a new job to the queue. This will be picked up by the next available
     * active thread.
     */
    public void addJob(Runnable runnable)
    {
        synchronized (jobs)
        {
            jobs.add(runnable);
            jobs.notifyAll();
        }
    }

    /**
     * Turn off the pool. Every thread, when finished with its current work,
     * will realize that the pool is no longer running, and will exit.
     */
    public void shutdown()
    {
        running = false;
        synchronized (jobs)
        {
            jobs.notifyAll();
        }
        logger.debug("ThreadPool shutting down.");
    }

    /**
     * A TaskThread sits in a loop, asking the pool for a job, and servicing it.
     */
    class TaskThread extends Thread
    {
        public TaskThread(ThreadGroup group, String name)
        {
            super(group, name);
        }

        /**
         * Get a job from the pool, run it, repeat. If the obtained job is null,
         * we exit the loop and the thread.
         */
        public void run()
        {
            while (true)
            {
                Runnable job;

                job = obtainJob();

                if (job == null)
                {
                    // no more jobs available as ThreadPool has been closed? ->
                    // end this Thread
                    break;
                }

                job.run();
            }
        }
    }

    /**
     * Provided the exception handler for all task threads.
     */
    class PoolThreadGroup extends ThreadGroup
    {
        public PoolThreadGroup(String name)
        {
            super(name);
        }

        /**
         * Logs all exceptions that are not caught within the task threads.
         */
        public void uncaughtException(Thread t, Throwable e)
        {
            logger.warn("Uncaught exception in Thread " + t.getName(), e);
        }
    }
}
