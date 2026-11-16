package a1;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Assessment 1 - Object Oriented Programming (PROG2004).
 *
 * <p>This class is the entry point of the demonstration. It does not contain
 * any of the system's rules: it creates the objects, asks them to do things and
 * prints what happened. Part 3 is demonstrated by building a team of health
 * professionals and treating them through their common type; Parts 4 to 6 are
 * demonstrated by booking, filtering, sorting and cancelling appointments.</p>
 */
public class AssignmentOne {

    private static final String LINE =
            "----------------------------------------------------------------------";

    public static void main(String[] args) {
        printHeader();

        List<HealthProfessional> team = createTeam();

        printSection("PART 3 - Working with health professional objects");
        listTeam(team);
        showPolymorphicBehaviour(team);

        printSection("PARTS 4 to 6 - Appointment system");
        demonstrateAppointmentSystem(team);

        System.out.println();
        System.out.println(LINE);
        System.out.println("End of demonstration.");
        System.out.println(LINE);
    }

    /**
     * Creates the clinic's team: three general practitioners plus one or two of
     * each of the other types designed in Part 2.
     *
     * @return the team, held through the common supertype
     */
    private static List<HealthProfessional> createTeam() {
        Set<DayOfWeek> weekdays = EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.FRIDAY);
        List<HealthProfessional> team = new ArrayList<>();

        // Three general practitioners; the first one keeps the default Mon/Wed/Fri roster.
        team.add(new GeneralPractitioner("1001", "Alice Nguyen", "Room 3"));
        team.add(new GeneralPractitioner("1002", "Ben Carter", "Room 4", weekdays));
        team.add(new GeneralPractitioner("1003", "Chloe Singh", "Room 5", weekdays));

        // Two dentists.
        team.add(new Dentist("2001", "David Park", "Orthodontics"));
        team.add(new Dentist("2002", "Grace Liu", "Paediatric Dentistry", weekdays));

        // Two nurses, one of whom is trained to take blood samples.
        team.add(new Nurse("3001", "Emma Wilson", "Ward 3A", true));
        team.add(new Nurse("3002", "Jack Taylor", "Ward 3B", false, weekdays));

        // Two radiologists.
        team.add(new Radiologist("4001", "Farid Hassan", "MRI"));
        team.add(new Radiologist("4002", "Ivy Chen", "CT", weekdays));

        return team;
    }

    private static void listTeam(List<HealthProfessional> team) {
        System.out.println("The clinic employs " + team.size() + " health professionals:");
        System.out.println();
        for (HealthProfessional professional : team) {
            System.out.println(professional);
        }
        System.out.println("Team by role:");
        for (String role : new String[]{"General Practitioner", "Dentist", "Nurse", "Radiologist"}) {
            System.out.println("  " + role + ": " + countByRole(team, role));
        }
    }

    private static int countByRole(List<HealthProfessional> team, String role) {
        int count = 0;
        for (HealthProfessional professional : team) {
            if (professional.getRole().equals(role)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Treats every professional through the same {@code HealthProfessional}
     * reference. The object decides which behaviour runs, which is polymorphism.
     *
     * @param team the clinic's team
     */
    private static void showPolymorphicBehaviour(List<HealthProfessional> team) {
        Patient patient = new Patient("Michael Chen", "0412345678");
        System.out.println();
        System.out.println("The same request is sent to every professional; each one answers in its own way:");
        for (HealthProfessional professional : team) {
            System.out.println("  - " + professional.performCoreService(patient));
        }
        System.out.println();
    }

    private static void demonstrateAppointmentSystem(List<HealthProfessional> team) {
        LocalDate today = LocalDate.now();
        AppointmentManager manager = new AppointmentManager();

        Patient michael = new Patient("Michael Chen", "0412345678");
        Patient sarah = new Patient("Sarah Lee", "0498765432");
        Patient tom = new Patient("Tom Brown", "0455123456");

        System.out.println("Today is " + today + " ("
                + today.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + ").");
        System.out.println("The clinic runs " + ClinicSchedule.describe() + ".");
        System.out.println();

        System.out.println("1. Booking appointments across the different professional types");
        book(manager, michael, findProfessional(team, "1002"), today, LocalTime.of(9, 0));
        book(manager, sarah, findProfessional(team, "2001"), today, LocalTime.of(9, 30));
        book(manager, tom, findProfessional(team, "3001"), today, LocalTime.of(10, 0));
        book(manager, michael, findProfessional(team, "4001"), today, LocalTime.of(10, 30));
        book(manager, sarah, findProfessional(team, "1002"), today, LocalTime.of(11, 0));
        System.out.println();

        System.out.println("2. Error cases: a double booking, an invalid slot and an invalid day");
        book(manager, tom, findProfessional(team, "1002"), today, LocalTime.of(9, 0));
        book(manager, michael, findProfessional(team, "1003"), today, LocalTime.of(9, 15));
        book(manager, michael, findProfessional(team, "1003"), today.plusDays(1), LocalTime.of(9, 0));
        try {
            new Patient("Unlucky Person", "12345");
        } catch (IllegalArgumentException problem) {
            System.out.println("  WARN  Booking rejected: " + problem.getMessage());
        }
        System.out.println();

        System.out.println("3. All appointments, sorted from earliest to latest");
        printAppointments(manager.getAppointmentsInTimeOrder());
        System.out.println();

        System.out.println("4. Filtered by health professional ID 1002, then sorted");
        printAppointments(manager.getAppointmentsInTimeOrder(
                manager.getAppointmentsForProfessional("1002")));
        System.out.println();

        System.out.println("5. Filtered by patient mobile 0498765432, then sorted");
        printAppointments(manager.getAppointmentsInTimeOrder(
                manager.getAppointmentsForPatient("0498765432")));
        System.out.println();

        System.out.println("6. Cancelling appointments");
        System.out.println("  " + manager.cancelAppointment("2001", LocalTime.of(9, 30)));
        System.out.println("  " + manager.cancelAppointment("1002", LocalTime.of(15, 30)));
        System.out.println("  " + manager.cancelAppointmentsForPatient("0498765432"));
        System.out.println();

        System.out.println("7. The collection after the cancellations ("
                + manager.getAppointmentCount() + " appointments left)");
        printAppointments(manager.getAppointmentsInTimeOrder());
        System.out.println();

        System.out.println("8. Confirming a cancelled slot can be booked again");
        book(manager, tom, findProfessional(team, "2001"), today, LocalTime.of(9, 30));
    }

    /**
     * Creates an appointment and adds it to the manager, reporting either the
     * manager's message or the validation message of the appointment itself.
     */
    private static void book(AppointmentManager manager, Patient patient,
                             HealthProfessional professional, LocalDate date, LocalTime startTime) {
        try {
            Appointment appointment = new Appointment(patient, professional, date, startTime);
            System.out.println("  " + manager.addAppointment(appointment));
        } catch (IllegalArgumentException problem) {
            System.out.println("  WARN  Booking rejected: " + problem.getMessage());
        }
    }

    private static void printAppointments(List<Appointment> appointments) {
        if (appointments.isEmpty()) {
            System.out.println("  (no appointments)");
            return;
        }
        for (Appointment appointment : appointments) {
            System.out.println(appointment);
            System.out.println();
        }
    }

    /**
     * Finds a professional by id. Used by the demonstration so that the booking
     * calls stay readable and do not depend on list positions.
     */
    private static HealthProfessional findProfessional(List<HealthProfessional> team, String id) {
        for (HealthProfessional professional : team) {
            if (professional.getId().equals(id)) {
                return professional;
            }
        }
        throw new IllegalArgumentException("No health professional with id " + id);
    }

    private static void printHeader() {
        System.out.println(LINE);
        System.out.println("PROG2004 Object Oriented Programming - Assessment 1");
        System.out.println("Simple appointment system for a health service");
        System.out.println(LINE);
        System.out.println();
    }

    private static void printSection(String title) {
        System.out.println(LINE);
        System.out.println(title);
        System.out.println(LINE);
    }
}
