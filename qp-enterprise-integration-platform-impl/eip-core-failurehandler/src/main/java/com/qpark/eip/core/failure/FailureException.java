package com.qpark.eip.core.failure;

/**
 * Exception containing a {@link FailureDescription}.
 * 
 * @author bhausen
 */
public class FailureException extends RuntimeException {
    /** Generated serial version UID */
    private static final long serialVersionUID = 8498005847914551283L;
    /** The {@link FailureDescription}. */
    private FailureDescription failureDescription;

    public FailureException() {
	super();
    }

    public FailureException(final FailureDescription failureDescription) {
	super(failureDescription.getUserMessage());
	this.failureDescription = failureDescription;
    }

    public FailureException(final FailureDescription failureDescription, final Throwable cause) {
	super(failureDescription.getUserMessage(), cause);
	this.failureDescription = failureDescription;
    }

    public FailureException(final String msg) {
	super(msg);
    }

    public FailureException(final Throwable cause) {
	super(cause);
    }

    public FailureException(final String msg, final Throwable cause) {
	super(msg, cause);
    }

    /**
     * @return the failureDescription
     */
    public FailureDescription getFailureDescription() {
	return this.failureDescription;
    }

    /**
     * @param failureDescription
     *            the failureDescription to set.
     */
    public void setFailureDescription(final FailureDescription failureDescription) {
	this.failureDescription = failureDescription;
    }
}
