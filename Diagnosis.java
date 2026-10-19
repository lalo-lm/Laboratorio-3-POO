public class Diagnosis extends Order {
    public Diagnosis(String Id, String costumerName, String address, float workHours, WorkZone zone) {
        super(Id, costumerName, address, workHours, zone);

        if (workHours > 1.5f) {
            throw new IllegalArgumentException("Un diagnóstico no puede superar las 1.5 horas de trabajo.");
        }
        
    }

    @Override
    public float quote() {
        return (zone == WorkZone.Metropolitan) ? 150.0f : 2250.0f;
    }

    @Override
    public int estimatedDays() {
        return (zone == WorkZone.Metropolitan) ? 1 : 2;
    }

    @Override
    public int maxVisits() {
        return 3;
    }

    @Override
    public String categoryName() {
        return "Diagnóstico";
    }
}