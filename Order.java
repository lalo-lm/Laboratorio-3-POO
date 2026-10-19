public abstract class Order implements Quotable {
    protected String Id;
    protected String customerName;
    protected String address;
    protected float workHours;
    protected WorkZone zone;
    protected OrderStatus status;
    protected int visitsCount;

    public Order(String Id, String customerName, String address, float workHours, WorkZone zone) {
        if (Id == null || Id.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de la orden no puede estar vacío.");
        }
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente no puede estar vacío.");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección no puede estar vacía.");
        }
        if (workHours <= 0) {
            throw new IllegalArgumentException("Las horas de trabajo deben ser estrictamente positivas.");
        }
        if (zone == null) {
            throw new IllegalArgumentException("La zona no puede ser nula.");
        }

        this.Id = Id;
        this.customerName = customerName;
        this.address = address;
        this.workHours = workHours;
        this.zone = zone;
        this.status = OrderStatus.Pending;
        this.visitsCount = 0;
    }

    public abstract int estimatedDays();
    public abstract int maxVisits();
    public abstract String categoryName();

    public String getId() {
        return Id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getAddress() {
        return address;
    }

    public float getWorkHours() {
        return workHours;
    }

    public WorkZone getZone() {
        return zone;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public int getVisitsCount() {
        return visitsCount;
    }

    public void incrementVisitsCount() {
        this.visitsCount++;
    }

    @Override
    public String toString() {
        return String.format("Orden [%s] | Categoría: %s | Cliente: %s | Zona: %s | Estado: %s | Visitas: %d / %d",
                Id, categoryName(), customerName, zone, status, visitsCount, maxVisits());
    }
}