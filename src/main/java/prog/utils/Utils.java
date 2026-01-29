package prog.utils;

import java.util.Arrays;

public class Utils {

    public static String capitalizeFirstOnly(String value) {
        if(value == null) {
            return null;
        }
        if(value.trim().isEmpty()) {
            return "";
        }

        return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
    }

    public static String deleteCivility(String name) {
        return name.replace("(Mme\\.)|(M\\.)", "").trim();
    }

    public static String getPrenom(String chaine) {
        if(chaine == null) {
            return null;
        }

        chaine = deleteCivility(chaine);

        // marquage manuel de la séparation du nom/prénom dans le cas d'une composition (ex : Jean François Guillaume)
        if(chaine.contains("_")) {
            String[] split = chaine.split("_");
            if(split.length == 2) {
                return split[1];
            }

            System.out.println("Format incorrect pour la récupération du nom provenant de " + chaine + ".");
            return String.join(" ", split);
        }

        // cas standard où l'espace définit la séparation

        String[] split = chaine.split("\\s+");
        if(split.length < 2) {
            return "";
        }

        return split[split.length - 1];
    }

    public static String getNom(String chaine) {
        if(chaine == null) {
            return null;
        }

        chaine = deleteCivility(chaine);

        // marquage manuel de la séparation du nom/prénom dans le cas d'une composition (ex : Jean François Guillaume)
        if(chaine.contains("_")) {
            String[] split = chaine.split("_");
            if(split.length == 2) {
                return split[0];
            }

            System.out.println("Format incorrect pour la récupération du nom provenant de " + chaine + ".");
            return "";
        }

        // cas standard où l'espace définit la séparation

        String[] split = chaine.split("\\s+");
        if(split.length == 0) {
            return "";
        }
        if(split.length == 1) {
            return split[0];
        }

        return String.join(" ", Arrays.copyOf(split, split.length-1));
    }
}
