package com.posadskiy.java.release.v15;

import lombok.extern.log4j.Log4j2;

/**
 * JEP 378: Text Blocks
 * <a href="https://openjdk.org/jeps/378">Docs</a>
 */
@Log4j2
public class TextBlocks {

    public static void main(String[] args) {
        log.info(TextBlocks::getHtml);
        log.info(TextBlocks::getSql);

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
