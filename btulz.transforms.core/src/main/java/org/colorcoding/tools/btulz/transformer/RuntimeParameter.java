package org.colorcoding.tools.btulz.transformer;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class RuntimeParameter {
	public long newLong() {
		Random rd = new Random();
		return rd.nextLong();
	}

	public long newInt() {
		Random rd = new Random();
		return rd.nextInt();
	}

	public long newShort() {
		Random rd = new Random();
		return rd.nextInt(32767);
	}

	public long getTime() {
		return (new Date()).getTime();
	}

	public String newUUID() {
		return UUID.randomUUID().toString();
	}
}
