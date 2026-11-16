package a1;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The fixed daily schedule of the clinic.
 *
 * <p>All of the schedule knowledge lives in this one class: the opening and
 * closing times, the slot length and the list of bookable start times. If the
 * clinic changes its hours, only the three constants below have to be edited,
 * and the rest of the appointment system keeps working (Part 5 design
 * consideration: "so that there is enough flexibility to change it").</p>
 */
public final class ClinicSchedule {

    /** The clinic opens at 08:00. */
    public static final LocalTime OPENING_TIME = LocalTime.of(8, 0);

    /** The clinic closes at 16:00, so the last appointment starts at 15:30. */
    public static final LocalTime CLOSING_TIME = LocalTime.of(16, 0);

    /** Each appointment occupies a 30 minute slot. */
    public static final int SLOT_MINUTES = 30;

    private static final List<LocalTime> DAILY_SLOTS = buildDailySlots();

    private ClinicSchedule() {
        // utility class - no instances
    }

    private static List<LocalTime> buildDailySlots() {
        List<LocalTime> slots = new ArrayList<>();
        LocalTime start = OPENING_TIME;
        while (start.isBefore(CLOSING_TIME)) {
            slots.add(start);
            start = start.plusMinutes(SLOT_MINUTES);
        }
        return Collections.unmodifiableList(slots);
    }

    /**
     * @return every bookable start time of a day, from 08:00 to 15:30
     */
    public static List<LocalTime> getDailySlots() {
        return DAILY_SLOTS;
    }

    /**
     * Checks that a time really is one of the clinic's slots, which prevents an
     * appointment such as 09:07 from ever being created.
     *
     * @param startTime the time to check
     * @return {@code true} when the time is a valid slot start
     */
    public static boolean isBookableSlot(LocalTime startTime) {
        return startTime != null && DAILY_SLOTS.contains(startTime);
    }

    /**
     * @param startTime the start of a slot
     * @return the time the slot ends
     */
    public static LocalTime getEndTime(LocalTime startTime) {
        return startTime.plusMinutes(SLOT_MINUTES);
    }

    /**
     * @return a short description of the schedule, used in messages
     */
    public static String describe() {
        return "08:00-16:00 in " + SLOT_MINUTES + " minute slots (last booking 15:30)";
    }
}
