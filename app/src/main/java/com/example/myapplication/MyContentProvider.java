package com.example.myapplication;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.HashMap;

public class MyContentProvider extends ContentProvider {
    static final String AUTHORITY = "com.example.myapplication";
    static final String CONTENT_PROVIDER = "contentprovider";
    static final String URL = "context://" + AUTHORITY + "/" + CONTENT_PROVIDER;
    static final Uri CONTENT_URI = Uri.parse(URL);
    static final String PRODUCT_TABLE = "Products";

    private SQLiteDatabase db;

    static final int ONE = 1;
    static final int ALL = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PRODUCT_TABLE, ONE);
        uriMatcher.addURI(AUTHORITY, PRODUCT_TABLE + "/#", ALL);
    }

    private static HashMap<String, String> PROJECTION_MAP;

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        int count =db.delete("Products", selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        long number_row = db.insert(PRODUCT_TABLE, "", values);
        if (number_row > 0) {
            Uri uri1 = ContentUris.withAppendedId(CONTENT_URI, number_row);
            getContext().getContentResolver().notifyChange(uri1, null);
            return uri1;
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        Context context = getContext();
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        if (db == null) {
            return false;
        }
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        SQLiteQueryBuilder sqlite_builder = new SQLiteQueryBuilder();
        sqlite_builder.setTables(PRODUCT_TABLE);
        switch (uriMatcher.match(uri)) {
            case ALL:
                sqlite_builder.setProjectionMap(PROJECTION_MAP);
                break;
            case ONE:
                sqlite_builder.appendWhere("id" + "=" + uri.getPathSegments().get(0));
        }
        if (sortOrder == null || sortOrder == "") {
            sortOrder = "name";
        }
        Cursor cursor = sqlite_builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        int count = db.update("Products", values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
