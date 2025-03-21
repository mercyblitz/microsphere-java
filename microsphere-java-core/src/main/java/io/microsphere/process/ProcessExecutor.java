package io.microsphere.process;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeoutException;

import static io.microsphere.process.ProcessManager.INSTANCE;
import static io.microsphere.text.FormatUtils.format;
import static java.lang.Long.getLong;
import static java.lang.Runtime.getRuntime;

/**
 * {@link Process} Executor
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @see ProcessExecutor
 * @since 1.0.0
 */
public class ProcessExecutor {

    private static final long waitForTimeInSecond = getLong("process.executor.wait.for", 1);

    private final ProcessManager processManager = INSTANCE;

    private final Runtime runtime = getRuntime();

    private final String command;

    private final String arguments;

    private boolean finished;

    /**
     * Constructor
     *
     * @param processName command
     * @param arguments   process arguments
     */
    public ProcessExecutor(String processName, String... arguments) {
        StringBuilder argumentsBuilder = new StringBuilder();
        if (arguments != null) {
            for (String argument : arguments) {
                argumentsBuilder.append(" ").append(argument);
            }
        }
        this.arguments = argumentsBuilder.toString();
        this.command = processName + this.arguments;
    }

    /**
     * Execute current process.
     * <p/>
     * //     * @param inputStream  input stream keeps output stream from process
     *
     * @param outputStream output stream for process normal or error input stream.
     * @throws IOException if process execution is failed.
     */
    public void execute(OutputStream outputStream) throws IOException {
        try {
            this.execute(outputStream, Long.MAX_VALUE);
        } catch (TimeoutException e) {
        }
    }

    /**
     * Execute current process.
     * <p/>
     * //     * @param inputStream  input stream keeps output stream from process
     *
     * @param outputStream          output stream for process normal or error input stream.
     * @param timeoutInMilliseconds milliseconds timeout
     * @throws IOException      if process execution is failed.
     * @throws TimeoutException if the execution is timeout over specified <code>timeoutInMilliseconds</code>
     */
    public void execute(OutputStream outputStream, long timeoutInMilliseconds) throws IOException, TimeoutException {
        Process process = runtime.exec(command);
        long startTime = System.currentTimeMillis();
        long endTime = -1L;
        InputStream processInputStream = process.getInputStream();
        InputStream processErrorInputStream = process.getErrorStream();
//        OutputStream processOutputStream = process.getOutputStream();
        int exitValue = -1;
        while (!finished) {
            long costTime = endTime - startTime;
            if (costTime > timeoutInMilliseconds) {
                finished = true;
                processManager.destroy(process);
                String message = format("Execution is timeout[{} ms]!", timeoutInMilliseconds);
                throw new TimeoutException(message);
            }
            try {
                processManager.addUnfinishedProcess(process, arguments);
                while (processInputStream.available() > 0) {
                    outputStream.write(processInputStream.read());
                }
                while (processErrorInputStream.available() > 0) {
                    outputStream.write(processErrorInputStream.read());
                }
                exitValue = process.exitValue();
                if (exitValue != 0) {
                    throw new IOException();
                }
                finished = true;
            } catch (IllegalThreadStateException e) {
                // Process is not finished yet;
                // Sleep a little to save on CPU cycles
                waitFor(waitForTimeInSecond);
                endTime = System.currentTimeMillis();
            } finally {
                processManager.removeUnfinishedProcess(process, arguments);
            }
        }
    }

    /**
     * Wait for specified seconds
     *
     * @param seconds specified seconds
     */
    private void waitFor(long seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.interrupted();
        }
    }

    /**
     * Check current process finish or not.
     *
     * @return <code>true</code> if current process finished
     */
    public boolean isFinished() {
        return finished;
    }
}
