package com.example.android.castleappnf;

import android.content.Context;

import java.util.ArrayList;

public class DummyData {
    public static ArrayList<Castles> generateAndReturnDataAZ(Context context) {
        ArrayList<Castles> result = new ArrayList<>();
        Castles warkworth;
        Castles dunstan;
        Castles rising;
        Castles berkhamsted;
        Castles warwick;
        Castles banbury;
        Castles alnwick;
        Castles dover;
        Castles raby;
        Castles caenarven;

        warkworth = new Castles("Warkworth", "English Heritage", 4, context.getString(R.string.warkworth_History), R.drawable.walkworth_castle, R.raw.canon_in_d, -1.6118, 55.3452, "https://www.english-heritage.org.uk/", "https://www.english-heritage.org.uk/visit/places/warkworth-castle-and-hermitage/prices-and-opening-times/");
        dunstan = new Castles("Dunstan", "English Heritage", 3, context.getString(R.string.dunstan_History), R.drawable.dunstan_castle, R.raw.four_seasons_spring, -1.5950, 55.4894, "https://www.english-heritage.org.uk/", "https://www.english-heritage.org.uk/visit/places/warkworth-castle-and-hermitage/prices-and-opening-times/");
        rising = new Castles("Rising", "Private", 45, context.getString(R.string.rising_History), R.drawable.castle_rising, R.raw.canon_in_d, 0.4704, 52.7981, "https://www.english-heritage.org.uk/", "https://www.english-heritage.org.uk/visit/places/warkworth-castle-and-hermitage/prices-and-opening-times/");
        berkhamsted = new Castles("Berkhamsted", "English Heritage", 2, context.getString(R.string.berk_History), R.drawable.berk_castle, R.raw.four_seasons_spring, -0.5591, 51.7639, "https://www.english-heritage.org.uk/", "https://www.english-heritage.org.uk/visit/places/warkworth-castle-and-hermitage/prices-and-opening-times/");
        warwick = new Castles("Warwick", "Warwick Castle", 5, context.getString(R.string.warwick_History), R.drawable.warwick_castle, R.raw.canon_in_d, -1.5852, 52.2797, "https://www.english-heritage.org.uk/", "https://www.english-heritage.org.uk/visit/places/warkworth-castle-and-hermitage/prices-and-opening-times/");
        banbury = new Castles("Banburgh", "English Heritage", 1, context.getString(R.string.banbury_History), R.drawable.banburgh_castle, R.raw.four_seasons_spring, -1.7099, 55.6090, "https://www.english-heritage.org.uk/", "https://www.english-heritage.org.uk/visit/places/warkworth-castle-and-hermitage/prices-and-opening-times/");
        alnwick = new Castles("Alnwick", "English Heritage", 3, context.getString(R.string.alnwick_History), R.drawable.alnwick_castle, R.raw.canon_in_d, -1.7059, 55.4156, "https://www.english-heritage.org.uk/", "https://www.english-heritage.org.uk/visit/places/warkworth-castle-and-hermitage/prices-and-opening-times/");
        dover = new Castles("Dover", "Dover castle", 4, context.getString(R.string.dover_History), R.drawable.dover_castle, R.raw.canon_in_d, 1.3234, 51.1288, "https://www.english-heritage.org.uk/", "https://www.english-heritage.org.uk/visit/places/warkworth-castle-and-hermitage/prices-and-opening-times/");
        raby = new Castles("Raby", "English Heritage", 2, context.getString(R.string.raby_History), R.drawable.raby_castle, R.raw.canon_in_d, -1.8020, 54.5916, "https://www.english-heritage.org.uk/", "https://www.english-heritage.org.uk/visit/places/warkworth-castle-and-hermitage/prices-and-opening-times/");
        caenarven = new Castles("Caenarven", "English Heritage", 5, context.getString(R.string.caen_History), R.drawable.caenarven_castle, R.raw.canon_in_d, -4.2769, 53.1391, "https://www.english-heritage.org.uk/", "https://www.english-heritage.org.uk/visit/places/warkworth-castle-and-hermitage/prices-and-opening-times/");
        result.add(alnwick);
        result.add(banbury);
        result.add(berkhamsted);
        result.add(caenarven);
        result.add(dover);
        result.add(dunstan);
        result.add(raby);
        result.add(rising);
        result.add(warkworth);
        result.add(warwick);






        return result;
    }

}
