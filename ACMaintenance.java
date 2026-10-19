public class ACMaintenance extends Order {
    private int BTUQuantity;

    public ACMaintenance(String Id, String costumerName, String address, float workHours, WorkZone zone, int BTUQuantity) {
        super(Id, costumerName, address, workHours, zone);

        if (zone != WorkZone.Metropolitan) {
            throw new IllegalArgumentException("El mantenimiento de aire acondicionado solo está disponible en la zona metropolitana.");
        }

        if (BTUQuantity < 9000 || BTUQuantity > 60000) {
            throw new IllegalArgumentException("La capacidad en BTU debe estar entre 9,000 y 60,000.");
        }

        this.BTUQuantity = BTUQuantity;
    }

    public int getBTUQuantity() {
        return BTUQuantity;
    }

    @Override
    public float quote() {
        float baseRate = 100.0f;
        return baseRate + (workHours * 110.0f) + 80.0f;  
    }

    @Override
    public int estimatedDays() {
        return 2;
    }

    @Override
    public int maxVisits() {
        return 1;
    }

    @Override
    public String categoryName() {
        return "Mantenimiento de Aire Acondicionado";
    }
}