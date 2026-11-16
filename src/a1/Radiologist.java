package a1;

import java.time.DayOfWeek;
import java.util.Set;

/**
 * A radiologist. The extra information this type records is the imaging
 * modality the radiologist works with.
 */
public class Radiologist extends HealthProfessional {

    private final String imagingModality;

    /**
     * @param id              numeric id, digits only
     * @param name            full name
     * @param imagingModality for example "MRI", "CT" or "X-Ray"
     */
    public Radiologist(String id, String name, String imagingModality) {
        super(id, name);
        this.imagingModality = Checks.requireText(imagingModality, "Imaging modality");
    }

    /**
     * @param id              numeric id, digits only
     * @param name            full name
     * @param imagingModality for example "MRI", "CT" or "X-Ray"
     * @param availableDays   the days this radiologist works
     */
    public Radiologist(String id, String name, String imagingModality, Set<DayOfWeek> availableDays) {
        super(id, name, availableDays);
        this.imagingModality = Checks.requireText(imagingModality, "Imaging modality");
    }

    public String getImagingModality() {
        return imagingModality;
    }

    @Override
    public String getRole() {
        return "Radiologist";
    }

    @Override
    public String performCoreService(Patient patient) {
        return "Dr " + getName() + " performs a scan using " + imagingModality
                + " for " + patient.getName() + " and reviews the images.";
    }

    @Override
    protected String describeSpecialistDetails() {
        return "    Imaging modality : " + imagingModality + System.lineSeparator();
    }
}
