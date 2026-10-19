import java.util.ArrayList;
import java.util.List;

public class TecniHogarManager {
    private List<Order> orders;
    private List<Group> groups;
    private List<WorkingTime> workingTimes;

    private float registrationIncome;
    private float additionalVisitsIncome;

    public TecniHogarManager() {
        orders = new ArrayList<>();
        groups = new ArrayList<>();
        workingTimes = new ArrayList<>();
        registrationIncome = 0.0f;
        additionalVisitsIncome = 0.0f;
        loadInitialData();
    }

    private void loadInitialData() {
        // Cuadrillas iniciales (al menos 3, una con cert refrigeración y otra sin cert)
        Group g1 = new Group("G-01", 10.0f, true);
        g1.addTechnician(new Technician("T-101", "Carlos Pérez"));
        g1.addTechnician(new Technician("T-102", "Ana Gómez"));

        Group g2 = new Group("G-02", 8.0f, false);
        g2.addTechnician(new Technician("T-201", "Luis Martínez"));

        Group g3 = new Group("G-03", 12.0f, false);
        g3.addTechnician(new Technician("T-301", "María Rodríguez"));

        groups.add(g1);
        groups.add(g2);
        groups.add(g3);

        // Órdenes iniciales (al menos 2 de cada categoría, pendientes, sin ingresos registrados inicialmente)
        try {
            registerOrder(new Diagnosis("ORD-001", "Juan López", "Zona 10", 1.2f, WorkZone.Metropolitan));
            registerOrder(new Diagnosis("ORD-002", "Sofía Castro", "Antigua Guatemala", 1.5f, WorkZone.Peripheral));

            registerOrder(new Installation("ORD-003", "Pedro Ramírez", "Zona 14", 2.5f, WorkZone.Metropolitan, 4));
            registerOrder(new Installation("ORD-004", "Lucía Méndez", "Mixco", 3.0f, WorkZone.Peripheral, 2));

            registerOrder(new CertifiedInstallation("ORD-005", "Empresa ABC", "Zona 4", 3.0f, WorkZone.Metropolitan, 5, 2500.0f));
            registerOrder(new CertifiedInstallation("ORD-006", "Constructora XYZ", "Carretera a El Salvador", 4.0f, WorkZone.Peripheral, 3, 1800.0f));

            registerOrder(new ACMaintenance("ORD-007", "Hotel Reforma", "Zona 9", 1.5f, WorkZone.Metropolitan, 12000));
            registerOrder(new ACMaintenance("ORD-008", "Oficinas Centrales", "Zona 1", 2.0f, WorkZone.Metropolitan, 24000));
        } catch (Exception e) {
            System.err.println("Error al cargar datos iniciales: " + e.getMessage());
        }
    }

    public void registerOrder(Order order) {
        for (Order o : orders) {
            if (o.getId().equalsIgnoreCase(order.getId())) {
                throw new IllegalArgumentException("Ya existe una orden con el código: " + order.getId());
            }
        }
        float cost = order.quote();
        orders.add(order);
        registrationIncome += cost; // Se cobra el monto completo al registrar
    }

    public void registerGroup(Group group) {
        for (Group g : groups) {
            if (g.getId().equalsIgnoreCase(group.getId())) {
                throw new IllegalArgumentException("Ya existe una cuadrilla con el código: " + group.getId());
            }
        }
        groups.add(group);
    }

    public void registerWorkingTime(WorkingTime wt) {
        for (WorkingTime w : workingTimes) {
            if (w.getId().equalsIgnoreCase(wt.getId())) {
                throw new IllegalArgumentException("Ya existe una jornada con el código: " + wt.getId());
            }
        }
        // Validar que la cuadrilla no tenga otra jornada sin cerrar
        for (WorkingTime w : workingTimes) {
            if (w.getGroup().getId().equalsIgnoreCase(wt.getGroup().getId()) && w.getStatus() != WorkTimeStatus.Closed) {
                throw new IllegalStateException("La cuadrilla ya tiene una jornada activa sin cerrar.");
            }
        }
        workingTimes.add(wt);
    }

    public Order findOrderById(String Id) {
        for (Order o : orders) {
            if (o.getId().equalsIgnoreCase(Id)) return o;
        }

        return null;
    }

    public Group findGroupById(String id) {
        for (Group g : groups) {
            if (g.getId().equalsIgnoreCase(id)) return g;
        }
        return null;
    }

    public WorkingTime findWorkingTimeById(String id) {
        for (WorkingTime wt : workingTimes) {
            if (wt.getId().equalsIgnoreCase(id)) return wt;
        }
        return null;
    }

    public List<Order> getOrders() { 
        return orders; 
    }
    public List<Group> getGroups() { 
        return groups; 
    }
    public List<WorkingTime> getWorkingTimes() { 
        return workingTimes; 
    }

    public float getRegistrationIncome() { 
        return registrationIncome; 
    }
    public float getAdditionalVisitsIncome() { 
        return additionalVisitsIncome; 
    }
    public void addAdditionalVisitsIncome(float amount) { 
        this.additionalVisitsIncome += amount; 
    }
    public float getTotalIncome() { 
        return registrationIncome + additionalVisitsIncome; 
    }

    public float getIncomeByCategory(String categoryName) {
        float total = 0;
        for (Order o : orders) {
            if (o.categoryName().equalsIgnoreCase(categoryName)) {
                total += o.quote();
            }
        }
        return total;
    }
}