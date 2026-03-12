// NEW
public class OrderFilter {
    private Double minPrice;

    public OrderFilter(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }
}