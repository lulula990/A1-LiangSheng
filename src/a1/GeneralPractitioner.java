package a1;

import java.time.DayOfWeek;
import java.util.Set;

/**
 * A general practitioner: the one type of health professional that every
 * clinic in this assignment must model.
 */
public class GeneralPractitioner extends HealthProfessional {

    private final String consultationRoom;

    /**
     * @param id               numeric id, digits only
     * @param name             full name
     * @param consultationRoom the room this GP normally works in
     */
    public GeneralPractitioner(String id, String name, String consultationRoom) {
        super(id, name);
        this.consultationRoom = Checks.requireText(consultationRoom, "Consultation room");
    }

    /**
     * @param id               numeric id, digits only
     * @param name             full name
     * @param consultationRoom the room this GP normally works in
     * @param availableDays    the days this GP works
     */
    public GeneralPractitioner(String id, String name, String consultationRoom, Set<DayOfWeek> availableDays) {
        super(id, name, availableDays);
        this.consultationRoom = Checks.requireText(consultationRoom, "Consultation room");
    }

    public String getConsultationRoom() {
        return consultationRoom;
    }

    @Override
    public String getRole() {
        return "General Practitioner";
    }

    @Override
    public String performCoreService(Patient patient) {
        return "Dr " + getName() + " consults " + patient.getName()
                + " in " + consultationRoom + " and writes a prescription.";
    }

    @Override
    protected String describeSpecialistDetails() {
        return "    Consultation room : " + consultationRoom + System.lineSeparator();
    }
}
