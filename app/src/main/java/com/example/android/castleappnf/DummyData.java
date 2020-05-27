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
        Castles carlisle;
        Castles bodiam;
        Castles rochester;
        Castles conway;
        Castles durham;
        Castles edinburgh;
        Castles stirling;
        Castles conisbrough;
        Castles york;
        Castles lancaster;
        Castles pontefract;
        Castles cardiff;
        Castles prudhoe;
        Castles richmond;
        Castles lewes;
        Castles oxford;


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
        int[] carPics = {R.drawable.caenarven_castle, R.drawable.caenpictwo, R.drawable.caenpicthree};
        //Need to add photos
        int[] bodPics = {R.drawable.banburgh_castle, R.drawable.bampictwo, R.drawable.bampicthree};
        int[] rochPics = {R.drawable.castle_rising, R.drawable.risingpictwo, R.drawable.risingpicthree};
        int[] conwayPics = {R.drawable.dover_castle, R.drawable.doverpictwo, R.drawable.doverpicthree};
        int[] edinPics = {R.drawable.caenarven_castle, R.drawable.caenpictwo, R.drawable.caenpicthree};
        int[] stirPics = {R.drawable.banburgh_castle, R.drawable.bampictwo, R.drawable.bampicthree};
        int[] durPics = {R.drawable.castle_rising, R.drawable.risingpictwo, R.drawable.risingpicthree};
        int[] conisPics = {R.drawable.dover_castle, R.drawable.doverpictwo, R.drawable.doverpicthree};
        int[] yorkPics = {R.drawable.caenarven_castle, R.drawable.caenpictwo, R.drawable.caenpicthree};
        int[] lancasterPics = {R.drawable.banburgh_castle, R.drawable.bampictwo, R.drawable.bampicthree};
        int[] pontePics = {R.drawable.castle_rising, R.drawable.risingpictwo, R.drawable.risingpicthree};
        int[] cardiffPics = {R.drawable.dover_castle, R.drawable.doverpictwo, R.drawable.doverpicthree};
        int[] prudhoePics = {R.drawable.castle_rising, R.drawable.risingpictwo, R.drawable.risingpicthree};
        int[] richmondPics = {R.drawable.dover_castle, R.drawable.doverpictwo, R.drawable.doverpicthree};
        int[] oxfordPics = {R.drawable.castle_rising, R.drawable.risingpictwo, R.drawable.risingpicthree};
        int[] lewesPics = {R.drawable.dover_castle, R.drawable.doverpictwo, R.drawable.doverpicthree};

        warkworth = new Castles("Warkworth Castle", "English Heritage", 5, context.getResources().getStringArray(R.array.wark_history), walkPics, R.raw.canon_in_d, -1.6118, 55.3452, "https://www.english-heritage.org.uk/visit/places/warkworth-castle-and-hermitage/", "https://www.english-heritage.org.uk/visit/places/warkworth-castle-and-hermitage/prices-and-opening-times/");
        dunstan = new Castles("Dunstanburgh Castle", "English Heritage", 3, context.getResources().getStringArray(R.array.dunstan_history), dunstanPics, R.raw.four_seasons_spring, -1.5950, 55.4894, "https://www.english-heritage.org.uk/visit/places/dunstanburgh-castle/", "https://www.english-heritage.org.uk/visit/places/dunstanburgh-castle/prices-and-opening-times/");
        rising = new Castles("Castle Rising", "Castle rising", 4, context.getResources().getStringArray(R.array.rising_history), risPics, R.raw.canon_in_d, 0.4704, 52.7981, "http://www.castlerising.co.uk/", "http://www.castlerising.co.uk/visit-us/");
        berkhamsted = new Castles("Berkhamsted Castle", "English Heritage", 2, context.getResources().getStringArray(R.array.berk_history), berkPics, R.raw.four_seasons_spring, -0.5591, 51.7639, "https://www.english-heritage.org.uk/visit/places/berkhamsted-castle/", "https://www.english-heritage.org.uk/visit/places/berkhamsted-castle/");
        warwick = new Castles("Warwick Castle", "Warwick Castle", 5, context.getResources().getStringArray(R.array.warwick_history), warwickPics, R.raw.canon_in_d, -1.5852, 52.2797, "https://www.warwick-castle.com/", "https://www.warwick-castle.com/tickets-passes/day-tickets/castle-tickets/");
        banbury = new Castles("Bamburgh Castle", "Bamburgh Castle", 1, context.getResources().getStringArray(R.array.bamburgh_history), banPics, R.raw.four_seasons_spring, -1.7099, 55.6090, "https://www.bamburghcastle.com/", "https://www.bamburghcastle.com/visit-us/");
        alnwick = new Castles("Alnwick Castle", "Alnwick Castle", 3, context.getResources().getStringArray(R.array.alnwick_history), alnPics, R.raw.canon_in_d, -1.7059, 55.4156, "https://www.alnwickcastle.com/", "https://www.alnwickcastle.com/opening-times-and-ticket-prices");
        dover = new Castles("Dover Castle", "English Heritage", 4, context.getResources().getStringArray(R.array.dover_history), dovPics, R.raw.canon_in_d, 1.3234, 51.1288, "https://www.english-heritage.org.uk/visit/places/dover-castle/", "https://www.english-heritage.org.uk/visit/places/dover-castle/prices-and-opening-times/");
        raby = new Castles("Raby Castle", "Raby Castle", 2, context.getResources().getStringArray(R.array.raby_history), rabyPics, R.raw.canon_in_d, -1.8020, 54.5916, "https://www.raby.co.uk/raby-castle/", "https://www.raby.co.uk/raby-castle/your-visit/plan-your-day/opening-times-prices/");
        caenarven = new Castles("Caernarfon Castle", "Cadw", 5, context.getResources().getStringArray(R.array.caen_history), caenPics, R.raw.canon_in_d, -4.2769, 53.1391, "https://cadw.gov.wales/visit/places-to-visit/caernarfon-castle", "https://cadw.gov.wales/visit/places-to-visit/caernarfon-castle#opening-times");
        carlisle = new Castles("Carlisle Castle", "English Heritage", 3, context.getResources().getStringArray(R.array.carlisle_history), carPics, R.raw.canon_in_d, -2.9418, 54.8970, "https://www.english-heritage.org.uk/visit/places/carlisle-castle/", "https://www.english-heritage.org.uk/visit/places/carlisle-castle/prices-and-opening-times/");
        bodiam = new Castles("Bodiam Castle", "National Trust", 4, context.getResources().getStringArray(R.array.bodiam_history), bodPics, R.raw.canon_in_d, 0.5435, 51.0023, "https://www.nationaltrust.org.uk/bodiam-castle", "https://www.nationaltrust.org.uk/bodiam-castle#Opening%20times");
        rochester = new Castles("Rochester Castle", "English Heritage", 2, context.getResources().getStringArray(R.array.rochester_history), rochPics, R.raw.canon_in_d, 0.5015, 51.3898, "https://www.english-heritage.org.uk/visit/places/rochester-castle/", "https://www.english-heritage.org.uk/visit/places/rochester-castle/prices-and-opening-times/");
        conway = new Castles("Conway Castle", "Cadw", 5, context.getResources().getStringArray(R.array.conway_history), conwayPics, R.raw.canon_in_d, -3.8256, 53.2801, "https://cadw.gov.wales/visit/places-to-visit/conwy-castle", "https://cadw.gov.wales/visit/places-to-visit/conwy-castle#opening-times");
        durham = new Castles("Durham Castle", "Durham University", 2, context.getResources().getStringArray(R.array.durham_history), durPics, R.raw.canon_in_d, -1.3434, 54.4629, "https://www.dur.ac.uk/durham.castle/visit/", "https://www.dur.ac.uk/durham.castle/visit/");
        edinburgh = new Castles("Edinburgh Castle", "Scottish Government", 5, context.getResources().getStringArray(R.array.edin_history), edinPics, R.raw.canon_in_d, -3.1203, 55.5655, "https://www.edinburghcastle.scot/", "https://www.edinburghcastle.scot/plan-your-visit/opening-times");
        conisbrough = new Castles("Conisbrough Castle", "English Heritage", 4, context.getResources().getStringArray(R.array.conis_history), conisPics, R.raw.canon_in_d, -1.1335, 53.2903, "https://www.english-heritage.org.uk/visit/places/conisbrough-castle/", "https://www.english-heritage.org.uk/visit/places/conisbrough-castle/prices-and-opening-times/");
        stirling = new Castles("Stirling Castle", "Historic Environment Scotland", 2, context.getResources().getStringArray(R.array.stirling_history), stirPics, R.raw.canon_in_d, -3.9474, 56.1238, "https://www.stirlingcastle.scot/", "https://www.stirlingcastle.scot/visit/opening-times/");
        york = new Castles("Clifford's Tower", "English Heritage", 2, context.getResources().getStringArray(R.array.york_history), yorkPics, R.raw.canon_in_d, -1.0799, 53.9558, "https://www.english-heritage.org.uk/visit/places/cliffords-tower-york/", "https://www.english-heritage.org.uk/visit/places/cliffords-tower-york/prices-and-opening-times/");
        cardiff = new Castles("Cardiff Castle", "Cardiff Council", 5, context.getResources().getStringArray(R.array.cardiff_history), cardiffPics, R.raw.canon_in_d, -3.1812, 51.4822, "https://www.cardiffcastle.com/", "https://www.cardiffcastle.com/opening-times/");
        pontefract = new Castles("Pontefract Castle", "Wakefield Museums and Castles", 4, context.getResources().getStringArray(R.array.pontefract_history), pontePics, R.raw.canon_in_d, -1.3041, 53.6950, "https://www.pontefractcastle.co.uk/", "https://www.pontefractcastle.co.uk/gettinghere.aspx");
        lancaster = new Castles("Lancaster Castle", "Lancaster Castle", 4, context.getResources().getStringArray(R.array.lancaster_history), lancasterPics, R.raw.canon_in_d, -2.8049, 54.0498, "http://www.lancastercastle.com/", "http://www.lancastercastle.com/tours-visits/opening-times/");
        richmond = new Castles("Richmond Castle", "English Heritage", 2, context.getResources().getStringArray(R.array.richmond_history), richmondPics, R.raw.canon_in_d, -1.7376, 54.4017, "https://www.english-heritage.org.uk/visit/places/richmond-castle/", "https://www.english-heritage.org.uk/visit/places/richmond-castle/prices-and-opening-times/");
        prudhoe = new Castles("Prudhoe Castle", "English Heritage", 3, context.getResources().getStringArray(R.array.prudhoe_history), prudhoePics, R.raw.canon_in_d, -1.854, 54.964, "https://www.english-heritage.org.uk/visit/places/prudhoe-castle/", "https://www.english-heritage.org.uk/visit/places/prudhoe-castle/prices-and-opening-times/");
        oxford = new Castles("Oxford Castle", "Oxford Castle", 2, context.getResources().getStringArray(R.array.oxford_history), oxfordPics, R.raw.canon_in_d, -1.2633, 51.7516, "https://www.oxfordcastleandprison.co.uk/", "https://www.oxfordcastleandprison.co.uk/your-visit/opening-times-and-prices/");
        lewes = new Castles("Lewes Castle", "Sussex Archaeological Society", 3, context.getResources().getStringArray(R.array.lewes_history), lewesPics, R.raw.canon_in_d, 0.0078, 50.8729, "https://sussexpast.co.uk/properties-to-discover/lewes-castle", "https://sussexpast.co.uk/properties-to-discover/lewes-castle");


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
        result.add(carlisle);
        result.add(bodiam);
        result.add(rochester);
        result.add(conway);
        result.add(durham);
        result.add(edinburgh);
        result.add(conisbrough);
        result.add(stirling);
        result.add(york);
        result.add(cardiff);
        result.add(pontefract);
        result.add(lancaster);
        result.add(richmond);
        result.add(prudhoe);
        result.add(oxford);
        result.add(lewes);






        return result;
    }

}
