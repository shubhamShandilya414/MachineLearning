// NEW
public class OrderDto {
    private Long id;
    private String name;
    private Double price;

    // NEW
    public static OrderDto fromOrder(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setName(order.getName());
        dto.setPrice(order.getPrice());
        return dto;
    }

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}