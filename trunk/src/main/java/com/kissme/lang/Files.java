package com.kissme.lang;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

import com.kissme.lang.file.CopyFileCommand;
import com.kissme.lang.file.DeleteFileCommand;
import com.kissme.lang.file.FileCommandInvoker;
import com.kissme.lang.file.MakeFileCommand;
import com.kissme.lang.file.WriteBytesToFileCommand;
import com.kissme.lang.file.WriteFileToCommand;

/**
 * 
 * @author loudyn
 * 
 */
public abstract class Files {

	private static final String UNIX_SEPERATOR = "/";
	private static final String WINDOW_SEPERATOR = "\\";

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static byte[] read(File file) {

		if (!file.exists() || !file.canRead()) {
			throw new IllegalStateException("file is not exist or can not read!");
		}

		ByteArrayOutputStream out = null;
		try {

			out = new ByteArrayOutputStream();
			new WriteFileToCommand(file, out, true);
			return out.toByteArray();
		} catch (Exception e) {
			throw Lang.uncheck(e);
		} finally {
			IOs.freeQuietly(out);
		}
	}

	/**
	 * 
	 * @param file
	 * @param content
	 */
	public static void write(File file, byte[] content) {

		try {

			new MakeFileCommand(file).execute();
			if (file.isDirectory()) {
				throw new IllegalStateException("file[" + file.getName() + "] is a directory!");
			}

			new WriteBytesToFileCommand(file, content).execute();
		} catch (Exception e) {
			throw Lang.uncheck(e);
		}
	}

	/**
	 * 
	 * @param file
	 * @param content
	 * @param encoding
	 */
	public static void write(File file, String content, String encoding) {

		try {

			write(file, content.getBytes(encoding));
		} catch (Exception e) {
			Lang.uncheck(e);
		}
	}

	/**
	 * 
	 * @param file
	 * @param out
	 * @param close
	 */
	public static void writeTo(File file, OutputStream out, boolean close) {
		new WriteFileToCommand(file, out, close).execute();
	}

	/**
	 * 
	 * @param file
	 */
	public static void delete(String file) {
		delete(new File(file));
	}

	/**
	 * 
	 * @param file
	 */
	public static void delete(File file) {
		new DeleteFileCommand(file).execute();
	}

	/**
	 * 
	 * @param directory
	 * @return
	 */
	public static File[] list(String directory) {
		return list(new File(directory));
	}

	/**
	 * 
	 * @param directory
	 * @param filter
	 * @return
	 */
	public static File[] list(String directory, FileFilter filter) {
		return list(new File(directory), filter);
	}

	/**
	 * 
	 * @param directory
	 * @return
	 */
	public static File[] list(File directory) {
		return list(directory, new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return true;
			}
		});
	}

	/**
	 * 
	 * @param directory
	 * @param filter
	 * @return
	 */
	public static File[] list(File directory, FileFilter filter) {
		if (directory.isFile()) {
			return new File[] {};
		}

		List<File> files = new LinkedList<File>();
		list(files, directory, filter);

		return files.toArray(new File[files.size()]);
	}

	private static void list(List<File> files, File directory, FileFilter fileFilter) {
		if (directory.isFile()) {
			files.add(directory);
			return;
		}

		if (!directory.canRead()) {
			return;
		}

		File[] innerFiles = directory.listFiles(fileFilter);
		if (null == innerFiles || innerFiles.length == 0) {
			return;
		}

		for (File inner : innerFiles) {
			list(files, inner, fileFilter);
		}
	}

	/**
	 * 
	 * @param source
	 * @param target
	 */
	public static void copy(String source, String target) {
		copy(new File(source), new File(target));
	}

	/**
	 * 
	 * @param source
	 * @param target
	 */
	public static void copy(File source, File target) {
		try {

			new CopyFileCommand(source, target).execute();
		} catch (Exception e) {
			throw Lang.uncheck(e);
		}
	}

	/**
	 * 
	 * @param source
	 * @param target
	 */
	public static void move(String source, String target) {
		move(new File(source), new File(target));
	}

	/**
	 * 
	 * @param source
	 * @param target
	 */
	public static void move(File source, File target) {

		new FileCommandInvoker().command(new MakeFileCommand(target))
								.command(new CopyFileCommand(source, target))
								.command(new DeleteFileCommand(source))
								.invoke();
	}

	/**
	 * 
	 * @param oldname
	 * @param newname
	 */
	public static void rename(String oldname, String newname) {
		try {

			File file = new File(oldname);
			if (!file.exists()) {
				throw new FileNotFoundException("the old file is not exist!");
			}

			File newFile = new File(newname);
			if (newFile.exists()) {
				throw new IllegalStateException("the new file is exist!");
			}

			file.renameTo(newFile);
		} catch (Exception e) {
			throw Lang.uncheck(e);
		}
	}

	/**
	 * 
	 * @return
	 */
	public static String home() {
		return System.getProperty("user.path");
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static String canonical(File file) {
		try {
			return file.getCanonicalPath();
		} catch (Exception e) {
			throw Lang.uncheck(e);
		}
	}

	/**
	 * 
	 * @param base
	 * @param paths
	 * @return
	 */
	public static String join(String base, String... paths) {
		StringBuilder buf = new StringBuilder().append(base);
		for (String path : paths) {
			buf.append(File.separator).append(path);
		}

		return asPlatform(buf.toString());
	}

	/**
	 * 
	 * @param filename
	 * @return
	 */
	public static String[] split(String filename) {
		filename = asUnix(filename);
		int index = filename.lastIndexOf(UNIX_SEPERATOR);
		if (index == -1) {
			return new String[] { filename, "" };
		}

		return new String[] { filename.substring(0, index), filename.substring(index) };
	}

	/**
	 * 
	 * @param filename
	 * @return
	 */
	public static String major(String filename) {

		String[] split = split(filename);
		String major = Strings.isBlank(split[1]) ? split[0] : split[1];
		int dotIndex = major.lastIndexOf(".");
		if (dotIndex == -1) {
			return major;
		}

		return major.substring(0, dotIndex);
	}

	/**
	 * 
	 * @param filename
	 * @return
	 */
	public static String suffix(String filename) {
		if (Strings.isBlank(filename)) {
			return "";
		}

		int dotIndex = filename.lastIndexOf(".");
		if (dotIndex == -1) {
			return "";
		}

		return filename.substring(dotIndex + 1);
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static String asPlatform(String path) {
		return path.replaceAll("[/|\\\\]+", Matcher.quoteReplacement(File.separator));
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static String asUnix(String path) {
		return path.replaceAll("[/|\\\\]+", UNIX_SEPERATOR);
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static String asWindow(String path) {
		return path.replaceAll("[/|\\\\]+", Matcher.quoteReplacement(WINDOW_SEPERATOR));
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static String asPackage(String path) {
		return path.replaceAll("[/|\\\\]+", "\\.");
	}

	/**
	 * 
	 * @param packageName
	 * @return
	 */
	public static String asPath(String packageName) {
		return packageName.replaceAll("\\.", "/");
	}

	private Files() {}
}
