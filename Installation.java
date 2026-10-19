public class Installation extends Order {
    protected int pointsToInstall;

    public Installation(String Id, String costumerName, String address, float workHours, WorkZone zone, int pointsToInstall) {
        super(Id, costumerName, address, workHours, zone);

        if (pointsToInstall <= 0) {
            throw new IllegalArgumentException("La cantidad de puntos a instalar debe ser un entero positivo.");
        }

        this.pointsToInstall = pointsToInstall;
    }

    public int getPointsToInstall() {
        return pointsToInstall;
    }

    @Override
    public float quote() {
        float baseRate = (zone == WorkZone.Metropolitan) ? 100.0f : 175.0f;
        float hoursByPoints = pointsToInstall * 0.75f;
        float billableHours = Math.max(workHours, hoursByPoints);
        return baseRate + (billableHours * 90.0f);
    }

    @Override
    public int estimatedDays() {
        return (zone == WorkZone.Metropolitan) ? 3 : 5;
    }

    @Override
    public int maxVisits() {
        return 3;
    }

    @Override
    public String categoryName() {
        return "Instalación";
    }
}