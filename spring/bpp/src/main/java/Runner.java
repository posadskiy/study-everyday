import dto.ToDo;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import storage.Storage;

public class Runner {
    private static final String STORAGE_NAME = "storage.LocalStorage";

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Storage storage = context.getBean(STORAGE_NAME, Storage.class);

        storage.add(new ToDo()); 
        System.out.println("Storage = " + storage);
    }
}
