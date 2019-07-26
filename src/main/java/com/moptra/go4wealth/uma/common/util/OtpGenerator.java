package com.moptra.go4wealth.uma.common.util;

import java.security.SecureRandom;
import java.util.Random;

public class OtpGenerator {
	private static final char[] CHARS = {'0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9'};

	private static final char[] NUMBERS = { '0', '1', '2', '3','4', '5', '6', '7', '8', '9'};

	private Random rand;
	private int length;
	private boolean isOnlyNumber;

	/**
	 * Creates a new generator.
	 * @param length String length of OTP that will be generated.
	 */
	public OtpGenerator(int length,boolean isOnlyNumber) {
		this.length = length;
		this.rand = new SecureRandom();
		this.isOnlyNumber=isOnlyNumber;
	}
	/**
	 * Creates a new generator.
	 * @param length String length of OTP that will be generated
	 */
	public OtpGenerator(int length) {
		this.length = length;
		this.rand = new SecureRandom();
	}

	public String generateOTP() {
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			if(isOnlyNumber)
				sb.append(NUMBERS[rand.nextInt(NUMBERS.length)]);
			else
				sb.append(CHARS[rand.nextInt(CHARS.length)]);
		}
		return sb.toString();
	}
}
