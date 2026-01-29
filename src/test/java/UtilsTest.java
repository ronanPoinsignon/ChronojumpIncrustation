import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import prog.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class UtilsTest {

    Map<String, String[]> nameResultMap = new HashMap<String, String[]>(){{
        put("nom prenom", new String[]{"nom", "prenom"});
        put(" nom  prenom ", new String[]{"nom", "prenom"});
        put("nom 2 prenom", new String[]{"nom 2", "prenom"});
        put("nom_prenom", new String[]{"nom", "prenom"});
        put("nom_2 prenom", new String[]{"nom", "2 prenom"});
        put("nom 1 2 3 prenom", new String[]{"nom 1 2 3", "prenom"});
        put("nom 1 2_3 prenom", new String[]{"nom 1 2", "3 prenom"});
        put("", new String[]{"", ""});
        put("nom", new String[]{"nom", ""});
        put(" nom ", new String[]{"nom", ""});
        put(null, new String[]{null, null});
    }};

    @Test
    public void testGetPrenom() {
        for(Map.Entry<String, String[]> entry : nameResultMap.entrySet()) {
            Assertions.assertEquals(entry.getValue()[1], Utils.getPrenom(entry.getKey()));
        }
    }

    @Test
    public void testGetNom() {
        for(Map.Entry<String, String[]> entry : nameResultMap.entrySet()) {
            Assertions.assertEquals(entry.getValue()[0], Utils.getNom(entry.getKey()));
        }
    }
}
