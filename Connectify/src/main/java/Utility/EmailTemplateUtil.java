package Utility;

import org.springframework.stereotype.Component;

@Component
public class EmailTemplateUtil {

    public static String buildEmailTemplate(
            String recipientName,
            String heading,
            String message

    ) {

        return """
                Hello %s,

                %s

                %s
                

                Thank you for being part of Connectify.

                Best Regards,
                Connectify Team
                """
                .formatted(
                        recipientName,
                        heading,
                        message
                );
    }
}