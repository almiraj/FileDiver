package pkg;

import java.io.File;

/**
 * This processes files and directories recursively.
 * Almost the same as search by file or directory name and fire a trigger.
 * It may check file content, and more.
 */
public class FileDiver {

	/**
	 * Normally, instantiated with anonymous class, or implemented by lambda.
	 */
	public static interface FileDiverFunction {
		/**
		 * Callback function.
		 *
		 * @param file Found file or directory
		 * @return If returns {@code false}, stop recursively search
		 * @throws Exception any exception
		 */
		public boolean apply(File file) throws Exception;
	}

	private static final int UNLIMITED_DEPTH = -1;

	private static final FileDiver INSTANCE = new FileDiver();

	public static FileDiver getInstance() {
		return INSTANCE;
	}

	private FileDiver() {
	}

	/**
	 * Recursively search files or directories, and process it by {@code fileDiverFunction}.
	 *
	 * @param targetDir
	 * @param fileDiverFunction callback function
	 */
	public void dive(File targetDir, FileDiverFunction fileDiverFunction) {
		this.dive(targetDir, fileDiverFunction, UNLIMITED_DEPTH);
	}

	/**
	 * On limited depth, recursively search files or directories, and process it by {@code fileDiverFunction}.
	 *
	 * @param targetDir
	 * @param fileDiverFunction callback function
	 * @param depthLimit starts by 1
	 */
	public void dive(File targetDir, FileDiverFunction fileDiverFunction, int depthLimit) {
		if (depthLimit != UNLIMITED_DEPTH && depthLimit <= 0) {
			throw new IllegalArgumentException("depthLimit must be more than 1");
		}
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
