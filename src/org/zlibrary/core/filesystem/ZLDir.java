package org.zlibrary.core.filesystem;

import java.util.*;
import org.zlibrary.core.util.*;

public abstract class ZLDir {
	private String myPath;

	public static ZLDir root() {
		return ZLFSManager.instance().rootDirectory();
	}

	public ZLDir(String path) {
		myPath = path;
		ZLFSManager.instance().normalize(myPath);
	}
	
	public String path() {
		return myPath;
	}
	
	public String name() {
		int index = ZLFSManager.instance().findLastFileNameDelimiter(myPath);
		return myPath.substring(index + 1);
	}
	
	public String parentPath() {
		return ZLFSManager.instance().parentPath(myPath);
	}
	
	public String itemPath(String itemName) {
		if (itemName == "..") {
			return parentPath();
		} else {
			return isRoot() ? myPath + itemName : myPath + delimiter() + itemName;
		}

	}
	
	public boolean isRoot() {
		return myPath == ZLFSManager.instance().rootDirectoryPath();
	}

	abstract public ArrayList collectSubDirs();
	abstract public ArrayList collectFiles();

	abstract protected String delimiter();
}
