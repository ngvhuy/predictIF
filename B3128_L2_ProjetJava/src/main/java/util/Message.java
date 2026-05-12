package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Pour simuler l'envoi d'un mail ou d'un sms.
 * 
 * @author DASI Team
 */
public class Message {

    public static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd~HH:mm:ss");
    public static final DateTimeFormatter HORODATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm:ss");

    private static void debut() {
        System.out.println();
        System.out.println();
        System.out.println("---<([ MESSAGE @ " + LocalDateTime.now().format(TIMESTAMP_FORMAT) + " ])>---");
        System.out.println();
    }

    private static void fin() {
        System.out.println();
        System.out.println("---<([ FIN DU MESSAGE ])>---");
        System.out.println();
        System.out.println();
    }

    public static void envoyerMail(String mailExpediteur, String mailDestinataire, String objet, String corps) {
        Message.debut();
        System.out.println("~~~ E-mail envoyé le " + LocalDateTime.now().format(HORODATE_FORMAT) + " ~~~");
        System.out.println("De : " + mailExpediteur);
        System.out.println("À  : " + mailDestinataire);
        System.out.println("Obj: " + objet);
        System.out.println();
        System.out.println(corps);
        Message.fin();
    }

    public static void envoyerNotification(String telephoneDestinataire, String message) {
        Message.debut();
        System.out.println("~~~ Notification envoyée le " + LocalDateTime.now().format(HORODATE_FORMAT) + " ~~~");
        System.out.println("À  : " + telephoneDestinataire);
        System.out.println();
        System.out.println(message);
        Message.fin();
    }

    
    // Pour afficher les couleurs "--color always" à ajouter à la configuration Maven
    // Netbeans: menu Tools / Options / Java / Maven, paramètre "Global Execution Option"

    // ANSI escape codes for console colors
    // => valid until next line break or explicit RESET
    // => explicit RESET at the end of every colored line is anyway recommended...

    public static final String FG_BLACK = "\u001b[30m";
    public static final String FG_BLUE = "\u001b[34m";
    public static final String FG_CYAN = "\u001b[36m";
    public static final String FG_GREEN = "\u001b[32m";
    public static final String FG_MAGENTA = "\u001b[35m";
    public static final String FG_RED = "\u001b[31m";
    public static final String FG_WHITE = "\u001b[37m";
    public static final String FG_YELLOW = "\u001b[33m";

    public static final String BG_BLACK = "\u001b[40m"; // Problem ?
    public static final String BG_BLUE = "\u001b[44m";
    public static final String BG_CYAN = "\u001b[46m";
    public static final String BG_GREEN = "\u001b[42m";
    public static final String BG_MAGENTA = "\u001b[45m";
    public static final String BG_RED = "\u001b[41m";
    public static final String BG_WHITE = "\u001b[47m";
    public static final String BG_YELLOW = "\u001b[43m";

    public static final String RESET = "\u001B[0m";
}