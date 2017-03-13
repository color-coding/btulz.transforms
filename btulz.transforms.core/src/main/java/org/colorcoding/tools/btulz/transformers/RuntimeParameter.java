package org.colorcoding.tools.btulz.transformers;

import java.util.Random;
import java.util.UUID;

public class RuntimeParameter {
	public long newLong() {
		Random rd = new Random();
		return rd.nextLong();
	}

	public String newUUID() {
		return UUID.randomUUID().toString();
	}
}
