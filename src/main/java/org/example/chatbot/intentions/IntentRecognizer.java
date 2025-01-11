package org.example.chatbot.intentions;

import java.text.Normalizer;
import java.util.*;

public class IntentRecognizer {

    private final Map<String, List<String>> synonyms;

    public IntentRecognizer() {
        synonyms = new HashMap<>();
        synonyms.put("bilete", Arrays.asList("bilete", "tichete", "locuri", "intrari", "bilet"));
        synonyms.put("pret", Arrays.asList("pret", "costa", "tarif", "cost", "valoare", "cat"));
        synonyms.put("artist", Arrays.asList("artist", "formatie", "trupa", "cantaret", "band", "canta", "performanta"));
    }

    /**
     * ✅ Eliminarea diacriticelor și normalizarea textului
     */
    public static String removeDiacritics(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase().trim();
    }

    /**
     * ✅ Verifică dacă un text conține sinonimele pentru o categorie
     */
    private boolean containsSynonym(String input, String key) {
        List<String> synonymsList = synonyms.getOrDefault(key, Collections.emptyList());
        String normalizedInput = removeDiacritics(input);
        return synonymsList.stream().anyMatch(normalizedInput::contains);
    }

    /**
     * ✅ Actualizare Regex pentru detectarea corectă a întrebărilor despre artiști
     */
    public String identifyIntent(String input) {
        String normalizedInput = removeDiacritics(input);

        if (containsSynonym(normalizedInput, "bilete") && normalizedInput.matches(".*(cate|ramase|disponibile|mai sunt).*")) {
            return "INQUIRE_TICKETS";
        }
        if (containsSynonym(normalizedInput, "pret") && containsSynonym(normalizedInput, "bilete")) {
            return "INQUIRE_PRICE";
        }
        if (normalizedInput.matches(".*(vreau sa rezerv|as dori sa rezerv).*bilete.*")) {
            return "INQUIRE_RESERVATION";
        }
        if (containsSynonym(normalizedInput, "artist") && normalizedInput.matches(".*(cand canta|cand este|when is|performanta).*")) {
            return "INQUIRE_ARTIST";
        }
        return "UNKNOWN_INTENT";
    }
    public String extractTicketType(String input) {
        String normalizedInput = removeDiacritics(input);
        if (normalizedInput.contains("ultra vip")) return "ultra vip";
        if (normalizedInput.contains("vip")) return "vip";
        if (normalizedInput.contains("general admission")) return "general admission";
        return "general admission";  // ✅ Fallback pentru bilet standard
    }
}
