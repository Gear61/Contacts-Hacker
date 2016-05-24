package com.randomappsinc.contactshacker.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alexanderchiou on 5/23/16.
 */
public class GroupServer {
    private static GroupServer instance;
    private Map<String, List<String>> mappings;
    private List<String> listNames;

    private GroupServer() {
        mappings = new HashMap<>();

        List<String> theAvengers = new ArrayList<>(Arrays.asList("Iron Man", "Captain America",
                "The Hulk", "Thor", "Black Widow", "Hawkeye"));
        mappings.put("Avengers", theAvengers);

        List<String> theJusticeLeague = new ArrayList<>(Arrays.asList("Superman", "Batman", "Wonder Woman",
                "Flash", "Green Lantern", "Aquaman"));
        mappings.put("Justice League", theJusticeLeague);

        List<String> theThreeStooges = new ArrayList<>(Arrays.asList("Moe", "Larry", "Curly"));
        mappings.put("Three Stooges", theThreeStooges);

        List<String> harryPotterTrio = new ArrayList<>(Arrays.asList("Harry", "Ron", "Hermione"));
        mappings.put("Harry Potter Trio", harryPotterTrio);

        List<String> theSevenDwarfs = new ArrayList<>(Arrays.asList("Doc", "Dopey", "Bashful", "Grumpy",
                "Grumpy", "Sneezy", "Sleepy", "Happy"));
        mappings.put("7 Dwarfs", theSevenDwarfs);

        List<String> lotrFellowship = new ArrayList<>(Arrays.asList("Frodo", "Sam", "Merry", "Pippin", "Legolas",
                "Boromir", "Gandalf", "Gimli", "Aragorn"));
        mappings.put("Lord of the Rings Fellowship", lotrFellowship);

        List<String> southParkQuad = new ArrayList<>(Arrays.asList("Stan", "Kyle", "Kenny", "Cartman"));
        mappings.put("South Park Quad", southParkQuad);

        List<String> onePieceCrew = new ArrayList<>(Arrays.asList("Luffy", "Zoro", "Nami", "Usopp", "Sanji", "Robin",
                "Franky", "Brooke"));
        mappings.put("One Piece Crew", onePieceCrew);

        List<String> scoobyGang = new ArrayList<>(Arrays.asList("Scooby", "Shaggy", "Velma", "Fred", "Daphne"));
        mappings.put("Scooby Gang", scoobyGang);

        List<String> threeMusketeers = new ArrayList<>(Arrays.asList("Athos", "Porthos", "Aramis"));
        mappings.put("3 Musketeers", threeMusketeers);

        listNames = new ArrayList<>();
        for (String listName : mappings.keySet()) {
            listNames.add(listName);
        }
        Collections.sort(listNames);
    }

    public static GroupServer getInstance() {
        if (instance == null) {
            instance = new GroupServer();
        }
        return instance;
    }

    public List<String> getNamesInList(String listName) {
        return mappings.get(listName);
    }

    public List<String> getListNames() {
        return listNames;
    }
}
