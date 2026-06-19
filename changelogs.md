Removed code to spawn new threads every connection
Now created a pool with thread count
Switched HttpWorkerThread to HttpWorkerInstructions as it not implements Runnable
