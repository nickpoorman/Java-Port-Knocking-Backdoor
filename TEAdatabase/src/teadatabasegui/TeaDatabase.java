package teadatabasegui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kendall and Chintan
 */
public class TeaDatabase {

    private static final String USER_NAME = "root";
    private static final String PASSWORD = "PASSWORD";
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String CONNECTION_URL = "jdbc:mysql://csahouse.homelinux.net:3306/tea_database";
    private static final Connection teaConnection;

    static {
        try {
            Class.forName(JDBC_DRIVER);
            teaConnection = DriverManager.getConnection(CONNECTION_URL, USER_NAME, PASSWORD);
        } catch (SQLException e) {
            throw new InternalError(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new InternalError("Could Not Find JDBC Driver Class");
        }
    }
    private static final TeaDatabase tea = new TeaDatabase();

    private TeaDatabase() {
    }

    public static String insertCompanyInfo(List<String> list) {
        StringBuffer insertStatement = new StringBuffer();
        String columns = "(company_name, contact_first_name, contact_last_name, contact_phone, contact_cell, contact_email, street, city, state, zip, alt_mailing_address, alt_city, alt_state, alt_zip, billing_address, billing_city, billing_state, billing_zip, billing_contact, billing_phone, billing_email)";
        buildCompanyInfoInsert(insertStatement, columns, list);
        TeaDatabase.tea.executeInsert(insertStatement.toString());
        return getNewestCompanyID();

    }

    private static String getNewestCompanyID() {
        return TeaDatabase.getQueryResults("SELECT company_id FROM companies ORDER BY company_id DESC ;").get(0);
    }

    private static void buildCompanyInfoInsert(StringBuffer insertStatement, String columns, List<String> values) {
        insertStatement.append("INSERT INTO companies");
        insertStatement.append(columns);
        insertStatement.append(" VALUES(");
        for (String s : values) {
            insertStatement.append("'");
            insertStatement.append(s);
            insertStatement.append("', ");
        }
        insertStatement.deleteCharAt(insertStatement.lastIndexOf(","));
        insertStatement.append(");");
    }

//    public static void updateCompanyInfo(String... companyInfo) {
//
//    }
    @SuppressWarnings("static-access")
    public static List<String> getCompanyInfo(String companyId) {
        StringBuffer select = new StringBuffer();
        select.append("SELECT * FROM companies WHERE company_id = ");
        select.append(companyId);
        select.append(";");
        System.out.println("Executing Select: ");
        System.out.println(select.toString());
        return TeaDatabase.tea.getQueryResults(select.toString());
    }

    @SuppressWarnings("static-access")
    public static List<String> getCompanyName(String companyId) {
        StringBuffer select = new StringBuffer();
        select.append("SELECT company_name FROM companies WHERE company_id = ");
        select.append(companyId);
        select.append(";");
        System.out.println("Executing Select: ");
        System.out.println(select.toString());
        return TeaDatabase.tea.getQueryResults(select.toString());
    }

    @SuppressWarnings("static-access")
    public static List<String> getPlanCodeFromPlanCodeID(String companyID, String planCodeId) {
        StringBuffer select = new StringBuffer();
        select.append("SELECT plan_code FROM rates WHERE plan_code_id = ");
        select.append(planCodeId);
        select.append(" AND company_id = ");
        select.append(companyID);
        select.append(";");
        System.out.println("Executing Select: ");
        System.out.println(select.toString());
        return TeaDatabase.tea.getQueryResults(select.toString());
    }

    public static void insertCompanyRates(List<String> list) {
        StringBuffer insertStatement = new StringBuffer();
        String columns = "(company_id, plan_code, s_field, two_p, e_ch, e_sp, f_field, contracts, renewal, filing, medical_or_dental)";
        insertStatement.append("INSERT INTO rates");
        insertStatement.append(columns);
        insertStatement.append(" VALUES(");
        insertStatement.append(list.get(0)).append(", ");
        for (int i = 1; i < 10; ++i) {
            insertStatement.append("'");
            insertStatement.append(list.get(i));
            insertStatement.append("', ");
        }
//        insertStatement.append(list.get(8)).append(", ");
//        insertStatement.append(list.get(9)).append(", ");
        insertStatement.append("'").append(list.get(10)).append("'");
        insertStatement.append(");");
        TeaDatabase.tea.executeInsert(insertStatement.toString());

    }

//    public static void updateCompanyRates(String... rates) {
//        StringBuffer update = new StringBuffer();
//    }
    public static List<String> getCompanyMedicalRates(String companyId) {
        return TeaDatabase.tea.getCompanyRates("medical", companyId);
    }

    public static List<String> getCompanyDentalRates(String companyId) {
        return TeaDatabase.tea.getCompanyRates("dental", companyId);
    }

    @SuppressWarnings("static-access")
    private List<String> getCompanyRates(String medicalOrDental, String companyId) {
        StringBuffer select = new StringBuffer();
        select.append("SELECT * FROM rates WHERE medical_or_dental = '");
        select.append(medicalOrDental);
        select.append("' AND company_id = ");
        select.append(companyId);
        select.append(";");
        System.out.println("Executing Select: ");
        System.out.println(select.toString());
        return TeaDatabase.tea.getQueryResults(select.toString());
    }

//    @SuppressWarnings("static-access")
//    public static List<String> getCompanyOtherRates(String companyId) {
//        StringBuffer select = new StringBuffer();
//        select.append("SELECT * FROM rates WHERE medical_or_dental != medical AND medical_or_dental != dental'");
//        select.append("' AND company_id = ");
//        select.append(companyId);
//        select.append(";");
//        System.out.println("Executing Select: ");
//        System.out.println(select.toString());
//        return TeaDatabase.tea.getQueryResults(select.toString());
//    }
//    public static void insertDetails(String... details) {
//
//    }
//    public static void updateDetails(String... details) {
//
//    }
//    public static List<String> getDetails(String planCodeId, String fieldId) {
//        return null;
//    }
    public static List<String> getQueryResults(String query) {
        ResultSet rs = null;
        List<String> results = new ArrayList<String>();
        try {
            Statement st = teaConnection.createStatement();
            rs = st.executeQuery(query);
            ResultSetMetaData data = rs.getMetaData();

            while (rs.next()) {
                for (int i = 1; i <= data.getColumnCount(); ++i) {
                    results.add(rs.getString(i));
                }
            }
        } catch (SQLException e) {
            throw new InternalError(e.getMessage());
        }
        return results;
    }

    private void executeInsert(String insertStatement) {
        System.out.println("Executing Insert: ");
        System.out.println(insertStatement);
        try {
            Statement insert = teaConnection.createStatement();
            insert.executeUpdate(insertStatement);
        } catch (SQLException e) {
            throw new InternalError(e.getMessage());
        }
    }

    public static List<List> getFieldNamesandValues(int company_id, int plan_code_id) {
        List<String> Names = new ArrayList<String>();
        List<String> Values = new ArrayList<String>();
        List<List> data = new ArrayList<List>();
        List<Integer> ID = new ArrayList<Integer>();
        try {
            Statement st = teaConnection.createStatement();
            String Querry = "select * from (select fields.field_id as " +
                    "id,field_name,field_value as value, rates_id from " +
                    "fields,field_values where fields.field_id = " +
                    "field_values.field_id) as tmp where rates_id =" + plan_code_id + ";";


            ResultSet rs = st.executeQuery(Querry);

            while (rs.next()) {
                Names.add(rs.getString("field_name"));
                Values.add(rs.getString("value"));
                ID.add(rs.getInt("id"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }


        data.add(ID);
        data.add(Names);
        data.add(Values);
        return data;
    }

    public static void insertDefaultFieldValues(int rates_id) {
        try {
            Statement st = teaConnection.createStatement();
            for (int i = 1; i < 11; i++) {
                String querry = "INSERT INTO field_values(field_id, field_value, rates_id) VALUES(" + i + ", 'None', " + rates_id + ");";
                st.executeUpdate(querry);
            }

        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static String[] getFieldNames(int rates_id) {

        String Querry = "select field_name " +
                "from fields " +
                "order by field_name;";
        List<String> result = TeaDatabase.getQueryResults(Querry);
        //System.out.println(result);
        String names[] = new String[result.size()];
        for (int i = 0; i < result.size(); i++) {
            names[i] = result.get(i);
        }
        return names;
    }

    public static void insertFieldValues(int rates_id, int field_id) {
        try {
            Statement st = teaConnection.createStatement();

            String querry = "INSERT INTO field_values(field_id, field_value, rates_id) VALUES(" + field_id + ", 'None', " + rates_id + ");";
            st.executeUpdate(querry);


        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void updateFieldValues(int fieldId, String value, int rates_id) {
        try {
            Statement st = teaConnection.createStatement();

            String Querry = "UPDATE field_values " +
                    "set field_value = '" + value + "' " +
                    "where field_id = " + fieldId + " AND rates_id =" + rates_id + " ;";

            st.executeUpdate(Querry);

        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static int getLatestSavedFieldID() {
        int id = 0;
        Statement st;
        try {
            st = teaConnection.createStatement();
            String Querry = "Select field_id from fields order by field_id desc ;";
            ResultSet rs = st.executeQuery(Querry);
            while (rs.next()) {
                id = rs.getInt("field_id");
                break;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }

        //System.out.println(id);
        return id;
    }

    public static List<List> getAllCompanyIDandNames() {
        List<String> CompanyNames = new ArrayList<String>();
        List<List> data = new ArrayList<List>();
        List<Integer> CompanyID = new ArrayList<Integer>();

        try {
            Statement st = teaConnection.createStatement();
            String Querry = "Select company_id,company_name from companies ORDER BY company_name;";
            ResultSet rs = st.executeQuery(Querry);

            while (rs.next()) {
                CompanyNames.add(rs.getString("company_name"));
                CompanyID.add(rs.getInt("company_id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }

        data.add(CompanyNames);
        data.add(CompanyID);
        return data;
    }

//    public static List<List> getAllCompanyIDandNamesForRegex(String word) {
//        List<String> CompanyNames = new ArrayList<String>();
//        List<List> data = new ArrayList<List>();
//        List<Integer> CompanyID = new ArrayList<Integer>();
//
//        try {
//            Statement st = teaConnection.createStatement();
//            //ORDER BY company_name
//            String Querry = "Select company_id,company_name from companies WHERE company_name REGEXP '[[:<:]]" + word + "[[:>:]]';";
//            ResultSet rs = st.executeQuery(Querry);
//
//            while (rs.next()) {
//                CompanyNames.add(rs.getString("company_name"));
//                CompanyID.add(rs.getInt("company_id"));
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        data.add(CompanyNames);
//        data.add(CompanyID);
//        return data;
//    }
    public static int insertField(String name) {

        int id = 0;
        try {
            Statement st = teaConnection.createStatement();

            String Querry1 = "Select count(*) as count from fields where field_name = '" + name + "' ;";
            int count = Integer.parseInt(TeaDatabase.tea.getQueryResults(Querry1).get(0));
            if (count == 0) {
                String Querry = "Insert into fields(field_name) values ('" + name + "');";
                st.executeUpdate(Querry);
            }
            id = Integer.parseInt(TeaDatabase.tea.getQueryResults("select field_id from fields where field_name = '" + name + "' ;").get(0));
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public static String getDropDownName(int id) {

        String DropDownName = null;
        try {
            Statement st = teaConnection.createStatement();
            String Querry = "Select drop_down_name " +
                    "from drop_downs " +
                    "Where drop_down_id = " + id + ";";
            ResultSet rs = st.executeQuery(Querry);

            while (rs.next()) {
                DropDownName = rs.getString("drop_down_name");
            }
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }

        return DropDownName;
    }

    public static List<String> getDropDownOptions(int id) {

        List<String> DropDownOptions = new ArrayList<String>();

        try {
            Statement st = teaConnection.createStatement();
            String Querry = "Select menu_option " +
                    "from drop_down_options " +
                    "Where drop_down_id = " + id + ";";
            ResultSet rs = st.executeQuery(Querry);

            while (rs.next()) {
                DropDownOptions.add(rs.getString("menu_option"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }

        return DropDownOptions;
    }

    public static List<List> getDropDownOptionsWithID(int id) {

        List<String> DropDownOptionsID = new ArrayList<String>();
        List<String> DropDownOptions = new ArrayList<String>();

        try {
            Statement st = teaConnection.createStatement();
            String Querry = "Select option_id,menu_option " +
                    "from drop_down_options " +
                    "Where drop_down_id = " + id + ";";
            ResultSet rs = st.executeQuery(Querry);

            while (rs.next()) {
                DropDownOptionsID.add(rs.getString("option_id"));
                DropDownOptions.add(rs.getString("menu_option"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }

        List<List> tmp = new ArrayList<List>();
        tmp.add(DropDownOptionsID);
        tmp.add(DropDownOptions);

        return tmp;
    }

    public static int getSavedOption(int dropdownid, int companyid) {
        //List<String> SavedOption = new ArrayList<String>();
        int selectedOption = -1;
        try {
            Statement st = teaConnection.createStatement();
            String Querry = "Select menu_option,option_id " +
                    "from drop_down_options " +
                    "where option_id in " +
                    "(Select option_id " +
                    "  from company_drop_downs" +
                    "  where drop_down_id = " + dropdownid + " AND " +
                    "  company_id = " + companyid + ")";

            ResultSet rs = st.executeQuery(Querry);

            while (rs.next()) {
//                SavedOption.add(rs.getString("menu_option"));
//                SavedOption.add(rs.getString("option_id"));
                selectedOption = rs.getInt("option_id");

            }
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }

        return selectedOption;

    }

    public static long getlongTime(String time) {
        long convertedTime = 0;

        DateFormat simpledate = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = simpledate.parse(time);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(date.getTime());
            convertedTime = cal.getTimeInMillis();

        } catch (ParseException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return convertedTime;
    }

    public static List<String> getAllNonExpiredDentalRates(int companyID) {
        List<String> data = new ArrayList<String>();
        try {
            Statement st = teaConnection.createStatement();
            String Querry = "Select * from rates where company_id = " + companyID + ";";
            ResultSet rs = st.executeQuery(Querry);
            List<Integer> id = new ArrayList<Integer>();
            while (rs.next()) {
                long time = TeaDatabase.getlongTime(rs.getString("renewal"));
                long currentTime = System.currentTimeMillis();
                if (time - currentTime > 3600000 * 24) {
                    id.add(rs.getInt("plan_code_id"));
                }
            }

            for (int i = 0; i < id.size(); i++) {
                List<String> tmp = TeaDatabase.getQueryResults("Select * from rates where plan_code_id = " + id.get(i) + " AND medical_or_dental = 'dental' ;");
                for (String s : tmp) {
                    data.add(s);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

    //each row is a new list
    public static List<String> getAllNonExpiredMedicalRates(int companyID) {
        List<String> data = new ArrayList<String>();
        try {
            Statement st = teaConnection.createStatement();
            String Querry = "Select * from rates where company_id = " + companyID + ";";
            ResultSet rs = st.executeQuery(Querry);
            List<Integer> id = new ArrayList<Integer>();
            while (rs.next()) {
                long time = TeaDatabase.getlongTime(rs.getString("renewal"));
                long currentTime = System.currentTimeMillis();
                if (time - currentTime > 3600000 * 24) {
                    id.add(rs.getInt("plan_code_id"));
                }
            }
            for (int i = 0; i < id.size(); i++) {
                List<String> tmp = TeaDatabase.getQueryResults("Select * from rates where plan_code_id = " + id.get(i) + " AND medical_or_dental = 'medical' ;");
                for (String s : tmp) {
                    data.add(s);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

    public static List<String> getAllNonExpiredOtherRates(int companyID) {
        List<String> data = new ArrayList<String>();
        try {
            Statement st = teaConnection.createStatement();
            String Querry = "Select * from rates where company_id = " + companyID + ";";
            ResultSet rs = st.executeQuery(Querry);
            List<Integer> id = new ArrayList<Integer>();
            while (rs.next()) {
                long time = TeaDatabase.getlongTime(rs.getString("renewal"));
                long currentTime = System.currentTimeMillis();
                if (time - currentTime > 3600000 * 24) {
                    id.add(rs.getInt("plan_code_id"));
                }
            }

            for (int i = 0; i < id.size(); i++) {
                List<String> tmp = TeaDatabase.getQueryResults("Select * from rates where plan_code_id = " + id.get(i) + " AND medical_or_dental != 'medical' AND medical_or_dental != 'dental';");
                for (String s : tmp) {
                    data.add(s);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;

    }

    public static List<String> getAllExpiredDentalRates(int companyID) {
        List<String> data = new ArrayList<String>();
        try {
            Statement st = teaConnection.createStatement();
            String Querry = "Select * from rates where company_id = " + companyID + ";";
            ResultSet rs = st.executeQuery(Querry);
            List<Integer> expiredid = new ArrayList<Integer>();
            while (rs.next()) {
                long time = TeaDatabase.getlongTime(rs.getString("renewal"));
                long currentTime = System.currentTimeMillis();
                if (time - currentTime < 3600000 * 24) {
                    expiredid.add(rs.getInt("plan_code_id"));
                }
            }

            for (int i = 0; i < expiredid.size(); i++) {
                List<String> tmp = TeaDatabase.getQueryResults("Select * from rates where plan_code_id = " + expiredid.get(i) + " AND medical_or_dental = 'dental' ;");
                for (String s : tmp) {
                    data.add(s);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

    public static List<String> getAllExpiredMedicalRates(int companyID) {
        List<String> data = new ArrayList<String>();
        try {
            Statement st = teaConnection.createStatement();
            String Querry = "Select * from rates where company_id = " + companyID + ";";
            ResultSet rs = st.executeQuery(Querry);
            List<Integer> expiredid = new ArrayList<Integer>();
            while (rs.next()) {
                long time = TeaDatabase.getlongTime(rs.getString("renewal"));
                long currentTime = System.currentTimeMillis();
                if (time - currentTime < 3600000 * 24) {
                    expiredid.add(rs.getInt("plan_code_id"));
                }
            }

            for (int i = 0; i < expiredid.size(); i++) {
                List<String> tmp = TeaDatabase.getQueryResults("Select * from rates where plan_code_id = " + expiredid.get(i) + " AND medical_or_dental = 'medical' ;");
                for (String s : tmp) {
                    data.add(s);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

    public static List<String> getAllExpiredOtherRates(int companyID) {
        List<String> data = new ArrayList<String>();
        try {
            Statement st = teaConnection.createStatement();
            String Querry = "Select * from rates where company_id = " + companyID + ";";
            ResultSet rs = st.executeQuery(Querry);
            List<Integer> expiredid = new ArrayList<Integer>();
            while (rs.next()) {
                long time = TeaDatabase.getlongTime(rs.getString("renewal"));
                long currentTime = System.currentTimeMillis();
                if (time - currentTime < 3600000 * 24) {
                    expiredid.add(rs.getInt("plan_code_id"));
                }
            }

            for (int i = 0; i < expiredid.size(); i++) {
                List<String> tmp = TeaDatabase.getQueryResults("Select * from rates where plan_code_id = " + expiredid.get(i) + " AND medical_or_dental != 'medical' AND  medical_or_dental != 'dental';");
                for (String s : tmp) {
                    data.add(s);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }

        return data;

    }

    public static void insertDropDownOption(int dropdownid, String MenuOption) {
        String Querry = "INSERT INTO drop_down_options(drop_down_id,menu_option) VALUES (" + dropdownid + ", '" + MenuOption + "');";
        Statement st;
        try {
            st = teaConnection.createStatement();
            st.executeUpdate(Querry);
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void updateDropDownsOptionName(int dropdownid, int optionid, String menuoption) {

        //drop_down_options(drop_down_id, menu_option)

        String Querry = "UPDATE drop_down_options " +
                " SET menu_option ='" + menuoption + "' " +
                " WHERE option_id ='" + optionid + "' AND drop_down_id =" + dropdownid + ";";

        try {
            Statement st = teaConnection.createStatement();
            st.executeUpdate(Querry);
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public static void deleteDropDownOption(int dropdownid, String menuOption) {
//        String Querry = "DELETE from drop_down_options where drop_down_id = " + dropdownid + " AND menu_option = '" + menuOption + "'";
//
//        Statement st;
//        try {
//            st = teaConnection.createStatement();
//            st.executeUpdate(Querry);
//        } catch (SQLException ex) {
//            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    public static void updateSavedDropDowns(int optionid, int dropdownid, int companyid) {
//        String Querry = "UPDATE company_drop_downs " +
//                " SET option_id =" + optionid + "," +
//                " drop_down_id =" + dropdownid +
//                " WHERE company_id = " + companyid + " ;";

        String Querry = "UPDATE company_drop_downs " +
                " SET option_id =" + optionid + " " +
                " WHERE company_id = " + companyid + " AND drop_down_id =" + dropdownid + ";";

        try {
            Statement st = teaConnection.createStatement();
            st.executeUpdate(Querry);
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void insertSelectedDropDownsOption(int optionid, int dropdownid, int companyid) {
        String Querry = "INSERT INTO company_drop_downs(option_id, drop_down_id, company_id) VALUES " +
                " (" + optionid + "," + dropdownid + "," + companyid + " );";

        try {
            Statement st = teaConnection.createStatement();
            st.executeUpdate(Querry);
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static List<String> getPlansForCompany(int companyid) {
        //MIGHT NEED TO REMOVE THE )
        return TeaDatabase.getQueryResults("SELECT * FROM plans WHERE company_id = " + companyid + ";");
    }

    public static List<String> getPlansClassInfoForCompany(int companyid, int classid) {
        return TeaDatabase.getQueryResults("SELECT * FROM plans_class_info WHERE company_id = " + companyid + " AND class_id = " + classid + ";");
    }

//                    /*New Stuff -->*/
//                CREATE TABLE IF NOT EXISTS plans(
//                company_id          INT NOT NULL,
//                union_yes_no        VARCHAR(10),
//                submitted           DATE,
//                initials            VARCHAR(10),
//                approval_date       DATE,
//                welcome_letter      DATE,
//                effective_date      DATE,
//                FOREIGN KEY(company_id) REFERENCES companies(company_id)
//                );
//
//                CREATE TABLE IF NOT EXISTS plans_class_info(
//                company_id          INT NOT NULL,
//                class_id            INT NOT NULL,
//                waiting_period      VARCHAR(50),
//                eligibles           INT,
//                FOREIGN KEY(company_id) REFERENCES companies(company_id),
//                FOREIGN KEY(class_id) REFERENCES drop_down_options(option_id)
//                );
    public static void insertPlansClassInfoForCompany(int companyid, int classid, String waitingPeriod, String eligibles) {

        String Querry = "INSERT INTO plans_class_info (company_id,class_id,waiting_period,eligibles) values ";
        StringBuffer querryvalues = new StringBuffer();
        querryvalues.append("(" + companyid + ",");
        querryvalues.append(classid + ",");
        querryvalues.append("'" + waitingPeriod + "',");
        querryvalues.append("'" + eligibles + "');");

        System.out.println(Querry + querryvalues.toString());
        try {
            Statement st = teaConnection.createStatement();
            st.executeUpdate(Querry + querryvalues.toString());
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*data size 6 order :- waiting_period,eligibles  */

    public static void updatePlansClassInfoForCompany(int companyid, int classid, String waitingPeriod, String eligibles) {
        StringBuffer sb = new StringBuffer();

        sb.append("UPDATE plans_class_info ");
        sb.append("SET ");
        sb.append("waiting_period = '" + waitingPeriod + "',");
        sb.append("eligibles = '" + eligibles + "' ");
        sb.append("WHERE company_id = " + companyid + " AND class_id = " + classid + ";");

        try {
            Statement st = teaConnection.createStatement();
            st.executeUpdate(sb.toString());
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println(sb.toString());
    }

    public static void insertPlansForNewCompany(int companyid) {

        String Querry = "INSERT INTO plans (company_id) values ";
        StringBuffer querryvalues = new StringBuffer();
        querryvalues.append("(" + companyid + ");");

        System.out.println(Querry + querryvalues.toString());
        try {
            Statement st = teaConnection.createStatement();
            st.executeUpdate(Querry + querryvalues.toString());
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void insertPlansForCompany(int companyid, String unionYesOrNo,
            String submitted, String initials,
            String approvalDate, String welcomeLetter,
            String effectiveDate) {

        String Querry = "INSERT INTO plans (company_id,union_yes_no,submitted,initials,approval_date,welcome_letter,effective_date) values ";
        StringBuffer querryvalues = new StringBuffer();
        querryvalues.append("(" + companyid + ",");
        querryvalues.append("'" + unionYesOrNo + "',");
        querryvalues.append("'" + submitted + "',");
        querryvalues.append("'" + initials + "',");
        querryvalues.append("'" + approvalDate + "',");
        querryvalues.append("'" + welcomeLetter + "',");
        querryvalues.append("'" + effectiveDate + "');");

        System.out.println(Querry + querryvalues.toString());
        try {
            Statement st = teaConnection.createStatement();
            st.executeUpdate(Querry + querryvalues.toString());
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /*data size 6 order :- union_yes_no,submitted,initials,approvaldate,welcomeletter,effictive date*/
    public static void updatePlansForCompany(int companyid, String unionYesOrNo,
            String submitted, String initials,
            String approvalDate, String welcomeLetter,
            String effectiveDate) {
        StringBuffer sb = new StringBuffer();

        sb.append("UPDATE plans ");
        sb.append("SET ");
        sb.append("union_yes_no = '" + unionYesOrNo + "',");
        ////////////
        if (submitted.equals("")) {
            sb.append("submitted = " + null + ",");
        } else {
            sb.append("submitted = '" + submitted + "',");
        }
        ////////////
        sb.append("initials = '" + initials + "',");
        if (approvalDate.equals("")) {
            sb.append("approval_date = " + null + ",");
        } else {
            sb.append("approval_date = '" + approvalDate + "',");
        }
        ////////////
        if (welcomeLetter.equals("")) {
            sb.append("welcome_letter = " + null + ",");
        } else {
            sb.append("welcome_letter = '" + welcomeLetter + "',");
        }
        ////////////
        if (effectiveDate.equals("")) {
            sb.append("effective_date = " + null + " ");
        } else {
            sb.append("effective_date = '" + effectiveDate + "' ");
        }
        ////////////
        sb.append("WHERE company_id = " + companyid + " ;");

        try {
            Statement st = teaConnection.createStatement();
            st.executeUpdate(sb.toString());
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println(sb.toString());
    }
    /*data size 10 order :- plan_code,s_field,two_p,e_ch ........ medical_or_dental*/

    public static void updateRatesForCompany(int companyid, int plancodeid, List<String> data) {
        StringBuffer sb = new StringBuffer();

        sb.append("UPDATE rates ");
        sb.append("SET ");
        sb.append("plan_code = '" + data.remove(0) + "',");
        sb.append("s_field = '" + data.remove(0) + "',");
        sb.append("two_p = '" + data.remove(0) + "',");
        sb.append("e_ch = '" + data.remove(0) + "',");
        sb.append("e_sp = '" + data.remove(0) + "',");
        sb.append("f_field = '" + data.remove(0) + "',");
        //sb.append("contracts = '" + data.remove(0) + "',");
        sb.append("renewal = '" + data.remove(0) + "',");
        sb.append("filing = '" + data.remove(0) + "', ");
        sb.append("medical_or_dental = '" + data.remove(0) + "' ");
        sb.append("WHERE company_id = " + companyid + " AND plan_code_id = " + plancodeid + " ;");

        try {
            Statement st = teaConnection.createStatement();
            st.executeUpdate(sb.toString());
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /* data size 21 order:- company_name,contact_first_name ........ billing_email*/
    public static void updateForCompanyDetails(int companyid, List<String> data) {
        StringBuffer sb = new StringBuffer();

        sb.append("UPDATE companies ");
        sb.append("SET ");
        sb.append("company_name = '" + data.remove(0) + "',");
        sb.append("contact_first_name = '" + data.remove(0) + "',");
        sb.append("contact_last_name  = '" + data.remove(0) + "',");
        sb.append("contact_phone = '" + data.remove(0) + "',");
        sb.append("contact_cell = '" + data.remove(0) + "',");
        sb.append("contact_email= '" + data.remove(0) + "',");
        sb.append("street = '" + data.remove(0) + "',");
        sb.append("city = '" + data.remove(0) + "',");
        sb.append("state = '" + data.remove(0) + "',");
        sb.append("zip = '" + data.remove(0) + "',");
        sb.append("alt_mailing_address = '" + data.remove(0) + "',");
        sb.append("alt_city = '" + data.remove(0) + "',");
        sb.append("alt_state = '" + data.remove(0) + "',");
        sb.append("alt_zip = '" + data.remove(0) + "',");
        sb.append("billing_address   = '" + data.remove(0) + "',");
        sb.append("billing_city   = '" + data.remove(0) + "',");
        sb.append("billing_state   = '" + data.remove(0) + "',");
        sb.append("billing_zip   = '" + data.remove(0) + "',");
        sb.append("billing_contact   = '" + data.remove(0) + "',");
        sb.append("billing_phone   = '" + data.remove(0) + "',");
        sb.append("billing_email   = '" + data.remove(0) + "' ");
        sb.append("WHERE company_id = " + companyid + " ;");

        try {
            Statement st = teaConnection.createStatement();
            st.executeUpdate(sb.toString());
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void deleteFieldNameValue(int fieldid, int rateid) {
        try {
            Statement st = teaConnection.createStatement();
            String Querry = "Delete from field_values where field_id = " + fieldid + " AND rates_id =" + rateid + " ;";

            st.executeUpdate(Querry);

        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static List<List<String>> getTasks(String date) {
    	String query = "select * from to_do_tasks where to_do_date = '" + date + "'";
    	List<String> results = TeaDatabase.getQueryResults(query);
    	List<String> tasks = new ArrayList<String>();
    	List<String> dates = new ArrayList<String>();
    	List<String> completed = new ArrayList<String>();
    	Iterator<String> it = results.iterator();
    	while (it.hasNext()) {
    		tasks.add(it.next());
    		dates.add(it.next());
    		completed.add(it.next());
    	}
    	List<List<String>> tmp = new ArrayList<List<String>>();
    	tmp.add(tasks);
    	tmp.add(dates);
    	tmp.add(completed);
    	return tmp;
    }
    
    public static void updateToDoList(String task, boolean completed) {
    	String query = "update to_do_tasks ";
        query += "set completed=true ";
        query += "where task='" + task + "'";
        System.out.println(query);
        try {
            Statement st = teaConnection.createStatement();
            st.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void insertIntoToDoList(String task, String date) {
    	String query = "insert into to_do_tasks";
    	query += " values('" + task + "', " + "'" + date + "', " + "false);";
    	try {
    		Statement st = teaConnection.createStatement();
    		st.executeUpdate(query);
    	}
    	catch (SQLException ex) {
    		Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
    	}
    	System.out.println(query);
    }

    //need a method to insert, update, and get company_owners based on company id
    //need a method to insert, update, and get group_numbers based on company_id
    //need to fix plans
    //need a method to insert, update, and get plans_class_info
    public static void insertCompanyOwner(int companyID, String ownerName, String percentOwnership) {
        String Querry = "INSERT INTO company_owners(company_id,owner_name,percent_ownership) VALUES (" + companyID + ", '" + ownerName + "', '" + percentOwnership + "');";
        Statement st;
        try {
            st = teaConnection.createStatement();
            st.executeUpdate(Querry);
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static List<String> getCompanyOwners(int companyID) {
        StringBuffer select = new StringBuffer();
        select.append("SELECT * FROM company_owners WHERE company_id = '");
        select.append(companyID);
        select.append("'");
        select.append(";");
        System.out.println("Executing Select: ");
        System.out.println(select.toString());
        return TeaDatabase.tea.getQueryResults(select.toString());
    }

    public static void updateCompanyOwner(int ownerID, String ownerName, String percentOwnership) {
        StringBuffer sb = new StringBuffer();

        sb.append("UPDATE company_owners ");
        sb.append("SET ");
        sb.append("owner_name = '" + ownerName + "',");
        sb.append("percent_ownership = '" + percentOwnership + "' ");
        sb.append("WHERE owner_id = " + ownerID + " ;");

        try {
            Statement st = teaConnection.createStatement();
            st.executeUpdate(sb.toString());
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void insertCompanyGroupNumber(int companyID, String groupNumber) {
        String Querry = "INSERT INTO group_numbers(company_id,group_number) VALUES (" + companyID + ", '" + groupNumber + "');";
        System.out.println("Querry: " + Querry);
        Statement st;
        try {
            st = teaConnection.createStatement();
            st.executeUpdate(Querry);
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static List<String> getCompanyGroupNumbers(int companyID) {
        StringBuffer select = new StringBuffer();
        select.append("SELECT * FROM group_numbers WHERE company_id = '");
        select.append(companyID);
        select.append("'");
        select.append(";");
        System.out.println("Executing Select: ");
        System.out.println(select.toString());
        return TeaDatabase.getQueryResults(select.toString());
    }

    public static void updateCompanyGroupNumber(int groupNumberID, String groupNumber) {
        StringBuffer sb = new StringBuffer();

        sb.append("UPDATE group_numbers ");
        sb.append("SET ");
        sb.append("group_number = '" + groupNumber + "' ");
        sb.append("WHERE group_number_id = " + groupNumberID + " ;");

        try {
            Statement st = teaConnection.createStatement();
            st.executeUpdate(sb.toString());
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public static List<List<String>> getCompanyFilesWithLabels(int companyID) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("select filelabel,filename ");
    	sb.append("from files,(select file_labels.label_id as labelid, ");
    	sb.append("file_labels.file_id as fileid, file_labels.file_label as filelabel, file_names.file_name as filename ");
    	sb.append("from file_labels,file_names ");
    	sb.append("where file_labels.label_id = file_names.label_id) as tmp ");
    	sb.append("where fileid = file_id AND ");
    	sb.append("company_id = ");
    	sb.append(companyID);
    	List<String> tmp = TeaDatabase.getQueryResults(sb.toString());
    	/* Get the labels and the names of files and store them in separate lists */  	
    	List<String> labels = new ArrayList<String>();
    	List<String> fileNames = new ArrayList<String>();
    	for (int i = 0; i < tmp.size(); ++i) {
    		if (i % 2 == 0) 
    			labels.add(tmp.get(i));
    		else 
    			fileNames.add(tmp.get(i));
    	}
    	tmp = null; //gc
    	List<List<String>> l = new ArrayList<List<String>>();
    	l.add(labels);
    	l.add(fileNames);
    	return l;

    }

	public static void insertLabelsIntoDatabase(int companyID, String labelName) {
		System.out.println("Inserting labels");
		try {
			List<Integer> fileId = new ArrayList<Integer>();
			Statement st = teaConnection.createStatement();
			String query = "Select file_id " + "from files "
					+ "Where company_id = " + companyID + " ;";
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				fileId.add(rs.getInt("file_id"));
			}
			if (fileId.size() != 0) {
				for (int id : fileId) {
					String insertQuery = "Insert INTO file_labels (file_label,file_id) "
							+ "values ( '" + labelName + "', " + id + " ) ;";
					st.executeUpdate(insertQuery);
				}

			}

		} catch (SQLException ex) {
			Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

    public static void updateLabelsIntoDatabase(String oldLabel, String newLabel) {
    	System.out.println("Updating labels");
    }

    public static void insertFilesIntoDatabase(String label, String fileName) {
        System.out.println("Inserting files into database...");
        String select = "select label_id from file_labels where file_label = '" + label + "';"; 
        System.out.println(select);
        int labelID = Integer.parseInt(TeaDatabase.getQueryResults(select).get(0));
        String query = "INSERT INTO file_names(label_id, file_name) VALUES (" + labelID + ", '" + fileName + "');";
        try {
            Statement st = teaConnection.createStatement();
            st.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void removeFileFromDatabase(String fileName) {
    	String delete = "delete from file_names where file_name='" + fileName + "'";
    	System.out.println(delete);
    	try {
    		Statement st = teaConnection.createStatement();
    		st.executeUpdate(delete);
    	}
    	catch (SQLException ex) {
    		Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
    	}
    }

    public static void main(String[] args) throws Exception {
        // TeaDatabase.insertPlansForCompany(22, null, null, null, null, null, null);
//        List<String> tmp = TeaDatabase.getPlansForCompany(22);
//        for (String s : tmp) {
//            if(s == null){
//                System.out.println("true");
//            }
//            System.out.println(s);
//        }
        //  TeaDatabase.insertPlansForNewCompany(21);
        //TeaDatabase.in
        // TeaDatabase.insertCompanyGroupNumber(22, "");
        // List<String> groupNumbers = TeaDatabase.getCompanyGroupNumbers(22);
        // TeaDatabase.updateCompanyGroupNumber(1, "Group number bla");

        /*TeaDatabase.getCompanyInfo("1");
        TeaDatabase.getCompanyMedicalRates("1");
        TeaDatabase.insertCompanyInfo("Magic Box", "Kendall", "Moore", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ");
        TeaDatabase.insertCompanyRates("1", "Test Plan Code", "nick@nickpoorman.com", "S", "2p", "e/ch", "e/sp", "F", "2", "sysdate()", "sysdate()", "medical");*/
//        System.out.println(TeaDatabase.getQueryResults("select * from drop_down_options ;"));
//        getNewestCompanyID();
        //System.out.println(TeaDatabase.getlongTime("2009-08-30"));
        // System.out.println(TeaDatabase.getQueryResults("Select company_id,company_name from companies ORDER BY company_name;"));
        // TeaDatabase.insertPlansForCompany(1, 10, "2009-04-07", "modha", "2009-05-10", "2009-05-20", "2009-06-01");
        //List a = new ArrayList();
        // getAllNonExpiredDentalRates(1);
        // insertSelectedDropDownsOption(4, 1, 1);
//        int tmp = getSavedOption(1, 1);
//        System.out.println("Saved option: " + tmp);
//              1  company_id,
//              2  plan_code,
//              4  s_field,
//              5  two_p,
//              6  e_ch,
//              7  e_sp,
//              8  f_field,
//              9  contracts,
//              10 renewal,
//              11 filing,
//              12 medical_or_dental
//        List<String> list = new LinkedList<String>();
//        list.add("1");
//        for (int i = 0; i < 10; i++) {
//            list.add(null);
//        }
////        insertCompanyRates(list);
//        TeaDatabase.insertCompanyRates("1", "Test Plan Code", "nick@nickpoorman.com", "S", "2p", "e/ch", "e/sp", "F", "2", "sysdate()", "sysdate()", "medical");
//    	for (List<String> list : TeaDatabase.getCompanyFilesWithLabels(1))
//    		System.out.println(list);
//    	System.out.println("Here");
    	TeaDatabase.getTasks("2009-11-09");
    }
}
