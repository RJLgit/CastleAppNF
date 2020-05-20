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

        int[] alnPics = {R.drawable.alnwick_castle, R.drawable.alnwickpictwo, R.drawable.alnwickpicthree};
        int[] walkPics = {R.drawable.walkworth_castle, R.drawable.warkpictwo, R.drawable.warkpicthree};
        int[] warwickPics = {R.drawable.warwick_castle, R.drawable.warwickpictwo, R.drawable.warkpicthree};
        int[] dunstanPics = {R.drawable.dunstan_castle, R.drawable.dunstanpictwo, R.drawable.dunstanpicthree};
        int[] rabyPics = {R.drawable.raby_castle, R.drawable.rabypictwo, R.drawable.rabypicthree};
        int[] berkPics = {R.drawable.berk_castle, R.drawable.berkpictwo, R.drawable.berkpicthree};
        int[] caenPics = {R.drawable.caenarven_castle, R.drawable.caenpictwo, R.drawable.caenpicthree};
        int[] banPics = {R.drawable.banburgh_castle, R.drawable.bampictwo, R.drawable.bampicthree};
        int[] risPics = {R.drawable.castle_rising, R.drawable.risingpictwo, R.drawable.risingpicthree};
        int[] dovPics = {R.drawable.dover_castle, R.drawable.doverpictwo, R.drawable.doverpicthree};

        warkworth = new Castles("Warkworth Castle", "English Heritage", 5, context.getResources().getStringArray(R.array.wark_history), walkPics, R.raw.canon_in_d, -1.6118, 55.3452, "https://www.english-heritage.org.uk/visit/places/warkworth-castle-and-hermitage/", "https://www.english-heritage.org.uk/visit/places/warkworth-castle-and-hermitage/prices-and-opening-times/");
        /*dunstan = new Castles("Dunstanburgh Castle", "English Heritage", 3, context.getString(R.string.dunstan_History), dunstanPics, R.raw.four_seasons_spring, -1.5950, 55.4894, "https://www.english-heritage.org.uk/visit/places/dunstanburgh-castle/", "https://www.english-heritage.org.uk/visit/places/dunstanburgh-castle/prices-and-opening-times/");
        rising = new Castles("Castle Rising", "Castle rising", 4, context.getString(R.string.rising_History), risPics, R.raw.canon_in_d, 0.4704, 52.7981, "http://www.castlerising.co.uk/", "http://www.castlerising.co.uk/visit-us/");
        berkhamsted = new Castles("Berkhamsted Castle", "English Heritage", 2, context.getString(R.string.berk_History), berkPics, R.raw.four_seasons_spring, -0.5591, 51.7639, "https://www.english-heritage.org.uk/visit/places/berkhamsted-castle/", "https://www.english-heritage.org.uk/visit/places/berkhamsted-castle/");
        warwick = new Castles("Warwick Castle", "Warwick Castle", 5, context.getString(R.string.warwick_History), warwickPics, R.raw.canon_in_d, -1.5852, 52.2797, "https://www.warwick-castle.com/", "https://www.warwick-castle.com/tickets-passes/day-tickets/castle-tickets/");
        banbury = new Castles("Bamburgh Castle", "Bamburgh Castle", 1, context.getString(R.string.banbury_History), banPics, R.raw.four_seasons_spring, -1.7099, 55.6090, "https://www.bamburghcastle.com/", "https://www.bamburghcastle.com/visit-us/");
        alnwick = new Castles("Alnwick Castle", "Alnwick Castle", 3, context.getString(R.string.alnwick_History), alnPics, R.raw.canon_in_d, -1.7059, 55.4156, "https://www.alnwickcastle.com/", "https://www.alnwickcastle.com/opening-times-and-ticket-prices");
        dover = new Castles("Dover Castle", "English Heritage", 4, context.getString(R.string.dover_History), dovPics, R.raw.canon_in_d, 1.3234, 51.1288, "https://www.english-heritage.org.uk/visit/places/dover-castle/", "https://www.english-heritage.org.uk/visit/places/dover-castle/prices-and-opening-times/");
        raby = new Castles("Raby Castle", "Raby Castle", 2, context.getString(R.string.raby_History), rabyPics, R.raw.canon_in_d, -1.8020, 54.5916, "https://www.raby.co.uk/raby-castle/", "https://www.raby.co.uk/raby-castle/your-visit/plan-your-day/opening-times-prices/");
        caenarven = new Castles("Caernarfon Castle", "Cadw", 5, context.getString(R.string.caen_History), caenPics, R.raw.canon_in_d, -4.2769, 53.1391, "https://cadw.gov.wales/visit/places-to-visit/caernarfon-castle", "https://cadw.gov.wales/visit/places-to-visit/caernarfon-castle#opening-times");
        result.add(alnwick);
        result.add(banbury);
        result.add(berkhamsted);
        result.add(caenarven);
        result.add(dover);
        result.add(dunstan);
        result.add(raby);
        result.add(rising);*/
        result.add(warkworth);
        //result.add(warwick);






        return result;
    }

}
