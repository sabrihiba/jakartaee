
package ma.emsi.sabri.tp0sabri.service;

import jakarta.enterprise.context.Dependent;
import java.io.Serializable;
import java.text.Normalizer;
import java.util.Locale;

@Dependent
public class GenerateurReponse implements Serializable {

    public String genererReponse(String question, String roleSysteme) {

        String texte = question.trim();
        String commande = texte.toLowerCase(Locale.ROOT);

        boolean dechiffrer = commande.startsWith("decrypt:");
        boolean chiffrer = commande.startsWith("encrypt:");

        if (!chiffrer && !dechiffrer) {
            return "Commande inconnue.\n"
                    + "Utilise encrypt: MESSAGE ou decrypt: MESSAGE";
        }

        String message = texte.substring(
                texte.indexOf(':') + 1
        ).trim();

        if (message.isEmpty()) {
            return "Erreur : aucun message à traiter.";
        }

        int decalage = 3;

        if (dechiffrer) {
            decalage = -decalage;
        }

        String resultat = cesar(message, decalage);

        return "=== SECRET MESSAGE DECODER ===\n"
                + "Mode : " + (dechiffrer ? "Déchiffrement" : "Chiffrement") + "\n"
                + "Décalage : 3\n"
                + "Message original : " + message + "\n"
                + "Résultat : " + resultat;
    }

    private String cesar(String texte, int decalage) {

        String normalise = Normalizer.normalize(
                texte, Normalizer.Form.NFD
        ).replaceAll("\\p{M}", "");

        StringBuilder resultat = new StringBuilder();

        for (char caractere : normalise.toCharArray()) {

            if (caractere >= 'A' && caractere <= 'Z') {
                int position = caractere - 'A';
                int nouvellePosition =
                        Math.floorMod(position + decalage, 26);

                resultat.append(
                        (char) ('A' + nouvellePosition)
                );

            } else if (caractere >= 'a' && caractere <= 'z') {
                int position = caractere - 'a';
                int nouvellePosition =
                        Math.floorMod(position + decalage, 26);

                resultat.append(
                        (char) ('a' + nouvellePosition)
                );

            } else {
                resultat.append(caractere);
            }
        }

        return resultat.toString();
    }
}
