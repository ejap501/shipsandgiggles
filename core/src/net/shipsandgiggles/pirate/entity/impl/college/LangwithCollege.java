package net.shipsandgiggles.pirate.entity.impl.college;

import net.shipsandgiggles.pirate.entity.college.College;

import java.io.File;

public class LangwithCollege extends College {

	public LangwithCollege(File skin) {
		super(skin, College.Type.LANGWITH);
	}

	@Override
	public boolean perform() {
		return false;
	}
}
