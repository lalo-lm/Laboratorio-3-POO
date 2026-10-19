public class CertifiedInstallation extends Installation {
    protected float materialCost;

    public CertifiedInstallation(String Id, String costumerName, String address, float workHours, WorkZone zone, int pointsToInstall, float materialCost) {
        super(Id, costumerName, address, workHours, zone, pointsToInstall);

        if (materialCost < 0) {
            throw new IllegalArgumentException("El costo de los materiales no puede ser negativo.");
        }

        this.materialCost = materialCost;
    }

    public float getMaterialCost() {
        return materialCost;
    }

    @Override
    public float quote() {
        float baseInstallationQuote = super.quote();
        float certificateFee = 120.0f;
        float materialGuarantee = materialCost * 0.05f;
        return baseInstallationQuote + certificateFee + materialGuarantee;
    }

    @Override
    public int estimatedDays() {
        return super.estimatedDays() +2;
    }

    @Override
    public int maxVisits() {
        return 2;
    }

    @Override
    public String categoryName() {
        return "Instalación Eléctrica Certificada";
    }
}