# A1 — Simple Appointment System for a Health Service

**Unit:** PROG2004 Object Oriented Programming
**Assessment:** 1 — Portfolio/programming (40%, individual)
**Author:** LiangSheng
**Language / level:** Java 17+ (no external libraries)

A class-level model of a small health-service booking system. There is deliberately no
UI, no menu, no login and no data storage: the deliverable is a set of well-designed
classes plus a `main` method that creates objects, exercises their behaviour and prints
the results to the console.

---

## 1. How to build and run

### From a terminal (Windows / macOS / Linux)

```bash
cd A1-LiangSheng
javac -d out src/a1/*.java
java -cp out a1.AssignmentOne
```

`src/a1/*.java` compiles every source file; the compiled classes go into `out/` and
`a1.AssignmentOne` is the entry point. Requires a JDK (17 or newer).

### From an IDE (IntelliJ IDEA / Eclipse / VS Code)

1. Open the `A1-LiangSheng` folder as a project.
2. Mark `src` as the source root.
3. Run `a1.AssignmentOne` (the class containing `main`).

---

## 2. Project structure

```
A1-LiangSheng/
├── src/a1/
│   ├── AssignmentOne.java          entry point + demonstration (Parts 3 and 6)
│   ├── HealthProfessional.java     abstract generic professional (Parts 1 and 2)
│   ├── GeneralPractitioner.java    concrete type (Part 2)
│   ├── Dentist.java                concrete type (Part 2)
│   ├── Nurse.java                  concrete type (Part 2)
│   ├── Radiologist.java            concrete type (Part 2)
│   ├── Patient.java                patient details (Part 4)
│   ├── ClinicSchedule.java         fixed daily slots 08:00–16:00 / 30 min (Part 5)
│   ├── Appointment.java            one booking, validated and comparable (Part 4)
│   ├── AppointmentManager.java     the appointment collection (Part 5)
│   ├── BookingResult.java          success/warning message returned by operations
│   └── Checks.java                 shared argument validation (avoids repetition)
├── README.md
└── .gitignore
```

---

## 3. Design decisions

### Part 1 — the generic health professional
`HealthProfessional` stores a numeric `id` (validated as digits only), a `name`, and a
fixed set of `availableDays`. All three fields are `private final`, so an object cannot
be changed after construction and can never be left in an invalid state. The roster is
copied into an `EnumSet` and handed out as an unmodifiable set, so external code can
read it but cannot change when a professional works.

A second constructor supplies the default roster (Monday, Wednesday, Friday) so a caller
who does not care about the roster does not have to provide one.

`describe()` builds the full readable output and `toString()` delegates to it, so
`System.out.println(doctor)` shows every detail of the object.

### Part 2 — specialising the professional
`HealthProfessional` is `abstract`, which stops the clinic from creating a "plain"
professional that has no specialty, and forces every subtype to implement
`getRole()`, `performCoreService(Patient)` and `describeSpecialistDetails()`.
`describe()`, `toString()`, `equals()` and `hashCode()` are `final`, so a subtype cannot
break the shared format or the identity rule.

Four concrete types are implemented, all sharing the Part 1 state through inheritance:

| Type | Extra state | Behaviour it adds |
|---|---|---|
| `GeneralPractitioner` | consultation room | consults and writes a prescription |
| `Dentist` | dental specialty | specialty check-up and clean |
| `Nurse` | ward, blood-sample training | takes blood or checks observations, depending on training |
| `Radiologist` | imaging modality | performs and reviews a scan |

Subtype constructors call `super(...)` first, so the shared state is always set up before
the subtype's own state — the shared details can never be forgotten.

### Part 3 — working with the objects
`main` creates a `List<HealthProfessional>` holding three GPs and one or two of each other
type, prints each one, counts them by role, and then sends the same
`performCoreService(patient)` request to every element. The object decides which version
runs, so adding a fifth professional type later requires no change to this loop.

### Part 4 — the appointment
`Appointment` holds a `Patient`, a `HealthProfessional`, a date and a slot start time.
The constructor rejects nulls, refuses any date other than today, and refuses any time
that is not one of the clinic's slots. `Patient` is its own class because a patient
exists independently of a booking and can book more than once.

`Appointment` implements `Comparable<Appointment>`, ordering by date, then start time,
then professional id. Equal start times for different professionals are therefore shown
in a stable order.

`equals()`/`hashCode()` use professional + date + slot, which is exactly the combination
the clinic must never book twice.

### Part 5 — managing the collection
`AppointmentManager` keeps a private `List<Appointment>`: booking order is preserved, the
list can be walked and it can be sorted. Every read method returns a new list, so no
caller can modify the manager's collection.

- **Add** — checks every existing appointment for a clash on the same professional and
  slot; refuses the booking with a warning message when one is found.
- **Filter** — by professional id or by patient mobile number.
- **Sort** — `getAppointmentsInTimeOrder()` and `getAppointmentsInTimeOrder(subset)`, so
  the same ordering applies to the whole list or to a filtered subset.
- **Cancel** — by professional id + time, or by patient mobile number (cancels all of that
  patient's bookings); a warning is returned when nothing matches.
- **Feedback** — every operation returns a `BookingResult` (success flag + message)
  instead of printing, so the manager contains no output code.

Clinic hours and slot length live in `ClinicSchedule` only, so changing the schedule
means editing three constants.

### Part 6 — the demonstration
`main` books several appointments across the different types, shows all / filtered /
sorted views, deliberately triggers a double booking, an invalid slot and an invalid day,
cancels by professional + time and by mobile number, shows the reduced collection and
finally re-books a cancelled slot.

---

## 4. Where the four OOP pillars appear

| Pillar | Where |
|---|---|
| **Encapsulation** | private final fields + validating constructors in `HealthProfessional`, `Patient`, `Appointment`; unmodifiable roster; `AppointmentManager` returns copies |
| **Inheritance** | `GeneralPractitioner`, `Dentist`, `Nurse`, `Radiologist` extend `HealthProfessional` and reuse its state and `describe()` |
| **Polymorphism** | `performCoreService(Patient)` overridden by every type, called through `HealthProfessional` references in `main`; `getRole()` in messages; `Comparable` ordering |
| **Abstraction** | `HealthProfessional` is abstract with abstract methods, so callers depend on the general contract, not on concrete types |

---

## 5. Assumptions and scope

- Appointments are only made for **today**, as required by the brief; the demonstration
  therefore always books on `LocalDate.now()`.
- The clinic schedule is fixed at 08:00–16:00 in 30-minute slots, last booking 15:30.
- Availability days are recorded and displayed, but a booking is not blocked on a day the
  professional does not normally work: the brief only asks for the roster to be stored, and
  enforcing it would make the demonstration depend on the day it is run. The check is a
  one-line addition in `AppointmentManager.addAppointment` if required.
- A patient is identified by mobile number; a professional is identified by id.
- `BookingResult` could be replaced by exceptions, but a result object keeps the manager
  free of console output and makes both success and failure easy to demonstrate.

---

## 6. Submission checklist

- [ ] `A1-LiangSheng.zip` containing this project (all source files)
- [ ] GitHub repository link in the MySCU comments (tutor and unit assessor invited if private)
- [ ] `A1-LiangSheng-WakaTime.png/jpg/pdf` attached
- [ ] Short video link (≤ 5 minutes) in the comments
- [ ] GenAI declaration in the comments
