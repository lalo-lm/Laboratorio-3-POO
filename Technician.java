public class Technician {
    private String Id;
    private String name;

    public Technician(String Id, String name) {
        if (Id == null || Id.trim().isEmpty() || name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID y el nombre del técnico no pueden estar vacíos");
        }

        this.Id = Id;
        this.name = name;
    }

    public String getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("Técnico [%s] %s", Id, name);
    }
}