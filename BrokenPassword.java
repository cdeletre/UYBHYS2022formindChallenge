package com.formind;
import java.util.Random;

class BrokenPassword extends SecurePassword{

    // hacked flag
    public boolean hacked = false;

    // extend the constructor so that two known password can be used
    // to initialize the SecurePassword generator.
    public BrokenPassword(String password1, String password2){
        System.out.println("Trying to hack the random generator with " + password1 + " and " + password2);
        // Let try to hack the random generator
        hackRandom(password1,password2);
    }

    public int nextSeed(long seed) {
        // compute next seed function like java.util.random does
        int bits=32;
        long seed2 = (seed * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
        return (int)(seed2 >>> (48 - bits));
    }

    public Long findSeed(long first, long second){
        // bruteforce the seed from two consecutive random values
        long seed = 0;
        for (int i = 0; i < 65536; i++) {
            seed = first *65536 + i;
            if (nextSeed(seed) == second) {
                return seed;
            }
        }
        return null;
    }
    
    public void hackRandom(String password1, String password2){
        // get integer value from the first part of the first password
        long first = Integer.parseInt(password1.substring(0,6), 36);
        // get integer value from the second part of the first password
        long second = Integer.parseInt(password1.substring(6,12), 36);
        // extract letters from the first password
        String letters = password1.replaceAll("[0-9]","");

        int tries = 4;
        Long seed = null;
        String password2test = "";
        
        // 4 tries, as there are 4 combinations of signs
        while (tries > 0){
            System.out.println("Trying with " + first + " and " + second);

            // Let's try to guess the seed
            seed = findSeed(first,second);
            password2test = "";

            if (seed != null){
                // We have a seed
                System.out.println("Found a seed ("+seed+")");
                random = new Random((seed ^ 0x5DEECE66DL) & ((1L << 48) - 1));
                // The seed that has generated the second value may have been found.
                // Generate, again, this second value so that the random generator is in the next state
                random.nextInt( );
                // Generate as many random values as letters in the password
                for (int i=0; i < letters.length(); i++) {
                    random.nextInt( );
                }

                // Generate the next password
                password2test = generatePassword();
                // Check if the generated password correspond to the second one
                if (password2test.equals(password2)){
                    // We're good to go
                    System.out.println("and the second password matches ! ("+ password2test +")");
                    hacked = true;
                    break;
                }else{
                    // We had a seed but the generated password does not match the second one
                    System.out.println("Sadly the second password doesn't match ("+ password2test +")");
                    // Let's try another combination of signs
                    if( tries % 2 == 0){
                        first = -first;
                    }else{
                        second = -second;
                    }
                    // One try burned
                    tries--;
                }
            }else{
                // No seed has been found with this combination of signs
                System.out.println("Can't find the seed");
                // Let's try another combination
                if( tries % 2 == 0){
                    first = -first;
                }else{
                    second = -second;
                }
                // One try burned
                tries--;
            }
        }
    }


    public static void main(String[] args) {

        // Instance the Broken generator from the two consecutive generated passwords we know
		BrokenPassword generator = new BrokenPassword("7EHPFygG2gq2","7M7y9YTHt800");
        if (generator.hacked){
            // the random generator has been defeated and we can generate the third SecurePassword
		    System. out.println("The third password is: " + generator.generatePassword());
        }else{
            // too bad for us, we didn't find the seed
            System. out.println("Game over");
        }
	}
}