package mining;

/**
 * eccezione lanciata in caso di raggio troppo grande.
 */
public class ClusteringRadiusException extends Exception {
	public ClusteringRadiusException(final String s) {
		super(s);
	}
}
