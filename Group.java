import java.util.ArrayList;
import java.util.List;

public class Group {
    private String Id;
    private float capacityHoursPerDay ;
    private boolean refrigeratedCertification;
    private List<Technician> technicians;

    public Group(String Id, float capacityHoursPerDay, boolean refrigeratedCertification) {
        if (Id == null || Id.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de la cuadrilla no puede estar vacío.");
        }

        if (capacityHoursPerDay <= 0) {
            throw new IllegalArgumentException("La capacidad de horas por día debe ser positiva.");
        }

        this.Id = Id;
        this.capacityHoursPerDay = capacityHoursPerDay;
        this.refrigeratedCertification = refrigeratedCertification;
        this.technicians = new ArrayList<>();
    }

    public void addTechnician(Technician technician) {
        technicians.add(technician);
    }

    public String getId() {
        return Id;
    }

    public float getCapacityHoursPerDay() {
        return capacityHoursPerDay;
    }

    public boolean isRefrigeratedCertified() {
        return refrigeratedCertification;
    }

    public List<Technician> getTechnicians() {
        return technicians;
    }

    @Override
    public String toString() {
        return String.format("Cuadrilla [%s] | Capacidad %.1f hrs/día | Certificado Refrigeración: %s | Técnicos: %d", Id, capacityHoursPerDay, refrigeratedCertification ? "Sí" : "No", technicians.size());
    }
}