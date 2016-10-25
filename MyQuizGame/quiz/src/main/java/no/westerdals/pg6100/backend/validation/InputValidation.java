package no.westerdals.pg6100.backend.validation;

public class InputValidation {

    public static boolean validInput(String... input) {
        for (String s : input) {
            if (s == null || s.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static String formatInput(String input) {
        return input.toLowerCase();
    }
}
