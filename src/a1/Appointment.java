package a1;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * A single appointment: one patient, one health professional, one time on one
 * day.
 *
 * <p>The constructor only succeeds when every required detail is present and
 * valid, so an appointment can never exist in an incomplete or invalid state
 * (Part 4 design consideration). The booking rules that need to look at other
 * appointments - such as "this professional is already busy" - live in
 * {@link AppointmentManager}, because a single appointment does not know about
 * the rest of the clinic's bookings.</p>
 */
public final class Appointment implements Comparable<Appointment> {

    private final Patient patient;
    private final HealthProfessional professional;
    private final LocalDate date;
    private final LocalTime startTime;

    /**
     * @param patient      the patient who is booking
     * @param professional any type of health professional
     * @param date         the day of the appointment; must be today
     * @param startTime    a valid clinic slot start, for example 09:30
     * @throws IllegalArgumentException when a detail is missing or invalid
     */
    public Appointment(Patient patient, HealthProfessional professional, LocalDate date, LocalTime startTime) {
        if (patient == null) {
            throw new IllegalArgumentException("An appointment needs a patient.");
        }
        if (professional == null) {
            throw new IllegalArgumentException("An appointment needs a health professional.");
        }
        if (date == null) {
            throw new IllegalArgumentException("An appointment needs a date.");
        }
        if (startTime == null) {
            throw new IllegalArgumentException("An appointment needs a start time.");
        }
        if (!date.equals(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Appointments can only be made for the current day (" + LocalDate.now() + ").");
        }
        if (!ClinicSchedule.isBookableSlot(startTime)) {
            throw new IllegalArgumentException(startTime + " is not a clinic time slot. " + ClinicSchedule.describe() + ".");
        }
        this.patient = patient;
        this.professional = professional;
        this.date = date;
        this.startTime = startTime;
    }

    public Patient getPatient() {
        return patient;
    }

    public HealthProfessional getProfessional() {
        return professional;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * @return the time this appointment ends
     */
    public LocalTime getEndTime() {
        return ClinicSchedule.getEndTime(startTime);
    }

    /**
     * Two appointments clash when the same professional is booked for the same
     * date and the same slot. Availability is decided by the professional's id,
     * so two different objects that represent the same professional still clash.
     *
     * @param other the appointment to compare with
     * @return {@code true} when both appointments use the same professional and slot
     */
    public boolean conflictsWith(Appointment other) {
        return other != null
                && professional.equals(other.professional)
                && date.equals(other.date)
                && startTime.equals(other.startTime);
    }

    /**
     * Orders appointments from the earliest to the latest. Appointments that
     * start at the same time are ordered by professional id, which keeps the
     * output of the demonstration stable.
     */
    @Override
    public int compareTo(Appointment other) {
        int byDate = date.compareTo(other.date);
        if (byDate != 0) {
            return byDate;
        }
        int byTime = startTime.compareTo(other.startTime);
        if (byTime != 0) {
            return byTime;
        }
        return professional.getId().compareTo(other.professional.getId());
    }

    /**
     * An appointment is identified by its professional and its slot, which is
     * exactly the combination the clinic must not book twice.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Appointment)) {
            return false;
        }
        Appointment that = (Appointment) other;
        return professional.equals(that.professional)
                && date.equals(that.date)
                && startTime.equals(that.startTime);
    }

    @Override
    public int hashCode() {
        int result = professional.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + startTime.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        text.append(String.format("%s-%s on %s", startTime, getEndTime(), date)).append(System.lineSeparator());
        text.append("    Patient      : ").append(patient).append(System.lineSeparator());
        text.append("    Professional : ").append(professional.getRole())
                .append(" ").append(professional.getName())
                .append(" (ID: ").append(professional.getId()).append(')');
        return text.toString();
    }
}
