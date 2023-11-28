package project.util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {
    private Pattern pattern;
    private Pattern pattern1;
    private Pattern pattern2;
    private Pattern pattern3;
    private Matcher matcher;

    private static final String EMAIL_PATTERN = 
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


    private static final String DATE_PATTERN = "(0[1-9]|1[012])[- /.](19|20)\\d\\d$";

    private static final String CARD_NUMBER_PATTERN = "[0-9]{16}";

    private static final String SECURITY_CODE_PATTERN = "[0-9]{3}";

    public InputValidator() {
        pattern = Pattern.compile(EMAIL_PATTERN);
        pattern1 = Pattern.compile(DATE_PATTERN);
        pattern2 = Pattern.compile(CARD_NUMBER_PATTERN);
        pattern3 = Pattern.compile(SECURITY_CODE_PATTERN);
    }

    public boolean validateEmail(final String hex) {

        matcher = pattern.matcher(hex);
        return matcher.matches();

    }

    public boolean validateDate(final String hex) {

        matcher = pattern1.matcher(hex);
        return matcher.matches();

    }

    public boolean validateCardNumber (final String cardNumber){
        
        matcher = pattern2.matcher(cardNumber);
        return matcher.matches();

    }

    public boolean validateSecurityCode (final String securityCode){
        
        matcher = pattern3.matcher(securityCode);
        return matcher.matches();

    }

    public boolean validatePassword(char[] password){
        if(password.length >= 8){
            return checkCapital(password) && checkDigit(password);
        } else {
            return false;
        }
        
    }

    public static boolean checkCapital(char[] password) {
        for (char c : password) {
            int code = (int) c;
            if (code >= 65 && code <= 90) 
                return true;
        }
        return false;
    }

    public static boolean checkDigit(char[] password) {
        for (char c : password) {
            if (Character.isDigit(c)){
                return true;
            }
        }
        return false;
    }


}
