package com.fireblend.uitest.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private Dao<Contactosbd, Integer> mUsuarioDao = null;

    public DatabaseHelper(Context context) {
        super(context, "ormlite.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db,
                         ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Contactosbd.class);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Contactosbd.class, true);

            onCreate(db, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dao<Contactosbd, Integer> getUsuarioDao() throws SQLException {
        if (mUsuarioDao == null) {
            mUsuarioDao = getDao(Contactosbd.class);
        }
        return mUsuarioDao;
    }
    @Override
    public void close() {
        mUsuarioDao = null;
        super.close();
    }
}
