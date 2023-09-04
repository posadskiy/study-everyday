package features.concurrency;

import java.util.concurrent.Flow;

class Product implements Flow.Subscription {

    private final Integer value;

    public Product(Integer value) {
        this.value = value;
    }

    @Override
    public void request(long n) {

    }

    @Override
    public void cancel() {

    }

    public Integer getValue() {
        return value;
    }
}
