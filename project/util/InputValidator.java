package project.util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {
    private Pattern pattern;
    private Matcher matcher;

    private static final String EMAIL_PATTERN = 
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public InputValidator() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    public boolean validate(final String hex) {

        matcher = pattern.matcher(hex);
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
