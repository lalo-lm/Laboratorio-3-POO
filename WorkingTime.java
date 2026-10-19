import java.util.List;

public class WorkingTime {
    private String Id;
    private Group group;
    private List<Order> orders;
    private WorkTimeStatus status;

    public WorkingTime(String Id, Group group) {
        if (Id == null || Id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de la jornada no puede estar vacío.");
        }

        if (group == null) {
            throw new IllegalArgumentException("La jornada debe estar asignada a una cuadrilla válida.");
        }

        this.Id = Id;
        this.group = group;
        this.orders = orders;
        this.status = WorkTimeStatus.In_preparation;
    }

    public void assignOrder(Order order, List<WorkingTime> allWorkingTimes) {
        if (status != WorkTimeStatus.In_preparation) {
            throw new IllegalStateException("Solo se pueden asignar órdenes a jornadas en preparación.");
        }
        if (order.getStatus() != OrderStatus.Pending) {
            throw new IllegalArgumentException("La orden debe estar en estado PENDING para ser asignada.");
        }

        // Validación 1: Verificar que la orden no esté asignada en otra jornada activa (no cerrada)
        for (WorkingTime wt : allWorkingTimes) {
            if (wt.getStatus() != WorkTimeStatus.Closed && wt.getOrders().contains(order)) {
                throw new IllegalStateException("La orden ya está asignada en otra jornada activa.");
            }
        }

        // Validación 2: Restricción de refrigeración para AC
        if (order instanceof ACMaintenance && !group.isRefrigeratedCertified()) {
            throw new IllegalArgumentException("La cuadrilla no cuenta con certificación en refrigeración para este mantenimiento de AC.");
        }

        // Validación 3: Capacidad de horas de la cuadrilla (horas estimadas)
        float currentTotalHours = 0;
        for (Order o : orders) {
            currentTotalHours += o.getWorkHours();
        }
        if (currentTotalHours + order.getWorkHours() > group.getCapacityHoursPerDay()) {
            throw new IllegalArgumentException("La orden excede la capacidad máxima de horas diarias de la cuadrilla.");
        }

        orders.add(order);
    }
    
    public void removeOrder(Order order) {
        if (status != WorkTimeStatus.In_preparation) {
            throw new IllegalStateException("Solo se pueden retirar órdenes de jornadas en preparación.");
        }
        if (!orders.remove(order)) {
            throw new IllegalArgumentException("La orden no se encuentra en esta jornada.");
        }
    }

    public void start(TecniHogarManager manager) {
        if (status != WorkTimeStatus.In_preparation) {
            throw new IllegalStateException("La jornada ya fue iniciada o cerrada.");
        }
        if (orders.isEmpty()) {
            throw new IllegalStateException("No se puede iniciar una jornada vacía.");
        }

        for (Order order : orders) {
            order.setStatus(OrderStatus.In_attention);

            // Registro de cargos por visitas adicionales (Q40 por cada visita a partir de la 2da)
            if (order.getVisitsCount() > 0) {
                float extraVisitCharge = order.getVisitsCount() * 40.0f;
                manager.addAdditionalVisitsIncome(extraVisitCharge);
            }
        }

        this.status = WorkTimeStatus.Started;
    }

    public void close() {
        if (status != WorkTimeStatus.Started) {
            throw new IllegalStateException("Solo se puede cerrar una jornada que esté en curso (STARTED).");
        }
        for (Order order : orders) {
            if (order.getStatus() == OrderStatus.In_attention) {
                throw new IllegalStateException("Todas las órdenes de la jornada deben tener un resultado registrado (COMPLETE o FAILED/CANCELLED).");
            }
        }
        this.status = WorkTimeStatus.Closed;
    }

    public String getId() { 
        return Id; 
    }
    public Group getGroup() { 
        return group; 
    }
    public List<Order> getOrders() { 
        return orders; 
    }
    public WorkTimeStatus getStatus() { 
        return status; 
    }

    public float calculateAssignedHours() {
        float total = 0;
        for (Order o : orders) {
            total += o.getWorkHours();
        }
        return total;
    }
}