package a1;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Part 5 - manages the clinic's appointment collection.
 *
 * <p>A {@code List<Appointment>} is used because the clinic needs to keep the
 * bookings in the order they were added, needs to walk through all of them, and
 * needs to sort them by time. The list stays private: every method that returns
 * appointments returns a new copy, so no caller can modify the collection
 * behind the manager's back.</p>
 */
public class AppointmentManager {

    private final List<Appointment> appointments = new ArrayList<>();

    /**
     * Adds an appointment, unless the same professional is already booked for
     * that slot.
     *
     * @param appointment the appointment to add
     * @return the outcome, including a warning when the booking is refused
     */
    public BookingResult addAppointment(Appointment appointment) {
        if (appointment == null) {
            return BookingResult.warning("Booking rejected: no appointment details were supplied.");
        }
        for (Appointment existing : appointments) {
            if (existing.conflictsWith(appointment)) {
                return BookingResult.warning("Booking rejected: " + appointment.getProfessional().getName()
                        + " already has an appointment at " + appointment.getStartTime()
                        + " on " + appointment.getDate() + ".");
            }
        }
        appointments.add(appointment);
        return BookingResult.success("Booked " + appointment.getPatient().getName() + " with "
                + appointment.getProfessional().getRole() + " " + appointment.getProfessional().getName()
                + " at " + appointment.getStartTime() + " on " + appointment.getDate() + ".");
    }

    /**
     * Cancels the appointment of a professional at a given time.
     *
     * @param professionalId the id of the professional
     * @param startTime      the slot start time
     * @return the outcome, including a warning when no such appointment exists
     */
    public BookingResult cancelAppointment(String professionalId, LocalTime startTime) {
        if (professionalId == null || professionalId.trim().isEmpty() || startTime == null) {
            return BookingResult.warning("Cancellation rejected: a professional id and a time are required.");
        }
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            if (appointment.getProfessional().getId().equals(professionalId.trim())
                    && appointment.getStartTime().equals(startTime)) {
                appointments.remove(i);
                return BookingResult.success("Cancelled " + appointment.getPatient().getName()
                        + "'s appointment with " + appointment.getProfessional().getName()
                        + " at " + startTime + ".");
            }
        }
        return BookingResult.warning("Cancellation rejected: no appointment found for professional "
                + professionalId.trim() + " at " + startTime + ".");
    }

    /**
     * Cancels every appointment booked with a particular mobile number.
     *
     * @param mobile the patient's mobile number
     * @return the outcome, including a warning when nothing was found
     */
    public BookingResult cancelAppointmentsForPatient(String mobile) {
        if (mobile == null || mobile.trim().isEmpty()) {
            return BookingResult.warning("Cancellation rejected: a patient mobile number is required.");
        }
        String wanted = mobile.trim();
        int cancelled = 0;
        for (int i = appointments.size() - 1; i >= 0; i--) {
            if (appointments.get(i).getPatient().getMobile().equals(wanted)) {
                appointments.remove(i);
                cancelled++;
            }
        }
        if (cancelled == 0) {
            return BookingResult.warning("Cancellation rejected: no appointment found for mobile " + wanted + ".");
        }
        return BookingResult.success("Cancelled " + cancelled + " appointment(s) for mobile " + wanted + ".");
    }

    /**
     * @return every appointment, in the order they were booked
     */
    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }

    /**
     * @param professionalId the id of a health professional
     * @return the appointments for that professional, in booking order
     */
    public List<Appointment> getAppointmentsForProfessional(String professionalId) {
        List<Appointment> matches = new ArrayList<>();
        if (professionalId == null) {
            return matches;
        }
        for (Appointment appointment : appointments) {
            if (appointment.getProfessional().getId().equals(professionalId.trim())) {
                matches.add(appointment);
            }
        }
        return matches;
    }

    /**
     * @param mobile a patient's mobile number
     * @return the appointments for that patient, in booking order
     */
    public List<Appointment> getAppointmentsForPatient(String mobile) {
        List<Appointment> matches = new ArrayList<>();
        if (mobile == null) {
            return matches;
        }
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().getMobile().equals(mobile.trim())) {
                matches.add(appointment);
            }
        }
        return matches;
    }

    /**
     * @return every appointment, earliest first
     */
    public List<Appointment> getAppointmentsInTimeOrder() {
        return getAppointmentsInTimeOrder(appointments);
    }

    /**
     * Sorts any collection of appointments, so the same ordering can be applied
     * to the whole list or to a filtered subset.
     *
     * @param source the appointments to sort
     * @return a new list, earliest first
     */
    public List<Appointment> getAppointmentsInTimeOrder(List<Appointment> source) {
        List<Appointment> sorted = new ArrayList<>(source);
        Collections.sort(sorted);
        return sorted;
    }

    /**
     * @param professionalId the id of a health professional
     * @param startTime      a slot start time
     * @return {@code true} when that professional is already booked at that time
     */
    public boolean isBooked(String professionalId, LocalTime startTime) {
        if (professionalId == null || startTime == null) {
            return false;
        }
        for (Appointment appointment : appointments) {
            if (appointment.getProfessional().getId().equals(professionalId.trim())
                    && appointment.getStartTime().equals(startTime)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return the number of appointments currently held
     */
    public int getAppointmentCount() {
        return appointments.size();
    }
}
