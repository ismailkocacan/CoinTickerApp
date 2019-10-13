package com.app.binancealarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

interface DbUpgrade {
    Boolean execute(SQLiteDatabase db);
}

abstract class AbstractEntity {
    private SQLiteDatabase db;
    private DbContext dbContext;

    protected AbstractEntity(Context context) {
        dbContext = new DbContext(context);
    }

    protected SQLiteDatabase getReadableDatabase() {
        setDb(dbContext.getReadableDatabase());
        return getDb();
    }

    protected SQLiteDatabase getWritableDatabase() {
        setDb(dbContext.getWritableDatabase());
        return getDb();
    }

    protected void closeDb() {
        //getDb().close();
    }

    protected SQLiteDatabase getDb() {
        return db;
    }

    protected void setDb(SQLiteDatabase db) {
        this.db = db;
    }

    protected int getRecordCount(){
        return 0;
    }
}

class Setting {
    private int id;
    private String keyName;
    private String value1;
    private String value2;
    private String value3;
    private String value4;

    public Setting() {

    }

    public Setting(Setting setting) {
        this.id = setting.getId();

        if (setting.getKeyName() != null) {
            this.keyName = setting.getKeyName();
        }
        if (setting.getValue1() != null){
            this.value1 = setting.getValue1();
        }
        if (setting.getValue2() != null){
            this.value2 = setting.getValue2();
        }
        if (setting.getValue3() != null){
            this.value3 = setting.getValue3();
        }
        if (setting.getValue4() != null){
            this.value4 = setting.getValue4();
        }
    }

    public Setting(Bundle bundle){
        if (bundle.containsKey("coinName")) {
            this.keyName = bundle.getString("coinName");
        }
        if (bundle.containsKey("operator")){
            this.value1 = bundle.getString("operator");
        }
        if (bundle.containsKey("alarmPriceType")) {
            this.value2 = bundle.getString("alarmPriceType");
        }
        if (bundle.containsKey("price")){
            String value = bundle.getString("price").trim();
            if (value.length() > 0) this.value3 = value;
        }
        if (bundle.containsKey("diff")){
            String value = bundle.getString("diff").trim();
            if (value.length() > 0) this.value4 = value;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }

    public String getValue4() {
        return value4;
    }

    public void setValue4(String value4) {
        this.value4 = value4;
    }
}

class DbUserSetting extends AbstractEntity {

    public final static String TABLE_SETTING ="USER_SETTING";

    public final static String RID   ="ID";
    public final static String KEY   ="KEY_NAME";
    public final static String VALUE ="KEY_VALUE";

    public DbUserSetting(Context context) {
        super(context);
    }


    public Boolean insert(String key,String value) {
        Boolean result = false;
        try {
            getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY, key);
            values.put(VALUE, value);
            getDb().insert(TABLE_SETTING, null, values);
            result = true;
        }catch (Exception e) {
            result = false;
        }
        closeDb();
        return result;
    }

    public String getValue(String key) {
        String result = "";
        try {
            getReadableDatabase();
            String countQuery = "SELECT "+VALUE+" FROM " + TABLE_SETTING+" where "+KEY+"='"+key+"'";
            Cursor cursor = getDb().rawQuery(countQuery, null);
            if (cursor != null) cursor.moveToFirst();
            result = cursor.getString(0);
            cursor.close();
            closeDb();
        } catch (Exception e) {
            result = "";
        }
        return result;
    }

    public void setValue(String key,String value) {
        Boolean exist = isKeyExist(key);
        if (exist) update(key,value);
        if (!exist) insert(key,value);
    }

    public Boolean update(String key,String value) {
        Boolean result = false;
        try {
            getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY, key);
            values.put(VALUE, value);
            int affectedRows = getDb().update(TABLE_SETTING, values, KEY + " = ?",new String[] { String.valueOf(key) });
            result = affectedRows == 1;
        }catch (Exception e) {
            result = false;
            App.log(e);
        }
        closeDb();
        return result;
    }

    public boolean isKeyExist(String key) {
        boolean result = false;
        Cursor cursor = null;
        try {
            getReadableDatabase();
            String query = "SELECT "+KEY+" FROM " + TABLE_SETTING+" where "+KEY+"='"+key+"'";
            cursor = getDb().rawQuery(query, null);
            result =  cursor.getCount() > 0;
        } catch (Exception e){
            result = false;
            App.log(e);
        }
        if (cursor != null)
            cursor.close();
        closeDb();
        return result;
    }

    public Boolean deleteById(int id) {
        boolean result = false;
        try {
            getWritableDatabase();
            int affectedRows = getDb().delete(TABLE_SETTING,  RID + " = ?",new String[]{
                    String.valueOf(id) });
            result = affectedRows == 1;
        }catch (Exception e){
            result = false;
            App.log(e);
        }
        closeDb();
        if (App.getAppTableChanged() != null)
            App.getAppTableChanged().onChanged(TABLE_SETTING);
        return result;
    }

    public static String getSqlDDLString() {
        StringBuilder sb = new StringBuilder();
        sb.append("create table "+TABLE_SETTING+"(");
        sb.append(String.format("%s integer primary key autoincrement,",RID));
        sb.append(String.format("%s TEXT null,", KEY));
        sb.append(String.format("%s TEXT null", VALUE));
        sb.append(")");
        return sb.toString();
    }
}

class DbSetting extends AbstractEntity {
    public final static String TABLE_SETTING ="SETTING";

    public final static String ID = "ID";
    public final static String KEY_NAME = "KEY_NAME";
    public final static String VALUE1 = "VALUE1";
    public final static String VALUE2 = "VALUE2";
    public final static String VALUE3 = "VALUE3";
    public final static String VALUE4 = "VALUE4";

    public final static int ID_INDEX = 0;      // id
    public final static int KEY_NAME_INDEX = 1; //coin name
    public final static int VALUE1_INDEX = 2;  // operator
    public final static int VALUE2_INDEX = 3;  // alarmPriceType
    public final static int VALUE3_INDEX = 4;
    public final static int VALUE4_INDEX = 5;


    public DbSetting(Context context) {
        super(context);
    }

    public Boolean insert(Setting setting) {
        try {
            getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, setting.getKeyName().trim());
            if (setting.getValue1() != null) values.put(VALUE1, setting.getValue1().trim());
            if (setting.getValue2() != null) values.put(VALUE2, setting.getValue2().trim());
            if (setting.getValue3() != null) values.put(VALUE3, setting.getValue3().trim());
            if (setting.getValue4() != null) values.put(VALUE4, setting.getValue4().trim());
            long id = getDb().insert(TABLE_SETTING, null, values);
            setting.setId((int)id);
            closeDb();
            if (App.getAppTableChanged() != null)
               App.getAppTableChanged().onChanged(TABLE_SETTING);
            return true;
        }catch (Exception e) {
            App.log(e);
            return false;
        }
    }

    public Setting getValue(String key) {
        Setting setting = new Setting();
        try {
            getReadableDatabase();
            String countQuery = "SELECT ID,KEY_NAME,VALUE1,VALUE2,VALUE3,VALUE4 FROM " + TABLE_SETTING+" where KEY_NAME='"+key+"'";
            Cursor cursor = getDb().rawQuery(countQuery, null);
            if (cursor != null) {
                cursor.moveToFirst();
                setting.setId(cursor.getInt(0));
                setting.setKeyName(cursor.getString(1));
                setting.setValue1(cursor.getString(2));
                setting.setValue2(cursor.getString(3));
                setting.setValue3(cursor.getString(4));
                setting.setValue4(cursor.getString(5));
            }
            cursor.close();
            closeDb();
        } catch (Exception e) {
            App.showMessage(e.getMessage());
        }
        return setting;
    }

    public void setValue(String key,String value1) {
        if (isKeyExist(key)) {
            Setting setting = new Setting();
            setting.setKeyName(key);
            setting.setValue1(value1);
            update(setting);
        } else {
            Setting setting = new Setting();
            setting.setKeyName(key);
            insert(setting);
        }
    }

    public void setValue(Setting setting) {
        if (isKeyExist(setting.getKeyName())) {
            update(setting);
        } else {
            insert(setting);
        }
    }

    public Boolean update(Setting setting) {
        try {
            getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, setting.getKeyName().trim());
            if (setting.getValue1()!=null) values.put(VALUE1, setting.getValue1());
            if (setting.getValue2()!=null) values.put(VALUE2, setting.getValue2());
            if (setting.getValue3()!=null) values.put(VALUE3, setting.getValue3());
            if (setting.getValue4()!=null) values.put(VALUE4, setting.getValue4());
            int result = getDb().update(TABLE_SETTING, values, KEY_NAME + " = ?",new String[]{
                    String.valueOf(setting.getKeyName().trim()) });
            closeDb();
            return result == 1;
        }catch (Exception e) {
            return false;
        }
    }

    public Boolean deleteById(int id) {
        boolean result = false;
        try {
            getWritableDatabase();
            int affectedRows = getDb().delete(TABLE_SETTING,  ID + " = ?",new String[]{
                    String.valueOf(id) });
            result = affectedRows == 1;
            if (result){
                if (App.getAppTableChanged() != null)
                    App.getAppTableChanged().onChanged(TABLE_SETTING);
            }
        }catch (Exception e){
            App.log(e);
        }
        closeDb();
        return result;
    }

    public boolean isKeyExist(String key) {
        int result = 0;
        try {
            getReadableDatabase();
            String countQuery = "SELECT "+ KEY_NAME +" FROM " + TABLE_SETTING+" where KEY_NAME='"+key.trim()+"'";
            Cursor cursor = getDb().rawQuery(countQuery, null);
            result =  cursor.getCount();
            cursor.close();
            closeDb();
        } catch (Exception e) {
        }
        return result > 0;
    }

    public List<AlarmRuleLastOrder> getListAlarmRuleLastOrder(String symbol){
        List<AlarmRuleLastOrder> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            getReadableDatabase();
            String query = "SELECT ID,KEY_NAME,VALUE1,VALUE2,VALUE3,VALUE4 FROM " + TABLE_SETTING+
                    " WHERE KEY_NAME='"+ symbol.trim()+
                    "' AND VALUE2='"+AlarmPriceType.LAST_ORDER.name().trim()+"'";
            cursor = getDb().rawQuery(query, null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    AlarmRuleLastOrder alarmRule = new AlarmRuleLastOrder();
                    alarmRule.setId(cursor.getInt(ID_INDEX));
                    alarmRule.setSymbol(cursor.getString(KEY_NAME_INDEX).trim());
                    alarmRule.setAlarmOperator(AlarmOperator.valueOf(cursor.getString(VALUE1_INDEX).trim()));
                    alarmRule.setAlarmPriceType(AlarmPriceType.valueOf(cursor.getString(VALUE2_INDEX).trim()));
                    if (!cursor.isNull(VALUE3_INDEX)){
                        String value = cursor.getString(VALUE3_INDEX).trim();
                        if (value.length() > 0) alarmRule.setValueAsDouble(Double.parseDouble(value));
                    }
                    if (!cursor.isNull(VALUE4_INDEX)) {
                        String fark = cursor.getString(VALUE4_INDEX).trim();
                        if (fark.length() > 0) alarmRule.setPriceChange(Double.parseDouble(fark));
                    }
                    list.add(alarmRule);
                    cursor.moveToNext();
                }
            }
        }finally {
            cursor.close();
            closeDb();
        }
        return list;
    }

    public List<AlarmRule> getListAlarmRule(String symbol, AlarmPriceType alarmPriceType){
        List<AlarmRule> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            getReadableDatabase();
            String query = "SELECT ID,KEY_NAME,VALUE1,VALUE2,VALUE3,VALUE4 FROM " + TABLE_SETTING+
                    " WHERE KEY_NAME='"+ symbol.trim()+
                    "' AND VALUE2='"+alarmPriceType.name().trim()+"'";
            cursor = getDb().rawQuery(query, null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    AlarmRule alarmRule = null;
                    switch (alarmPriceType) {
                        case LAST_ORDER: {
                            alarmRule = new AlarmRuleLastOrder();
                            if (!cursor.isNull(VALUE3_INDEX)){
                                String value = cursor.getString(VALUE3_INDEX).trim();
                                if (value.length() > 0)
                                    alarmRule.setValueAsDouble(Double.parseDouble(value));
                            }
                            if (!cursor.isNull(VALUE4_INDEX)) {
                                String fark = cursor.getString(VALUE4_INDEX).trim();
                                if (fark.length() > 0)
                                    ((AlarmRuleLastOrder) alarmRule).setPriceChange(Double.parseDouble(fark));
                            }
                            break;
                        }
                        case BUY: {
                            alarmRule = new AlarmRuleBuy();
                            if (!cursor.isNull(VALUE3_INDEX)){
                                String value = cursor.getString(VALUE3_INDEX).trim();
                                if (value.length() > 0)
                                    alarmRule.setValueAsDouble(Double.parseDouble(value));
                            }
                            break;
                        }
                        case ASK: {
                            alarmRule = new AlarmRuleAsk();
                            if (!cursor.isNull(VALUE3_INDEX)){
                                String value = cursor.getString(VALUE3_INDEX).trim();
                                if (value.length() > 0)
                                    alarmRule.setValueAsDouble(Double.parseDouble(value));
                            }
                            break;
                        }
                        case HIGH_LOW: {
                            alarmRule = new AlarmRuleHighLow();
                            if (!cursor.isNull(VALUE3_INDEX)){
                                String value = cursor.getString(VALUE3_INDEX).trim();
                                if (value.length() > 0)
                                    alarmRule.setValueAsBool(Boolean.parseBoolean(value));
                            }
                            break;
                        }
                    }
                    alarmRule.setId(Integer.parseInt(cursor.getString(ID_INDEX)));
                    alarmRule.setSymbol(cursor.getString(KEY_NAME_INDEX).trim());
                    alarmRule.setAlarmOperator(AlarmOperator.valueOf(cursor.getString(VALUE1_INDEX).trim()));
                    alarmRule.setAlarmPriceType(AlarmPriceType.valueOf(cursor.getString(VALUE2_INDEX).trim()));
                    list.add(alarmRule);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e){
            App.log(e);
        }
        if (cursor != null) cursor.close();
        closeDb();
        return list;
    }

    public AlarmRuleMap getListAlarm(){
        Cursor cursor = null;
        String query;
        AlarmRuleMap map = new AlarmRuleMap();
        try {
            getReadableDatabase();
            query = "SELECT ID,KEY_NAME,VALUE1,VALUE2,VALUE3,VALUE4 FROM " + TABLE_SETTING;
            cursor = getDb().rawQuery(query, null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String symbolName = cursor.getString(KEY_NAME_INDEX).trim();
                    if (!map.containsKey(symbolName)){
                        map.put(symbolName,new AlarmRules());
                    }
                    AlarmRule alarmRule = null;
                    AlarmPriceType alarmPriceType = AlarmPriceType.valueOf(cursor.getString(VALUE2_INDEX).trim());
                    switch (alarmPriceType) {
                        case LAST_ORDER: {
                            alarmRule = new AlarmRuleLastOrder();
                            if (!cursor.isNull(VALUE3_INDEX)){
                                String value = cursor.getString(VALUE3_INDEX).trim();
                                if (value.length() > 0)
                                    alarmRule.setValueAsDouble(Double.parseDouble(value));
                            }
                            if (!cursor.isNull(VALUE4_INDEX)){
                                String fark = cursor.getString(VALUE4_INDEX).trim();
                                if (fark.length() > 0)
                                    ((AlarmRuleLastOrder)alarmRule).setPriceChange(Double.parseDouble(fark));
                            }
                            break;
                        }
                        case BUY: {
                            alarmRule = new AlarmRuleBuy();
                            if (!cursor.isNull(VALUE3_INDEX)){
                                String value = cursor.getString(VALUE3_INDEX).trim();
                                if (value.length() > 0)
                                    alarmRule.setValueAsDouble(Double.parseDouble(value));
                            }
                            break;
                        }
                        case ASK: {
                            alarmRule = new AlarmRuleAsk();
                            if (!cursor.isNull(VALUE3_INDEX)){
                                String value = cursor.getString(VALUE3_INDEX).trim();
                                if (value.length() > 0)
                                    alarmRule.setValueAsDouble(Double.parseDouble(value));
                            }
                            break;
                        }
                        case HIGH_LOW: {
                            alarmRule = new AlarmRuleHighLow();
                            if (!cursor.isNull(VALUE3_INDEX)){
                                String value = cursor.getString(VALUE3_INDEX).trim();
                                if (value.length() > 0)
                                    alarmRule.setValueAsBool(Boolean.parseBoolean(value));
                            }
                            break;
                        }
                    }
                    alarmRule.setId(Integer.parseInt(cursor.getString(ID_INDEX)));
                    alarmRule.setAlarmPriceType(alarmPriceType);
                    alarmRule.setSymbol(symbolName);
                    alarmRule.setAlarmOperator(AlarmOperator.valueOf(cursor.getString(VALUE1_INDEX).trim()));
                    map.get(symbolName).add(alarmRule);
                    cursor.moveToNext();
                }
            }

        } catch (Exception e){
            App.log(e);
        }
        if (cursor != null) cursor.close();
        closeDb();
        return map;
    }

    @Override
    protected int getRecordCount() {
        int result = 0;
        Cursor cursor = null;
        try {
            getReadableDatabase();
            String countQuery = "SELECT count(*) as totalrows FROM " + TABLE_SETTING;
            cursor = getDb().rawQuery(countQuery, null);
            if (cursor != null) {
                if (cursor.moveToFirst()){
                    result = cursor.getInt(0);
                }
            }

        } catch (Exception e) {
            App.log(e);
        }
        if (cursor != null)
            cursor.close();
        closeDb();
        return result;
    }

    public Boolean post(Setting setting, Map<String,String> whereArgs) {
        Boolean result;
        try {
            getWritableDatabase();
            if (whereArgs != null){
                ContentValues values = new ContentValues();
                if (setting.getKeyName() != null) values.put(KEY_NAME, setting.getKeyName().trim());
                if (setting.getValue1() != null) values.put(VALUE1, setting.getValue1().trim());
                if (setting.getValue2() != null) values.put(VALUE2, setting.getValue2().trim());
                if (setting.getValue3() != null) values.put(VALUE3, setting.getValue3().trim());
                if (setting.getValue4() != null) values.put(VALUE4, setting.getValue4());

                int index = 0;
                String whereClause = "";
                String[] args = new String[whereArgs.size()];
                for (Map.Entry<String, String> entry : whereArgs.entrySet()) {
                    whereClause += entry.getKey() + " = ? and ";
                    args[index] = entry.getValue();
                    index++;
                }
                whereClause = whereClause.substring(0,whereClause.length()-4);
                int affectedRows = getDb().update(TABLE_SETTING, values, whereClause,args);
                result = affectedRows == 1;
                if (result) App.log("Kayıt Güncellendi");
                if (affectedRows == 0){
                    result = insert(setting);
                    if (result = true) App.log("Kayıt Eklendi.");
                }
            }else{
                result = insert(setting);
            }
        }catch (Exception e) {
            result = false;
            App.log(e);
        }
        closeDb();
        return result;
    }

    public static String getSqlDDLString() {
        StringBuilder sb = new StringBuilder();
        sb.append("create table "+TABLE_SETTING+"(");
        sb.append(String.format("%s integer primary key autoincrement,", ID));
        sb.append(String.format("%s TEXT null,", KEY_NAME));
        sb.append(String.format("%s TEXT null,", VALUE1));
        sb.append(String.format("%s TEXT null,", VALUE2));
        sb.append(String.format("%s TEXT null,", VALUE3));
        sb.append(String.format("%s TEXT null", VALUE4));
        sb.append(")");
        return sb.toString();
    }
}

public class DbContext extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "binance.db";

    private Context context;
    private HashMap<Integer, DbUpgrade> upgrades;

    public DbContext(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.upgrades = new HashMap<Integer, DbUpgrade>();
        App.log("DbContext create edildi");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(DbSetting.getSqlDDLString());
            db.execSQL(DbUserSetting.getSqlDDLString());
            App.log("Veritabanı Kuruldu.");
        } catch (Exception e) {
            App.log("DatabaseContext onCreate method çağrıldı");
            App.showMessage("Veritabanı Oluşturulamadı. \n"+e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            DbUpgrade dbUpgrade = upgrades.get(newVersion);
            if (dbUpgrade==null) return;
            Boolean result = dbUpgrade.execute(db);
            if (result) App.showMessage("Veritabanı sürümü yükseltildi.");
            if (!result) App.showMessage("Veritabanı güncellenemedi! \n ("+newVersion+") Nolu güncelleme hatalı !");
        }
    }

    public static String getDbVersion(Context context) {
        int result = 0;
        SQLiteDatabase db = null;
        try {
            DbContext dbContext = new DbContext(context);
            db = dbContext.getReadableDatabase();
            result = db.getVersion();
        } catch (Exception e) {
            result = 0;
        }
        db.close();
        return Integer.toString(result);
    }

}
