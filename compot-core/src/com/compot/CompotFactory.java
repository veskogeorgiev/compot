/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
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
