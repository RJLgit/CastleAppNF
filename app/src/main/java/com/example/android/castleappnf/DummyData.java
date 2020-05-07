package com.example.android.castleappnf;

import android.content.Context;

import java.util.ArrayList;

public class DummyData {
    public static ArrayList<Castles> generateAndReturnData(Context context) {
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

        warkworth = new Castles("Warkworth", "English Heritage", 4, context.getString(R.string.warkworth_History), R.drawable.walkworth_castle, R.raw.canon_in_d);
        dunstan = new Castles("Dunstan", "English Heritage", 3, context.getString(R.string.dunstan_History), R.drawable.dunstan_castle, R.raw.canon_in_d);
        rising = new Castles("Rising", "Private", 45, context.getString(R.string.rising_History), R.drawable.castle_rising, R.raw.canon_in_d);
        berkhamsted = new Castles("Berkhamsted", "English Heritage", 2, context.getString(R.string.berk_History), R.drawable.berk_castle, R.raw.canon_in_d);
        warwick = new Castles("Warwick", "Warwick Castle", 5, context.getString(R.string.warwick_History), R.drawable.warwick_castle, R.raw.canon_in_d);
        banbury = new Castles("Banburgh", "English Heritage", 1, context.getString(R.string.banbury_History), R.drawable.banburgh_castle, R.raw.canon_in_d);
        alnwick = new Castles("Alnwick", "English Heritage", 3, context.getString(R.string.alnwick_History), R.drawable.alnwick_castle, R.raw.canon_in_d);
        dover = new Castles("Dover", "Dover castle", 4, context.getString(R.string.dover_History), R.drawable.dover_castle, R.raw.canon_in_d);
        raby = new Castles("Raby", "English Heritage", 2, context.getString(R.string.raby_History), R.drawable.raby_castle, R.raw.canon_in_d);
        caenarven = new Castles("Caenarven", "English Heritage", 5, context.getString(R.string.caen_History), R.drawable.caenarven_castle, R.raw.canon_in_d);

        result.add(warkworth);
        result.add(dunstan);
        result.add(rising);
        result.add(berkhamsted);
        result.add(warwick);
        result.add(banbury);
        result.add(alnwick);
        result.add(dover);
        result.add(raby);
        result.add(caenarven);

        return result;
    }
}
