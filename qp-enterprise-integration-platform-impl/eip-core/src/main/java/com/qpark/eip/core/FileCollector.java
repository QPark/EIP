/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.LinkedList;

public class FileCollector {
    private static final FileFilter DIRFILTER = new FileFilter() {
	@Override
	public boolean accept(final File pathname) {
	    return pathname.isDirectory();
	}
    };

    public static LinkedList<File> getFiles(final File dir, final String ext) {
	FilenameFilter ff = null;
	if (ext != null) {
	    ff = new FilenameFilter() {
		@Override
		public boolean accept(final File dir, final String name) {
		    return name.endsWith(ext);
		}
	    };
	}
	LinkedList<File> list = new LinkedList<File>();
	getFiles(list, dir, ff);
	return list;
    }

    static void getFiles(final LinkedList<File> list, final File dir, final FilenameFilter ff) {
	if (dir != null) {
	    File[] fs = null;
	    if (ff == null) {
		fs = dir.listFiles();
		for (File f : fs) {
		    if (f.isDirectory()) {
			getFiles(list, f, ff);
		    } else {
			list.add(f);
		    }
		}
	    } else {
		fs = dir.listFiles(DIRFILTER);
		for (File f : fs) {
		    getFiles(list, f, ff);
		}
		list.addAll(Arrays.asList(dir.listFiles(ff)));
	    }
	}
    }
}
