package com.formind;

import java.util.Random;

class SecurePassword {

	// speed-up the algorithm by not re-creating the PRNG
	public Random random = new Random( );

	public String generatePassword() {

		StringBuilder password = new StringBuilder( );
		while (true) {
			// generate a real random 32-bits signed int, ex: 447603982
			int i = this.random.nextInt();
			// convert integer to base36 (lower case + digits), ex: "7ehpfy'
			String passwordPart = Long.toString(i, 36);
			// we have signed integers, remove the "minus" sign
			passwordPart = passwordPart.replace("-", "");
			// append the result to the existing password
			password.append( passwordPart);
			// ANSSI says password length should be 9 minimum
			// stop if we got the minimum complexity
			if (password. length( ) > 10) {
				break;
			}
		}

		// now we have a lower case+digit password, ex: "7ehpfygg2qq2"
		// raise the complexitv to have upper case letters
		StringBuilder result = new StringBuilder( );
		for (int i = 0; i < password.length(); i++) {
			// if we have a letter from ascii code + 1 chance out of
			if ((password.charAt(i) > 96) && random.nextInt( ) % 2 == 0) {
				// convert the lower case letter to upper case
				char x = (char) (password.charAt(i) - 32);
				result.append(x);
			} else {
				// just keep the initial character
				result.append( password.charAt(i));
			}
		}

		return result.toString( );

	}

	public static void main(String[] args) {
		SecurePassword generator = new SecurePassword();

		System.out.println(generator.generatePassword());
		//7EHPFygG2gq2
		System.out.println( generator.generatePassword());
		//7M7V9YTHt800
		System. out.println(generator.generatePassword());
		// ????
	}
}
