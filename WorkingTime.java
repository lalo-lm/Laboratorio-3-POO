import java.util.List;

public class WorkingTime {
    private Group group;
    private List<Order> orders;

    public void assignOrder(Order order) {
        orders.add(order);
    }
}