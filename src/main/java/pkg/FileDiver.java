package pkg;

import java.io.File;

/**
 * This processes files and directories recursively.
 * Almost the same as find by file or directory name and fire a trigger.
 * It may check file content, and more.
 */
public class FileDiver {

	/**
	 * Normally, instantiated with anonymous class.
	 * This may be implemented by lambda.
	 */
	public static interface FileDiverFunction {
		boolean apply(File file) throws Exception;
	}

	public static final int UNLIMITED_DEPTH = -1;

	private static final FileDiver INSTANCE = new FileDiver();

	public static FileDiver getInstance() {
		return INSTANCE;
	}

	private FileDiver() {
	}

	public void dive(File targetDir, FileDiverFunction fileDiverFunction) {
		this.dive(targetDir, fileDiverFunction, UNLIMITED_DEPTH);
	}

	public void dive(File targetDir, FileDiverFunction fileDiverFunction, int depthLimit) {
		if (!targetDir.isDirectory()) {
			throw new IllegalArgumentException("targetDir must be directory");
		}
		this.recursiveDive(targetDir, fileDiverFunction, depthLimit, 1);
	}

	protected void recursiveDive(File targetDir, FileDiverFunction fileDiverFunction, int depthLimit, int currentDepth) {
		for (File child : targetDir.listFiles()) {
			boolean isContinued;
			try {
				isContinued = fileDiverFunction.apply(child);
			} catch (Exception e) {
				throw new RuntimeException("Error occured at " + child.getAbsolutePath(), e);
			}
			if (!isContinued) {
				return;
			}
			if (child.isDirectory()) {
				int nextDepth = currentDepth + 1;
				if (depthLimit == -1 || nextDepth <= depthLimit) {
					this.recursiveDive(child, fileDiverFunction, depthLimit, nextDepth);
				}
			}
		}
	}

}
