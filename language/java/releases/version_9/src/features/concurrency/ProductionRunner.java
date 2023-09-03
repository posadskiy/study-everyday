package features.concurrency;

public class ProductionRunner {

    public static void main(String[] args) {
        final Producer producer = new Producer();
        final Consumer consumerOne = new Consumer();
        final Consumer consumerTwo = new Consumer();

        producer.subscribe(consumerOne);
        producer.subscribe(consumerTwo);

        producer.createProduct();
        producer.createProduct();
    }

}
