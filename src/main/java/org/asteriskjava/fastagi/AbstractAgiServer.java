package org.asteriskjava.fastagi;

import org.asteriskjava.util.DaemonThreadFactory;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.SynchronousQueue;

/**
 * Abstract base class for FastAGI and AsyncAGI servers.
 *
 * @since 1.0.0
 */
public abstract class AbstractAgiServer
{
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * The default thread pool size.
     */
    private static final int DEFAULT_POOL_SIZE = 10;

    /**
     * The default thread pool size.
     */
    private static final int DEFAULT_MAXIMUM_POOL_SIZE = 100;

    /**
     * The minimum number of worker threads in the thread pool.
     */
    private int poolSize = DEFAULT_POOL_SIZE;

    /**
     * The maximum number of worker threads in the thread pool. This equals the maximum number of
     * concurrent requests this AgiServer can serve.
     */
    private int maximumPoolSize = DEFAULT_MAXIMUM_POOL_SIZE;

    /**
     * The thread pool that contains the worker threads to process incoming requests.
     */
    private ThreadPoolExecutor pool;

    /**
     * The strategy to use for mapping AgiRequests to AgiScripts that serve them.
     */
    private MappingStrategy mappingStrategy;

    private volatile boolean die = false;

    /**
     * Returns the default number of worker threads in the thread pool.
     *
     * @return the default number of worker threads in the thread pool.
     * @since 1.0.0
     */
    public int getPoolSize()
    {
        return poolSize;
    }

    /**
     * Sets the default number of worker threads in the thread pool.
     * <p/>
     * This is the number of threads that are available even if they are idle.
     * <p/>
     * The default pool size is 10.
     *
     * @param poolSize the size of the worker thread pool.
     * @throws IllegalArgumentException if the new pool size is negative
     * @see {@link java.util.concurrent.ThreadPoolExecutor#setCorePoolSize(int)}
     */
    public synchronized void setPoolSize(int poolSize)
    {
        if (poolSize < 0)
        {
            throw new IllegalArgumentException("New poolSize (" + poolSize + ") is must not be negative");
        }

        if (pool != null)
        {
            pool.setCorePoolSize(poolSize);
        }
        this.poolSize = poolSize;
    }

    /**
     * Returns the maximum number of worker threads in the thread pool.
     *
     * @return the maximum number of worker threads in the thread pool.
     * @since 1.0.0
     */
    public int getMaximumPoolSize()
    {
        return maximumPoolSize;
    }

    /**
     * Sets the maximum number of worker threads in the thread pool.
     * <p/>
     * This equals the maximum number of concurrent requests this AgiServer can serve.
     * <p/>
     * The default maximum pool size is 100.
     *
     * @param maximumPoolSize the maximum size of the worker thread pool.
     * @throws IllegalArgumentException if maximumPoolSize is less than current pool size or less than or equal to 0.
     * @see {@link java.util.concurrent.ThreadPoolExecutor#setMaximumPoolSize(int)}
     */
    public synchronized void setMaximumPoolSize(int maximumPoolSize)
    {
        if (maximumPoolSize <= 0)
        {
            throw new IllegalArgumentException("New maximumPoolSize (" + maximumPoolSize + ") is must be positive");
        }

        if (maximumPoolSize < poolSize)
        {
            throw new IllegalArgumentException("New maximumPoolSize (" + maximumPoolSize + ") is less than current pool size (" + poolSize + ")");
        }

        if (pool != null)
        {
            pool.setMaximumPoolSize(maximumPoolSize);
        }
        this.maximumPoolSize = maximumPoolSize;
    }

    /**
     * Sets the strategy to use for mapping AgiRequests to AgiScripts that serve them.
     *
     * @param mappingStrategy the mapping strategy to use.
     */
    public void setMappingStrategy(MappingStrategy mappingStrategy)
    {
        this.mappingStrategy = mappingStrategy;
    }

    protected MappingStrategy getMappingStrategy()
    {
        return mappingStrategy;
    }

    protected boolean isDie()
    {
        return die;
    }

    protected synchronized void shutdown()
    {
        this.die = true;
        if (pool != null)
        {
            pool.shutdown();
        }
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();

        this.die = true;
        if (pool != null)
        {
            pool.shutdown();
        }
    }

    /**
     * Execute the runnable using the configured ThreadPoolExecutor obtained from {@link #getPool()}.
     *
     * @param command the command to run.
     * @throws RejectedExecutionException if the runnable can't be executed
     */
    protected void execute(Runnable command) throws RejectedExecutionException
    {
        if (isDie())
        {
            logger.warn("AgiServer is shutting down: Refused to execute AgiScript");
            return;
        }

        getPool().execute(command);
    }

    protected void handleException(String message, Exception e)
    {
        logger.warn(message, e);
    }

    private synchronized ThreadPoolExecutor getPool()
    {
        if (pool == null)
        {
            pool = createPool();
            logger.info("Thread pool started.");
        }

        return pool;
    }

    /**
     * Creates a new ThreadPoolExecutor to serve the AGI requests. The nature of this pool
     * defines how many concurrent requests can be handled. The default implementation
     * returns a dynamic thread pool defined by the poolSize and maximumPoolSize properties.<p>
     * You can override this method to change this behavior. For example you can use a cached
     * pool with
     * <pre>
     * return Executors.newCachedThreadPool(new DaemonThreadFactory());
     * </pre>
     *
     * @return the ThreadPoolExecutor to use for serving AGI requests.
     * @see #setPoolSize(int)
     * @see #setMaximumPoolSize(int)
     */
    protected ThreadPoolExecutor createPool()
    {
        return new ThreadPoolExecutor(
                poolSize,
                (maximumPoolSize < poolSize) ? poolSize : maximumPoolSize,
                50000L, TimeUnit.MILLISECONDS,
                new SynchronousQueue<Runnable>(),
                new DaemonThreadFactory()
        );
    }
}
