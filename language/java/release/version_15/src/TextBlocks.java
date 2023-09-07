import java.lang.System.Logger;
import java.lang.System.Logger.Level;

/**
 * JEP 378: Text Blocks
 * <a href="https://openjdk.org/jeps/378">Docs</a>
 */
public class TextBlocks {

    private final static Logger log = System.getLogger("default");


    public static void main(String[] args) {
        log.log(Level.INFO, TextBlocks::getHtml);
        log.log(Level.INFO, TextBlocks::getSql);

    }

    private static String getHtml() {
        return """
                        
            <html>
                <body>
                    <p>Hello, world</p>
                </body>
            </html>
            """;
    }

    private static String getSql() {
        return """
            SELECT "EMP_ID", "LAST_NAME" FROM "EMPLOYEE_TB"
            WHERE "CITY" = 'INDIANAPOLIS'
            ORDER BY "EMP_ID", "LAST_NAME";
            """;
    }
}
