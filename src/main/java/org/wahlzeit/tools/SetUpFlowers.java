/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.tools;

import java.io.File;

import org.wahlzeit.main.*;
import org.wahlzeit.services.*;
import org.wahlzeit.utils.PatternInstance;


/**
 * Sets up a fresh clean Wahlzeit Flowers application database.
 */
@PatternInstance(
	patternName = "Command",
	participants = {
		"ConcreteCommand"
	}
)
public class SetUpFlowers extends ScriptMain {

	/**
	 * 
	 */
	public static void main(String[] argv) {
		new SetUpFlowers().run(argv);
	}
	
	/**
	 * 
	 */
	public void startUp(String rootDir) throws Exception {
		super.startUp(rootDir);

		tearDownDatabase();
		setUpDatabase();
		loadGlobals();
	}
	
	/**
	 * 
	 */
	public void execute() throws Exception {
		String photoDir = SysConfig.getRootDirAsString() + File.separator + "config" + File.separator + "flowers";
		createUser("commons", "commons", "commons@wahlzeit.org", photoDir);
	}

}
