package dashboard.apps.helper;

public class StringHelper {

    public static String padLeft(String stringToPad, String paddingString, int padLength) {
        StringBuilder output = new StringBuilder(stringToPad);
        while (output.length() < padLength) {
            output.insert(0, paddingString);
        }
        return output.toString();
    }

    public static String padRight(String stringToPad, String paddingString, int padLength) {
        StringBuilder output = new StringBuilder(stringToPad);
        while (output.length() < padLength) {
            output.append(paddingString);
        }
        return output.toString();
    }
}
