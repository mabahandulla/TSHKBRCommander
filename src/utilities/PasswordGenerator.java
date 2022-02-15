package utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * This is a class for randomly generating a new password string.
 * <p>
 * The class code is taken from here:
 * <p>
 * https://stackoverflow.com/questions/19743124/java-password-generator
 * <p>
 * @author Georgios Syngouroglou
 */

public final class PasswordGenerator {

	private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
	private static final String UPPER = LOWER.toUpperCase();
    private static final String DIGITS = "0123456789";
    private static final String PUNCTUATION = "!@#$%&*()_+-=[]|,./?><";
    private boolean useLower;
    private boolean useUpper;
    private boolean useDigits;
    private boolean usePunctuation;
    private int passwordLength;


    
    
    
    public PasswordGenerator() {
    	throw new IllegalArgumentException("ERROR: Empty constructor of PasswordGenerator class is not supported.");
    }

    private PasswordGenerator(PasswordGeneratorBuilder builder) {
        this.useLower = builder.useLower;
        this.useUpper = builder.useUpper;
        this.useDigits = builder.useDigits;
        this.usePunctuation = builder.usePunctuation;
        this.passwordLength = builder.passwordLength;
    }
	

    public static class PasswordGeneratorBuilder {

        private boolean useLower;
        private boolean useUpper;
        private boolean useDigits;
        private boolean usePunctuation;
        private int passwordLength;

        public PasswordGeneratorBuilder() {
            this.useLower = false;
            this.useUpper = false;
            this.useDigits = false;
            this.usePunctuation = false;
            this.passwordLength = 12;
        }

        /**
         * Set true in case you would like to include lower characters
         * (abc...xyz). Default false.
         *
         * @param useLower true in case you would like to include lower
         * characters (abc...xyz). Default false.
         * @return the builder for chaining.
         */
        public PasswordGeneratorBuilder useLower(boolean useLower) {
            this.useLower = useLower;
            return this;
        }

        /**
         * Set true in case you would like to include upper characters
         * (ABC...XYZ). Default false.
         *
         * @param useUpper true in case you would like to include upper
         * characters (ABC...XYZ). Default false.
         * @return the builder for chaining.
         */
        public PasswordGeneratorBuilder useUpper(boolean useUpper) {
            this.useUpper = useUpper;
            return this;
        }

        /**
         * Set true in case you would like to include digit characters (123..).
         * Default false.
         *
         * @param useDigits true in case you would like to include digit
         * characters (123..). Default false.
         * @return the builder for chaining.
         */
        public PasswordGeneratorBuilder useDigits(boolean useDigits) {
            this.useDigits = useDigits;
            return this;
        }

        /**
         * Set true in case you would like to include punctuation characters
         * (!@#..). Default false.
         *
         * @param usePunctuation true in case you would like to include
         * punctuation characters (!@#..). Default false.
         * @return the builder for chaining.
         */
        public PasswordGeneratorBuilder usePunctuation(boolean usePunctuation) {
            this.usePunctuation = usePunctuation;
            return this;
        }

        
        /**
         * Set passwordLength. Default passwordLength = 12.
         *
         * @param setPasswordLength. Default passwordLength = 12.
         * @return the builder for chaining.
         */
        public PasswordGeneratorBuilder setPasswordLength(int passwordLength) {
            this.passwordLength = passwordLength;
            return this;
        }
        
        
        
        /**
         * Get an object to use.
         *
         * @return the {@link utilities.PasswordGenerator}
         * object.
         */
        public PasswordGenerator build() {
            return new PasswordGenerator(this);
        }
    }

    
    
    /**
     * This method will generate a password depending the use* properties you
     * define. It will use the categories with a probability. It is not sure
     * that all of the defined categories will be used.
     *
     * @return a password that uses the categories you define when constructing
     * the object with a probability.
     */
    
    public String generate() {

        if (this.passwordLength <= 0) {
            return "";
        }


        StringBuilder password = new StringBuilder(this.passwordLength);
        Random random = new Random(System.nanoTime());

        // Collect the categories to use.
        List<String> charCategories = new ArrayList<>(4);
        
        if (useLower) {
            charCategories.add(LOWER);
        }
        if (useUpper) {
            charCategories.add(UPPER);
        }
        if (useDigits) {
            charCategories.add(DIGITS);
        }
        if (usePunctuation) {
            charCategories.add(PUNCTUATION);
        }

        /* Build the password. */
        for (int i = 0; i < this.passwordLength; i++) {
            password.append(this.getRandomChar(charCategories, random));
        }
	    
	    /* Check if the first char in password is Letter and if not, change it */
	    if (!Character.isLetter(password.charAt(0))) {
		    charCategories.clear();
	        charCategories.add(LOWER);
	        charCategories.add(UPPER);
	    	password.setCharAt(0, this.getRandomChar(charCategories, random));	
	    }
        
        /* Check if the characters are repeated and if so, replace the repeated character with another random one */
	    for(int i = 0; i < password.length() - 1; i++) {
	       	char nextChar = password.charAt(i + 1);
	       	if(password.charAt(i) == nextChar) {
	            password.setCharAt(i + 1, this.getRandomChar(charCategories, random));
	       	}
	     }   
        
        return new String(password);
    }

 
    
    /**
     * This method returns a random character.
     *
     * @param List containing categories of characters and instance of the Random class.
     * @return returns a new random character.
     */
	public char getRandomChar(List<String> charCategories, Random random) {
    	String charCategory = charCategories.get(random.nextInt(charCategories.size()));
        int position = random.nextInt(charCategory.length());
        
    	return charCategory.charAt(position);
	}
    
    
}
