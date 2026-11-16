package a1;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;

/**
 * Part 1 and Part 2 - the generic health professional.
 *
 * <p>This class owns everything that every health professional has in common:
 * a numeric id, a name and the fixed set of days the professional works. It is
 * {@code abstract} for two reasons:</p>
 *
 * <ul>
 *   <li>a "plain" health professional with no specialty does not exist in the
 *       clinic, so allowing {@code new HealthProfessional(...)} would let an
 *       invalid object be created;</li>
 *   <li>it forces every concrete type to provide its own role, its own extra
 *       details and its own behaviour (Part 2 design consideration).</li>
 * </ul>
 *
 * <p>Encapsulation: every field is {@code private final}, so an object cannot
 * be changed, and therefore cannot be put into an invalid state, after it has
 * been constructed. Callers can read the state through getters, but the only
 * way to create an object is a constructor that validates its arguments.</p>
 */
public abstract class HealthProfessional {

    /** Roster used when the caller does not supply one (Part 1 design consideration). */
    private static final Set<DayOfWeek> DEFAULT_AVAILABLE_DAYS =
            Collections.unmodifiableSet(EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));

    private final String id;
    private final String name;
    private final Set<DayOfWeek> availableDays;

    /**
     * Creates a professional who works the default roster (Monday, Wednesday,
     * Friday). This overload exists so that a caller who does not care about
     * the roster does not have to supply one.
     *
     * @param id   numeric identifier, digits only
     * @param name full name
     */
    protected HealthProfessional(String id, String name) {
        this(id, name, DEFAULT_AVAILABLE_DAYS);
    }

    /**
     * Creates a professional with an explicit roster.
     *
     * @param id            numeric identifier, digits only
     * @param name          full name
     * @param availableDays the days this professional works; must not be empty
     */
    protected HealthProfessional(String id, String name, Set<DayOfWeek> availableDays) {
        this.id = Checks.requireDigits(id, "Professional id");
        this.name = Checks.requireText(name, "Professional name");
        this.availableDays = copyOf(availableDays);
    }

    private static Set<DayOfWeek> copyOf(Set<DayOfWeek> days) {
        if (days == null || days.isEmpty()) {
            throw new IllegalArgumentException("A health professional must be available on at least one day.");
        }
        // Defensive copy: later changes to the caller's set cannot affect this object.
        return Collections.unmodifiableSet(EnumSet.copyOf(days));
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /**
     * Returns the roster as an unmodifiable set, so external code can read it
     * but can never change when the professional works.
     *
     * @return the days this professional works
     */
    public Set<DayOfWeek> getAvailableDays() {
        return availableDays;
    }

    /**
     * @param day the day to check
     * @return {@code true} when this professional works on that day
     */
    public boolean isAvailableOn(DayOfWeek day) {
        return day != null && availableDays.contains(day);
    }

    /**
     * @return the specific type of professional, for example "General Practitioner"
     */
    public abstract String getRole();

    /**
     * Performs the core service this type of professional provides. Each subtype
     * answers differently, which is what makes a collection of
     * {@code HealthProfessional} references behave polymorphically.
     *
     * @param patient the patient being treated
     * @return a readable description of what was done
     */
    public abstract String performCoreService(Patient patient);

    /**
     * Supplies the type-specific lines shown by {@link #describe()}.
     *
     * @return one or more formatted detail lines, each ending with a line break
     */
    protected abstract String describeSpecialistDetails();

    /**
     * Builds the full, readable description of this professional. The method is
     * {@code final} so that no subtype can break the shared format; subtypes only
     * contribute their own extra lines through
     * {@link #describeSpecialistDetails()}.
     *
     * @return the description, with a trailing line break
     */
    public final String describe() {
        StringBuilder text = new StringBuilder();
        text.append('[').append(getRole()).append("] ").append(name)
                .append(" (ID: ").append(id).append(')').append(System.lineSeparator());
        text.append("    Available days  : ").append(formatAvailableDays()).append(System.lineSeparator());
        text.append(describeSpecialistDetails());
        return text.toString();
    }

    private String formatAvailableDays() {
        StringBuilder text = new StringBuilder();
        for (DayOfWeek day : DayOfWeek.values()) {
            if (availableDays.contains(day)) {
                if (text.length() > 0) {
                    text.append(", ");
                }
                text.append(day.getDisplayName(TextStyle.FULL, Locale.ENGLISH));
            }
        }
        return text.toString();
    }

    /**
     * Printing a professional (for example with
     * {@code System.out.println(doctor)}) shows all of its details in a readable form.
     */
    @Override
    public final String toString() {
        return describe();
    }

    /**
     * Two professionals are the same person when they have the same id. This is
     * what lets appointment management detect a double booking even when it is
     * given a different object representing the same professional.
     */
    @Override
    public final boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HealthProfessional)) {
            return false;
        }
        HealthProfessional that = (HealthProfessional) other;
        return id.equals(that.id);
    }

    @Override
    public final int hashCode() {
        return id.hashCode();
    }
}
