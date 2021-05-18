package com.example.a8_1_sqlite.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ContactTable {

    //Tabellspesifikt for ContactTable:
    public static final String CONTACT_TABLE = "ContactTable";

    public static final String CONTACT_COL_ID = "_id";
    public static final String CONTACT_COL_LASTNAME = "etternavn";
    public static final String CONTACT_COL_FIRSTNAME = "fornavn";
	public static final String CONTACT_COL_FBLINK = "fb_link";
	public static final String CONTACT_COL_EMAIL = "email";
	public static final String CONTACT_COL_PHONE = "telefonnr";
	public static final String CONTACT_COL_BIRTHYEAR = "fodselsaar";

    // SQL statement for å opprette en ny tabell:
    private static final String CONTACT_TABLE_CREATE = "create table "
            + CONTACT_TABLE
            + " (" + CONTACT_COL_ID + " integer primary key autoincrement, "
            + CONTACT_COL_LASTNAME + " text not null, "
            + CONTACT_COL_FIRSTNAME + " text, "
			+ CONTACT_COL_FBLINK + " text, "
			+ CONTACT_COL_EMAIL + " text, "
			+ CONTACT_COL_PHONE + " text, "
			+ CONTACT_COL_BIRTHYEAR + " integer not null " + ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(CONTACT_TABLE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(ContactTable.class.getName(), "Oppgraderer databasen fra versjon "
				+ oldVersion + " til " + newVersion
				+ ". Alle gamle data vil slettes.");
		
		database.execSQL("DROP TABLE IF EXISTS " + CONTACT_TABLE);
		onCreate(database);
	}
}
