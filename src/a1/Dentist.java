package a1;

import java.time.DayOfWeek;
import java.util.Set;

/**
 * A dentist. The extra information this type records is the branch of dentistry
 * the dentist specialises in.
 */
public class Dentist extends HealthProfessional {

    private final String dentalSpecialty;

    /**
     * @param id             numeric id, digits only
     * @param name           full name
     * @param dentalSpecialty for example "Orthodontics" or "Paediatric Dentistry"
     */
    public Dentist(String id, String name, String dentalSpecialty) {
        super(id, name);
        this.dentalSpecialty = Checks.requireText(dentalSpecialty, "Dental specialty");
    }

    /**
     * @param id              numeric id, digits only
     * @param name            full name
     * @param dentalSpecialty for example "Orthodontics" or "Paediatric Dentistry"
     * @param availableDays   the days this dentist works
     */
    public Dentist(String id, String name, String dentalSpecialty, Set<DayOfWeek> availableDays) {
        super(id, name, availableDays);
        this.dentalSpecialty = Checks.requireText(dentalSpecialty, "Dental specialty");
    }

    public String getDentalSpecialty() {
        return dentalSpecialty;
    }

    @Override
    public String getRole() {
        return "Dentist";
    }

    @Override
    public String performCoreService(Patient patient) {
        return "Dr " + getName() + " performs a check-up and clean in " + dentalSpecialty
                + " for " + patient.getName() + ".";
    }

    @Override
    protected String describeSpecialistDetails() {
        return "    Dental specialty : " + dentalSpecialty + System.lineSeparator();
    }
}
