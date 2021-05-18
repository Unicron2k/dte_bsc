package Helper;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.util.regex.Pattern.matches;

/**
 * <h1>Test.Helper class.</h1>
 * <br>
 * This class contains some useful method that I use often and therefore found them useful to have them defined in one place.
 *
 * @author Daniel Aaron Salwerowicz
 * @version 1.0
 * @since 2018-02-21
 */
public class Helper {
  /**
   * Regex for matching positive decimals.
   */
  public static final String POSITIVE_DECIMAL_REGEX = "([+]|[+]?\\d+(\\.|\\.\\d+)?)$";

  /**
   * Regex for matching negative decimals.
   */
  public static final String NEGATIVE_DECIMAL_REGEX = "([-]|[-]?\\d+(\\.|\\.\\d+)?)$";

  /**
   * Regex for matching positive integers.
   */
  public static final String POSITIVE_INTEGER_REGEX = "([+]|[+]?\\d+)";

  /**
   * Regex for matching negative integers.
   */
  public static final String NEGATIVE_INTEGER_REGEX = "([-]|[-]\\d+)";

  /**
   * Regex for matching decimals.
   */
  public static final String DECIMAL_REGEX = "([-+]|[-+]?\\d+(\\.|\\.\\d+)?)$";

  /**
   * Regex for matching hexadecimal strings.
   */
  public static final String HEXADECIMAL_REGEX = "[0-9A-F]+";

  /**
   * Regex for matching integers.
   */
  public static final String INTEGER_REGEX = "([-+]|[-+]?\\d+)";

  /**
   * Regex for matching hexadecimal strings.
   */
  public static final String BINARY_REGEX = "[01]+";

  /**
   * Value of 2 * pi.
   */
  public static final double PI_2 = 2 * Math.PI;

  /**
   * Value of pi.
   */
  public static final double PI = Math.PI;

  /**
   * Value of e.
   */
  public static final double E = Math.E;

  /**
   * Restrict text field input with a given regex.
   *
   * @param field Text field for which input is to be restricted.
   * @param regex Regex for allowed characters.
   */
  public static void restrictTextFieldInput(TextField field, String regex) {
    field.textProperty().addListener(
        (observable, oldValue, newValue) -> {
          if (newValue.equals(oldValue) || isNullOrEmpty(newValue)) return;
          field.setText(matches(regex, newValue) ? newValue : oldValue);
        });
  }

  /**
   * Checks whether string is null or empty.
   *
   * @param text Text to be tested.
   * @return True if null or empty, false otherwise.
   */
  public static boolean isNullOrEmpty(String text) {
    return text == null || text.isEmpty();
  }

  /**
   * Gets text from text field and clears it.
   *
   * @param field Text field from which text is taken.
   * @return Text from text field.
   */
  public static String getTextFromTextField(TextField field) {
    String text = field.getText();
    field.clear();
    return text;
  }

  /**
   * Clears text field and sets it to toString value of object.
   *
   * @param field  Text field to which text is going to be set.
   * @param object Generic object set into the text field.
   */
  public static <T> void setTextToTextField(TextField field, T object) {
    field.clear();
    field.setText(object.toString());
  }

  /**
   * Appends toString value of object to area and adds a new line.
   *
   * @param area   Text field to which text is going to be set.
   * @param object Generic object set into the text area.
   */
  public static <T> void appendTextToTextAreaWithNewline(TextArea area, T object) {
    area.appendText(object.toString() + "\n");
  }

  /**
   * Creates a string made up of character and of length defined in length.
   *
   * @param character Character that will make up string.
   * @param length Length of string.
   */
  public static String getCharString(char character, int length) {
    StringBuilder sb = new StringBuilder();

    if (length <= 0) return "";

    for (int i = 0; i < length; ++i, sb.append(character)) ;

    return sb.toString();
  }

  /**
   * Gets integer from text field and clears the text field.
   *
   * @param field Text field from which integer is to be taken.
   * @return Integer from text field.
   * @throws NumberFormatException Thrown if text field doesn't contain a valid integer.
   */
  public static int getIntegerFromTextField(TextField field) throws NumberFormatException {
    return parseInt(getTextFromTextField(field));
  }

  /**
   * Gets double from text field and clears the text field.
   *
   * @param field Text field from which double is to be taken.
   * @return Double from text field.
   * @throws NumberFormatException Thrown if text field doesn't contain a valid double.
   */
  public static double getDoubleFromTextField(TextField field) throws NumberFormatException {
    return parseDouble(getTextFromTextField(field));
  }

  /**
   * Checks if text contains a negative decimal.
   *
   * @param text Text to be checked.
   * @return true if it matched, false otherwise.
   */
  public static boolean isHexadecimalString(String text) {
    return matches(HEXADECIMAL_REGEX, text);
  }

  /**
   * Checks if text contains a negative decimal.
   *
   * @param text Text to be checked.
   * @return true if it matched, false otherwise.
   */
  public static boolean isBinaryString(String text) {
    return matches(BINARY_REGEX, text);
  }

  /**
   * Checks if text contains a positive decimal.
   *
   * @param text Text to be checked.
   * @return true if it matched, false otherwise.
   */
  public static boolean isPositiveDecimalString(String text) {
    return matches(POSITIVE_DECIMAL_REGEX, text);
  }

  /**
   * Checks if text contains a negative decimal.
   *
   * @param text Text to be checked.
   * @return true if it matched, false otherwise.
   */
  public static boolean isNegativeDecimalString(String text) {
    return matches(NEGATIVE_DECIMAL_REGEX, text);
  }

  /**
   * Checks if text contains a decimal.
   *
   * @param text Text to be checked.
   * @return true if it matched, false otherwise.
   */
  public static boolean isDecimalString(String text) {
    return matches(DECIMAL_REGEX, text);
  }

  /**
   * Checks if text contains a positive integer.
   *
   * @param text Text to be checked.
   * @return true if it matched, false otherwise.
   */
  public static boolean isPositiveIntegerString(String text) {
    return matches(POSITIVE_INTEGER_REGEX, text);
  }

  /**
   * Checks if text contains a negative integer.
   *
   * @param text Text to be checked.
   * @return true if it matched, false otherwise.
   */
  public static boolean isNegativeIntegerString(String text) {
    return matches(NEGATIVE_INTEGER_REGEX, text);
  }

  /**
   * Checks if text contains a integer.
   *
   * @param text Text to be checked.
   * @return true if it matched, false otherwise.
   */
  public static boolean isIntegerString(String text) {
    return matches(INTEGER_REGEX, text);
  }

  /**
   * Checks if text contains searchText ignoring case.
   *
   * @param text       Text to be checked.
   * @param searchText Text to check for.
   * @return true if text contains searchText, false otherwise.
   */
  public static boolean containsIgnoreCase(String text, String searchText) {
    return text != null && searchText != null && text.toLowerCase().contains(searchText.toLowerCase());
  }

  /**
   * Run to print code examples.
   *
   * @param args the input arguments.
   */
  public static void main(String[] args) {
    printCodeForDialog();
  }

  private static void printCodeForDialog() {
    System.out.println("private $OBJECT$ getOBJECT(String headerText, GridPane pane) {");
    System.out.println("  Dialog<$OBJECT$> dialog = new Dialog<>();");
    System.out.println("  ButtonType buttonTypeOk = new ButtonType(\"Done\", ButtonBar.ButtonData.OK_DONE);");
    System.out.println("");
    System.out.println("  dialog.setTitle(titleText);");
    System.out.println("  dialog.setHeaderText(headerText);");
    System.out.println("  // Pane contains necessary text fields");
    System.out.println("  dialog.getDialogPane().setContent(pane);");
    System.out.println("  dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);");
    System.out.println("  dialog.setResultConverter(b -> {");
    System.out.println("     // Get input from text fields");
    System.out.println("");
    System.out.println("     return $OBJECT$( $INPUT$ );");
    System.out.println("   });");
    System.out.println("");
    System.out.println("  Optional<$OBJECT$> result = dialog.showAndWait();");
    System.out.println("  return result.orElse(null);");
    System.out.println("}");
  }
}
