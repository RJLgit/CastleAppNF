package com.example.android.castleappnf;

import android.content.Context;

import java.util.ArrayList;
/*Class that creates the castles and generates an ArrayList of them for the app to use.*/

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
        Castles maiden;
        Castles tamworth;
        Castles lincoln;
        Castles colchester;
        Castles ludlow;
        Castles restormel;
        Castles belvoir;
        Castles berkeley;
        Castles harlech;
        Castles longtown;
        Castles launceston;
        Castles lindisfarne;
        Castles totnes;
        Castles wallingford;
        Castles kenilworth;

        warkworth = new Castles("Warkworth Castle", "English Heritage", 5, context.getResources().getStringArray(R.array.wark_history), R.raw.music_one, -1.6118, 55.3452, "https://www.english-heritage.org.uk/visit/places/warkworth-castle-and-hermitage/", "https://www.english-heritage.org.uk/visit/places/warkworth-castle-and-hermitage/prices-and-opening-times/");
        dunstan = new Castles("Dunstanburgh Castle", "English Heritage", 3, context.getResources().getStringArray(R.array.dunstan_history), R.raw.music_two, -1.5950, 55.4894, "https://www.english-heritage.org.uk/visit/places/dunstanburgh-castle/", "https://www.english-heritage.org.uk/visit/places/dunstanburgh-castle/prices-and-opening-times/");
        rising = new Castles("Castle Rising", "Castle rising", 4, context.getResources().getStringArray(R.array.rising_history), R.raw.music_three, 0.4704, 52.7981, "http://www.castlerising.co.uk/", "http://www.castlerising.co.uk/visit-us/");
        berkhamsted = new Castles("Berkhamsted Castle", "English Heritage", 2, context.getResources().getStringArray(R.array.berk_history), R.raw.music_four, -0.5591, 51.7639, "https://www.english-heritage.org.uk/visit/places/berkhamsted-castle/", "https://www.english-heritage.org.uk/visit/places/berkhamsted-castle/");
        warwick = new Castles("Warwick Castle", "Warwick Castle", 5, context.getResources().getStringArray(R.array.warwick_history), R.raw.music_one, -1.5852, 52.2797, "https://www.warwick-castle.com/", "https://www.warwick-castle.com/tickets-passes/day-tickets/castle-tickets/");
        banbury = new Castles("Bamburgh Castle", "Bamburgh Castle", 1, context.getResources().getStringArray(R.array.bamburgh_history), R.raw.music_two, -1.7099, 55.6090, "https://www.bamburghcastle.com/", "https://www.bamburghcastle.com/visit-us/");
        alnwick = new Castles("Alnwick Castle", "Alnwick Castle", 3, context.getResources().getStringArray(R.array.alnwick_history), R.raw.music_three, -1.7059, 55.4156, "https://www.alnwickcastle.com/", "https://www.alnwickcastle.com/opening-times-and-ticket-prices");
        dover = new Castles("Dover Castle", "English Heritage", 4, context.getResources().getStringArray(R.array.dover_history), R.raw.music_four, 1.3234, 51.1288, "https://www.english-heritage.org.uk/visit/places/dover-castle/", "https://www.english-heritage.org.uk/visit/places/dover-castle/prices-and-opening-times/");
        raby = new Castles("Raby Castle", "Raby Castle", 2, context.getResources().getStringArray(R.array.raby_history), R.raw.music_one, -1.8020, 54.5916, "https://www.raby.co.uk/raby-castle/", "https://www.raby.co.uk/raby-castle/your-visit/plan-your-day/opening-times-prices/");
        caenarven = new Castles("Caernarfon Castle", "Cadw", 5, context.getResources().getStringArray(R.array.caen_history), R.raw.music_two, -4.2769, 53.1391, "https://cadw.gov.wales/visit/places-to-visit/caernarfon-castle", "https://cadw.gov.wales/visit/places-to-visit/caernarfon-castle#opening-times");
        carlisle = new Castles("Carlisle Castle", "English Heritage", 3, context.getResources().getStringArray(R.array.carlisle_history), R.raw.music_three, -2.9418, 54.8970, "https://www.english-heritage.org.uk/visit/places/carlisle-castle/", "https://www.english-heritage.org.uk/visit/places/carlisle-castle/prices-and-opening-times/");
        bodiam = new Castles("Bodiam Castle", "National Trust", 4, context.getResources().getStringArray(R.array.bodiam_history), R.raw.music_four, 0.5435, 51.0023, "https://www.nationaltrust.org.uk/bodiam-castle", "https://www.nationaltrust.org.uk/bodiam-castle#Opening%20times");
        rochester = new Castles("Rochester Castle", "English Heritage", 2, context.getResources().getStringArray(R.array.rochester_history), R.raw.music_one, 0.5015, 51.3898, "https://www.english-heritage.org.uk/visit/places/rochester-castle/", "https://www.english-heritage.org.uk/visit/places/rochester-castle/prices-and-opening-times/");
        conway = new Castles("Conway Castle", "Cadw", 5, context.getResources().getStringArray(R.array.conway_history), R.raw.music_two, -3.8256, 53.2801, "https://cadw.gov.wales/visit/places-to-visit/conwy-castle", "https://cadw.gov.wales/visit/places-to-visit/conwy-castle#opening-times");
        durham = new Castles("Durham Castle", "Durham University", 2, context.getResources().getStringArray(R.array.durham_history), R.raw.music_three, -1.5763, 54.7754, "https://www.dur.ac.uk/durham.castle/visit/", "https://www.dur.ac.uk/durham.castle/visit/");
        edinburgh = new Castles("Edinburgh Castle", "Scottish Government", 5, context.getResources().getStringArray(R.array.edin_history), R.raw.music_four, -3.1203, 55.5655, "https://www.edinburghcastle.scot/", "https://www.edinburghcastle.scot/plan-your-visit/opening-times");
        conisbrough = new Castles("Conisbrough Castle", "English Heritage", 4, context.getResources().getStringArray(R.array.conis_history), R.raw.music_one, -1.1335, 53.2903, "https://www.english-heritage.org.uk/visit/places/conisbrough-castle/", "https://www.english-heritage.org.uk/visit/places/conisbrough-castle/prices-and-opening-times/");
        stirling = new Castles("Stirling Castle", "Historic Environment Scotland", 2, context.getResources().getStringArray(R.array.stirling_history), R.raw.music_two, -3.9474, 56.1238, "https://www.stirlingcastle.scot/", "https://www.stirlingcastle.scot/visit/opening-times/");
        york = new Castles("Clifford's Tower", "English Heritage", 2, context.getResources().getStringArray(R.array.york_history), R.raw.music_three, -1.0799, 53.9558, "https://www.english-heritage.org.uk/visit/places/cliffords-tower-york/", "https://www.english-heritage.org.uk/visit/places/cliffords-tower-york/prices-and-opening-times/");
        cardiff = new Castles("Cardiff Castle", "Cardiff Council", 5, context.getResources().getStringArray(R.array.cardiff_history), R.raw.music_four, -3.1812, 51.4822, "https://www.cardiffcastle.com/", "https://www.cardiffcastle.com/opening-times/");
        pontefract = new Castles("Pontefract Castle", "Wakefield Museums and Castles", 4, context.getResources().getStringArray(R.array.pontefract_history), R.raw.music_one, -1.3041, 53.6950, "https://www.pontefractcastle.co.uk/", "https://www.pontefractcastle.co.uk/gettinghere.aspx");
        lancaster = new Castles("Lancaster Castle", "Lancaster Castle", 4, context.getResources().getStringArray(R.array.lancaster_history), R.raw.music_two, -2.8049, 54.0498, "http://www.lancastercastle.com/", "http://www.lancastercastle.com/tours-visits/opening-times/");
        richmond = new Castles("Richmond Castle", "English Heritage", 2, context.getResources().getStringArray(R.array.richmond_history), R.raw.music_three, -1.7376, 54.4017, "https://www.english-heritage.org.uk/visit/places/richmond-castle/", "https://www.english-heritage.org.uk/visit/places/richmond-castle/prices-and-opening-times/");
        prudhoe = new Castles("Prudhoe Castle", "English Heritage", 3, context.getResources().getStringArray(R.array.prudhoe_history), R.raw.music_four, -1.854, 54.964, "https://www.english-heritage.org.uk/visit/places/prudhoe-castle/", "https://www.english-heritage.org.uk/visit/places/prudhoe-castle/prices-and-opening-times/");
        oxford = new Castles("Oxford Castle", "Oxford Castle", 2, context.getResources().getStringArray(R.array.oxford_history), R.raw.music_one, -1.2633, 51.7516, "https://www.oxfordcastleandprison.co.uk/", "https://www.oxfordcastleandprison.co.uk/your-visit/opening-times-and-prices/");
        lewes = new Castles("Lewes Castle", "Sussex Archaeological Society", 3, context.getResources().getStringArray(R.array.lewes_history), R.raw.music_two, 0.0078, 50.8729, "https://sussexpast.co.uk/properties-to-discover/lewes-castle", "https://sussexpast.co.uk/properties-to-discover/lewes-castle");
        tamworth = new Castles("Tamworth Castle", "Tamworth Castle", 4, context.getResources().getStringArray(R.array.tamworth_history), R.raw.music_three, -1.6969, 52.6326, "http://www.tamworthcastle.co.uk/", "http://www.tamworthcastle.co.uk/opening-times-prices");
        maiden = new Castles("Maiden Castle", "English Heritage", 2, context.getResources().getStringArray(R.array.maiden_history), R.raw.music_four, -2.4682, 50.6945, "https://www.english-heritage.org.uk/visit/places/maiden-castle/", "https://www.english-heritage.org.uk/visit/places/maiden-castle/#beforeyougo");
        colchester = new Castles("Colchester Castle", "Colchester museums", 4, context.getResources().getStringArray(R.array.colchester_history), R.raw.music_one, 0.9030, 51.8905, "https://colchester.cimuseums.org.uk/visit/colchester-castle/", "https://colchester.cimuseums.org.uk/visit/colchester-castle/");
        lincoln = new Castles("Lincoln Castle", "Lincoln Castle", 4, context.getResources().getStringArray(R.array.lincoln_history), R.raw.music_two, -0.5407, 53.2346, "https://www.lincolncastle.com/", "https://www.lincolncastle.com/content/plan-your-visit");
        restormel = new Castles("Restormel Castle", "English Heritage", 2, context.getResources().getStringArray(R.array.restormel_history), R.raw.music_three, -4.6715, 50.4223, "https://www.english-heritage.org.uk/visit/places/restormel-castle/", "https://www.english-heritage.org.uk/visit/places/restormel-castle/prices-and-opening-times/");
        ludlow = new Castles("Ludlow Castle", "Ludlow Castle", 5, context.getResources().getStringArray(R.array.ludlow_history), R.raw.music_four, -2.7230, 52.3672, "https://www.ludlowcastle.com/", "https://www.ludlowcastle.com/times-admission/");
        berkeley = new Castles("Berkeley Castle", "Berkeley Castle", 5, context.getResources().getStringArray(R.array.berkeley_history), R.raw.music_one, -2.4571, 51.6879, "https://www.berkeley-castle.com/", "https://www.berkeley-castle.com/visit");
        belvoir = new Castles("Belvoir Castle", "Belvoir Castle", 4, context.getResources().getStringArray(R.array.belvoir_history), R.raw.music_two, -0.7762, 52.8877, "https://www.belvoircastle.com/", "https://www.belvoircastle.com/castle-opening-times/");
        longtown = new Castles("Longtown Castle", "English Heritage", 2, context.getResources().getStringArray(R.array.longtown_history), R.raw.music_three, -2.9910, 51.9562, "https://www.english-heritage.org.uk/visit/places/longtown-castle/", "https://www.english-heritage.org.uk/visit/places/longtown-castle/opening-times/");
        harlech = new Castles("Harlech Castle", "Cadw", 5, context.getResources().getStringArray(R.array.harlech_history), R.raw.music_four, -4.1089, 52.8600, "https://cadw.gov.wales/visit/places-to-visit/harlech-castle", "https://cadw.gov.wales/visit/places-to-visit/harlech-castle#opening-times");
        launceston = new Castles("Launceston Castle", "English Heritage", 2, context.getResources().getStringArray(R.array.launceston_history), R.raw.music_one, -4.3614, 50.6378, "https://www.english-heritage.org.uk/visit/places/launceston-castle/", "https://www.english-heritage.org.uk/visit/places/launceston-castle/prices-and-opening-times/");
        lindisfarne = new Castles("Lindisfarne Castle", "National Trust", 4, context.getResources().getStringArray(R.array.lindisfarne_history), R.raw.music_two, -1.7850, 55.6690, "https://www.nationaltrust.org.uk/lindisfarne-castle", "https://www.nationaltrust.org.uk/lindisfarne-castle#Opening%20times");
        totnes = new Castles("Totnes Castle", "English Heritage", 2, context.getResources().getStringArray(R.array.totnes_history), R.raw.music_three, -3.6906, 50.4320, "https://www.english-heritage.org.uk/visit/places/totnes-castle/", "https://www.english-heritage.org.uk/visit/places/totnes-castle/opening-times/");
        wallingford = new Castles("Wallingford Castle", "Wallingford council", 2, context.getResources().getStringArray(R.array.wallingford_history), R.raw.music_four, -1.1214, 51.6025, "https://www.wallingford.co.uk/visitors/places-to-visit/castle-gardens---meadow", "https://www.wallingford.co.uk/visitors/places-to-visit/castle-gardens---meadow");
        kenilworth = new Castles("Kenilworth Castle", "English Heritage", 4, context.getResources().getStringArray(R.array.kenilworth_history), R.raw.music_one, -1.5921, 52.3482, "https://www.english-heritage.org.uk/visit/places/kenilworth-castle/", "https://www.english-heritage.org.uk/visit/places/kenilworth-castle/prices-and-opening-times/");



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
        result.add(maiden);
        result.add(tamworth);
        result.add(lincoln);
        result.add(colchester);
        result.add(ludlow);
        result.add(restormel);
        result.add(berkeley);
        result.add(belvoir);
        result.add(harlech);
        result.add(longtown);
        result.add(lindisfarne);
        result.add(launceston);
        result.add(totnes);
        result.add(wallingford);
        result.add(kenilworth);

        return result;
    }

}
