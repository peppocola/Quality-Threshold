package data;

/**
 * eccezione lanciata in caso di Dataset vuoto.
 */

public class EmptyDatasetException extends Exception {
	public EmptyDatasetException(final String s) {
		super(s);
	}
}
