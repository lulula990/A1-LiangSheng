package a1;

import java.time.DayOfWeek;
import java.util.Set;

/**
 * A nurse. A nurse records the ward they work on and whether they are trained
 * to take blood samples, which changes the service they can provide.
 */
public class Nurse extends HealthProfessional {

    private final String ward;
    private final boolean canTakeBloodSamples;

    /**
     * @param id                   numeric id, digits only
     * @param name                 full name
     * @param ward                 the ward this nurse works on
     * @param canTakeBloodSamples  whether this nurse is trained to take blood samples
     */
    public Nurse(String id, String name, String ward, boolean canTakeBloodSamples) {
        super(id, name);
        this.ward = Checks.requireText(ward, "Ward");
        this.canTakeBloodSamples = canTakeBloodSamples;
    }

    /**
     * @param id                   numeric id, digits only
     * @param name                 full name
     * @param ward                 the ward this nurse works on
     * @param canTakeBloodSamples  whether this nurse is trained to take blood samples
     * @param availableDays        the days this nurse works
     */
    public Nurse(String id, String name, String ward, boolean canTakeBloodSamples, Set<DayOfWeek> availableDays) {
        super(id, name, availableDays);
        this.ward = Checks.requireText(ward, "Ward");
        this.canTakeBloodSamples = canTakeBloodSamples;
    }

    public String getWard() {
        return ward;
    }

    public boolean canTakeBloodSamples() {
        return canTakeBloodSamples;
    }

    @Override
    public String getRole() {
        return "Nurse";
    }

    @Override
    public String performCoreService(Patient patient) {
        if (canTakeBloodSamples) {
            return "Nurse " + getName() + " takes a blood sample from "
                    + patient.getName() + " on " + ward + ".";
        }
        return "Nurse " + getName() + " checks " + patient.getName()
                + "'s observations on " + ward + ".";
    }

    @Override
    protected String describeSpecialistDetails() {
        StringBuilder text = new StringBuilder();
        text.append("    Ward             : ").append(ward).append(System.lineSeparator());
        text.append("    Blood samples    : ")
                .append(canTakeBloodSamples ? "trained" : "not trained")
                .append(System.lineSeparator());
        return text.toString();
    }
}
