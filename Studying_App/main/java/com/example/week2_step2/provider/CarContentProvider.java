package com.example.week2_step2.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class CarContentProvider extends ContentProvider {

    public static final String CONTENT_AUTHORITY = "fit2081.app.Yeonsoo";

    public static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    CarDatabase mCarDatabase;

    public CarContentProvider() {
    }

    @Override
    public boolean onCreate() {
        mCarDatabase = CarDatabase.getDatabase(getContext());
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deletionCount;

        deletionCount = mCarDatabase
                .getOpenHelper()
                .getWritableDatabase()
                .delete(Car.TABLE_NAME, selection, selectionArgs);

        return deletionCount;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long rowId = mCarDatabase
                .getOpenHelper()
                .getWritableDatabase()
                .insert(Car.TABLE_NAME, 0, values);

        return ContentUris.withAppendedId(CONTENT_URI, rowId);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(Car.TABLE_NAME);
        String query = builder.buildQuery(projection, selection, null, null, sortOrder, null);

        final Cursor cursor = mCarDatabase
                .getOpenHelper()
                .getReadableDatabase()
                .query(query, selectionArgs);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int updateCount;
        updateCount = mCarDatabase
                .getOpenHelper()
                .getWritableDatabase()
                .update(Car.TABLE_NAME, 0, values, selection, selectionArgs);

        return updateCount;
    }
}