package com.compot;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.compot.dao.TypeManager;
import com.compot.dao.internal.DAOFactory;
import com.compot.dao.internal.TypeManagerImpl;
import com.compot.model.Metamodel;
import com.compot.model.MetamodelFactory;
import com.compot.model.TableManager;
import com.compot.sql.CreateStatement;

/**
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class CompotFactory extends SQLiteOpenHelper {

	private TypeManager typeManager = new TypeManagerImpl();
	private DAOFactory daoFactory = new DAOFactory(typeManager);
	private TableManager tableManager = new TableManager(typeManager);

	private Compot compot;

	public CompotFactory(Context context, String name) {
		this(context, name, null, 1);
	}

	public CompotFactory(Context context, String name, CursorFactory cursorFactory, int version) {
		super(context, name, cursorFactory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		registerEntities(db);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		//
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//
	}

	public Compot getCompot() {
		if (compot == null) {
			compot = new Compot(daoFactory, getWritableDatabase());
		}
		return compot;
	}

	public void close() {
		getWritableDatabase().close();
	}

	public String getDatabasePath() {
		return getReadableDatabase().getPath();
	}

	@SuppressWarnings("unused")
	private void dropEntities(SQLiteDatabase db) throws InvalidModelException {
		StringBuilder query = new StringBuilder();
		for (Metamodel<?> mm : MetamodelFactory.getMetamodels()) {
			query.append("DROP TABLE IF EXISTS ").append(mm.getTableName()).append(";");
		}
		System.out.println(query);
		db.execSQL(query.toString());
	}

	private void registerEntities(SQLiteDatabase db) {
		// TODO see why batch execute does not work
//		StringBuilder query = new StringBuilder();
		for (Metamodel<?> mm : MetamodelFactory.getMetamodels()) {
			CreateStatement stm = tableManager.getCreateStatement(mm);
//			query.append(stm.toString());
			db.execSQL(stm.toString());
		}
//		System.out.println(query);
	}
}
