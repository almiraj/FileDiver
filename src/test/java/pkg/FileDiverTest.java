package pkg;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import pkg.FileDiver.FileDiverFunction;

public class FileDiverTest {

	@Test
	public void test_matched_bar() {
		File testDir = new File(this.getClass().getResource("testDir").getFile());
		List<String> actualContents = new ArrayList<>();

		FileDiver.getInstance().dive(testDir, new FileDiverFunction() {
			@Override
			public boolean apply(File file) throws Exception {
				if (file.isFile() && file.getName().contains("bar")) {
					actualContents.add(Files.readAllLines(Paths.get(file.getAbsolutePath())).get(0));
				}
				return true;
			}
		});

		assertEquals(Arrays.asList("bar contents", "foo_bar contents", "foo_bar_buzz contents", "bar3 contents"),
				actualContents);
	}

	@Test
	public void test_matched_bar_depth_limited_by1() {
		File testDir = new File(this.getClass().getResource("testDir").getFile());
		List<String> actualContents = new ArrayList<>();

		FileDiver.getInstance().dive(testDir, new FileDiverFunction() {
			@Override
			public boolean apply(File file) throws Exception {
				if (file.isFile() && file.getName().contains("bar")) {
					actualContents.add(Files.readAllLines(Paths.get(file.getAbsolutePath())).get(0));
				}
				return true;
			}
		}, 1);

		assertEquals(Arrays.asList("bar contents"),
				actualContents);
	}

	@Test
	public void test_matched_bar_depth_limited_by2() {
		File testDir = new File(this.getClass().getResource("testDir").getFile());
		List<String> actualContents = new ArrayList<>();

		FileDiver.getInstance().dive(testDir, new FileDiverFunction() {
			@Override
			public boolean apply(File file) throws Exception {
				if (file.isFile() && file.getName().contains("bar")) {
					actualContents.add(Files.readAllLines(Paths.get(file.getAbsolutePath())).get(0));
				}
				return true;
			}
		}, 2);

		assertEquals(Arrays.asList("bar contents", "foo_bar contents", "bar3 contents"),
				actualContents);
	}

	@Test
	public void test_matched_abcDir() {
		File testDir = new File(this.getClass().getResource("testDir").getFile());
		List<String> actualFoundDirChildren = new ArrayList<>();

		FileDiver.getInstance().dive(testDir, new FileDiverFunction() {
			@Override
			public boolean apply(File file) throws Exception {
				if (file.isDirectory() && file.getName().equals("abcDir")) {
					actualFoundDirChildren.addAll(Arrays.asList(file.list()));
				}
				return true;
			}
		});

		assertEquals(Arrays.asList("aaaDir", "bbb.txt", "ccc.txt"),
				actualFoundDirChildren);
	}

}
