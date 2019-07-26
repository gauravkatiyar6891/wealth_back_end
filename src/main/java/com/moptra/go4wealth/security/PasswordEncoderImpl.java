package com.moptra.go4wealth.security;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderImpl implements PasswordEncoder {
	
	@Override
	public String encode(CharSequence rawPassword) {
		String encodedPassword = BCrypt.hashpw(rawPassword.toString(), BCrypt.gensalt(12));
		return encodedPassword;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		boolean result = BCrypt.checkpw(rawPassword.toString(), encodedPassword);
		return result;
	}

}
