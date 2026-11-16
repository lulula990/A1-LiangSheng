package a1;

/**
 * Small package-private helper that centralises the argument checks used by
 * the model classes.
 *
 * <p>Keeping the validation in one place avoids repeating the same
 * null/blank/format checks in every constructor, which is one of the design
 * considerations of Part 3 ("avoid code repetition").</p>
 */
final class Checks {

    private Checks() {
        // utility class - no instances
    }

    /**
     * Returns the trimmed text, or throws if the value is null or blank.
     *
     * @param value     the value supplied by the caller
     * @param fieldName the name used in the error message
     * @return the trimmed, non-empty value
     */
    static String requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be empty.");
        }
        return value.trim();
    }

    /**
     * Returns the trimmed text if it consists of digits only, otherwise throws.
     *
     * @param value     the value supplied by the caller
     * @param fieldName the name used in the error message
     * @return the trimmed, digit-only value
     */
    static String requireDigits(String value, String fieldName) {
        String trimmed = requireText(value, fieldName);
        for (int i = 0; i < trimmed.length(); i++) {
            if (!Character.isDigit(trimmed.charAt(i))) {
                throw new IllegalArgumentException(fieldName + " must contain digits only, but was \"" + trimmed + "\".");
            }
        }
        return trimmed;
    }
}
