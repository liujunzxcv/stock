public class StockVo {
    private String date;

    private float open;

    private float close;

    private float high;
    private float low;


    public float getOpen() {
        return open;
    }

    public StockVo setOpen(float open) {
        this.open = open;
        return this;
    }

    public float getClose() {
        return close;
    }

    public StockVo setClose(float close) {
        this.close = close;
        return this;
    }

    public float getHigh() {
        return high;
    }

    public StockVo setHigh(float high) {
        this.high = high;
        return this;
    }

    public float getLow() {
        return low;
    }

    public StockVo setLow(float low) {
        this.low = low;
        return this;
    }

    public String getDate() {
        return date;
    }

    public StockVo setDate(String date) {
        this.date = date;
        return this;
    }
}
