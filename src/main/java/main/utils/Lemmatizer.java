package main.utils;

import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class Lemmatizer
{
    private static final List<String> auxiliaryParts = List.of("ПРЕДЛ", "МСП", "СОЮЗ", "МЕЖД", "ЧАСТ");

    public Lemmatizer(){
    }

    public String replaceAuxiliarySymbols(String string) {
        String regexToRemoveLatinsAndPunctuation = "[^а-яА-Я\s]";
        String regexToRemoveMultipleSpaces = "[\\s]{2,}";
        return string
                .toLowerCase(Locale.ROOT)
                .replaceAll(regexToRemoveLatinsAndPunctuation, " ")
                .trim()
                .replaceAll(regexToRemoveMultipleSpaces, " ");
    }

    public ArrayList<String> getBasicFormsFromString(String input) throws IOException {
        input = replaceAuxiliarySymbols(input);
        LuceneMorphology luceneMorphology = new RussianLuceneMorphology();
        ArrayList<String> wordsToCount = new ArrayList<>();
        String[] wordsFromInput = input.split(" ");
        for (String w : wordsFromInput) {
            String[] words2 = luceneMorphology.getMorphInfo(w).toString().split(" ");
            if(!auxiliaryParts.contains(words2[1].replaceAll("[^а-яА-Я]", "")) && w.length() > 2) {
                int separator = luceneMorphology.getMorphInfo(w).toString().indexOf("|");
                wordsToCount.add(luceneMorphology.getMorphInfo(w).toString().substring(1,separator));
            }
        }
        return wordsToCount;
    }

    public HashMap<String, Float> lemmasMapFromString(String input) {
        ArrayList<String> list = null;
        HashMap<String, Float> map = new HashMap<>();
        try {
            list = getBasicFormsFromString(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(list != null) {
            map = countLemmas(list);
        }
        return map;
    }

    public HashMap<String, Float> countLemmas(ArrayList<String> wordsToCount) {
        HashMap<String, Float> lemmas = new HashMap<>();
        wordsToCount.forEach(w2c -> {
            Float count = (float) Collections.frequency(wordsToCount, w2c);
            lemmas.put(w2c, count);
        });
        return lemmas;
    }
}
