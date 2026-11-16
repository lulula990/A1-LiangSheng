package a1;

/**
 * A patient. Part 4 asks whether a patient should be its own class; it should,
 * because a patient exists independently of any appointment and the same
 * patient can book more than once.
 */
public final class Patient {

    /** Australian mobile number: 10 digits starting with 04. */
    private static final String MOBILE_PATTERN = "^04\\d{8}$";

    private final String name;
    private final String mobile;

    /**
     * @param name   the patient's full name
     * @param mobile the patient's mobile number, for example 0412345678
     */
    public Patient(String name, String mobile) {
        this.name = Checks.requireText(name, "Patient name");
        if (mobile == null || !mobile.trim().matches(MOBILE_PATTERN)) {
            throw new IllegalArgumentException(
                    "Patient mobile must be a 10 digit number starting with 04, but was \"" + mobile + "\".");
        }
        this.mobile = mobile.trim();
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    /**
     * Two patient objects are the same patient when they share a mobile number.
     * This is what makes "find the appointments for this phone number" work even
     * when the caller supplies a newly created {@code Patient} object.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Patient)) {
            return false;
        }
        Patient that = (Patient) other;
        return mobile.equals(that.mobile);
    }

    @Override
    public int hashCode() {
        return mobile.hashCode();
    }

    @Override
    public String toString() {
        return name + " (" + mobile + ")";
    }
}
