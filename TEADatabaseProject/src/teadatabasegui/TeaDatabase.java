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
import javax.swing.JOptionPane;

/**
 *
 * @author Nick and Chintan
 */
public class TeaDatabase {

    private static final String USER_NAME = MainWindow.getConf().getDATABASE_USER_NAME();
    private static final String PASSWORD = MainWindow.getConf().getDATABASE_PASSWORD();
    private static final String JDBC_DRIVER = MainWindow.getConf().getJDBC_DRIVER();
    private static final String CONNECTION_URL = MainWindow.getConf().getDATABASE_CONNECTION_URL();
    private static final Connection teaConnection;

    static {
        try {
            Class.forName(JDBC_DRIVER);
            teaConnection = DriverManager.getConnection(CONNECTION_URL, USER_NAME, PASSWORD);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "The connection to the database could not be established.", "Connection Error", JOptionPane.ERROR_MESSAGE);
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

    @SuppressWarnings("static-access")
    public static List<String> getCompanyInfo(String companyId) {
        StringBuffer select = new StringBuffer();
        select.append("SELECT * FROM companies WHERE company_id = ");
        select.append(companyId);
        select.append(";");
        return TeaDatabase.tea.getQueryResults(select.toString());
    }

    @SuppressWarnings("static-access")
    public static List<String> getCompanyName(String companyId) {
        StringBuffer select = new StringBuffer();
        select.append("SELECT company_name FROM companies WHERE company_id = ");
        select.append(companyId);
        select.append(";");
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
        insertStatement.append("'").append(list.get(10)).append("'");
        insertStatement.append(");");
        TeaDatabase.tea.executeInsert(insertStatement.toString());

    }

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
        return TeaDatabase.tea.getQueryResults(select.toString());
    }

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

    public static void updateSavedDropDowns(int optionid, int dropdownid, int companyid) {
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
        return TeaDatabase.getQueryResults("SELECT * FROM plans WHERE company_id = " + companyid + ";");
    }

    public static List<String> getPlansClassInfoForCompany(int companyid, int classid) {
        return TeaDatabase.getQueryResults("SELECT * FROM plans_class_info WHERE company_id = " + companyid + " AND class_id = " + classid + ";");
    }

    public static void insertPlansClassInfoForCompany(int companyid, int classid, String waitingPeriod, String eligibles) {
        String Querry = "INSERT INTO plans_class_info (company_id,class_id,waiting_period,eligibles) values ";
        StringBuffer querryvalues = new StringBuffer();
        querryvalues.append("(" + companyid + ",");
        querryvalues.append(classid + ",");
        querryvalues.append("'" + waitingPeriod + "',");
        querryvalues.append("'" + eligibles + "');");
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
    }

    public static void insertPlansForNewCompany(int companyid) {
        String Querry = "INSERT INTO plans (company_id) values ";
        StringBuffer querryvalues = new StringBuffer();
        querryvalues.append("(" + companyid + ");");
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

    public static List<CalendarTask> getToDoList(String date) {
        StringBuffer sb = new StringBuffer();

        sb.append("SELECT * FROM calendar ");
        sb.append("WHERE ");
        sb.append("start_date < '" + date + "' ");
        sb.append("AND ");
        sb.append("completed_date is NULL ");
        sb.append("\n UNION SELECT * FROM calendar ");
        sb.append("WHERE ");
        sb.append("start_date = '" + date + "' ");
        sb.append("\n UNION SELECT * FROM calendar ");
        sb.append("WHERE ");
        sb.append("completed_date = '" + date + "';");
        try {
            Statement st = teaConnection.createStatement();
            ResultSet rs = st.executeQuery(sb.toString());

            List<CalendarTask> tasks = new ArrayList<CalendarTask>();
            while (rs.next()) {
                CalendarTask t = new CalendarTask();
                t.setTask_id(rs.getInt("task_id"));
                t.setTask(rs.getString("task"));
                t.setCompany_name(rs.getString("company_name"));
                t.setStart_date(rs.getString("start_date"));
                t.setCompleted_date(rs.getString("completed_date"));
                tasks.add(t);
            }
            return tasks;
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void insertCalendarTask(String task, String companyName, String startDate) {
        StringBuffer sb = new StringBuffer();

        sb.append("INSERT INTO calendar (");
        sb.append("task, company_name, start_date");
        sb.append(") VALUES(");
        sb.append("'" + task + "', ");
        sb.append("'" + companyName + "', ");
        sb.append("'" + startDate + "');");

        Statement st;
        try {
            st = teaConnection.createStatement();
            st.executeUpdate(sb.toString());
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void updateCalendarTask(int taskID, String task, String companyName, String startDate, String completedDate) {
        StringBuffer sb = new StringBuffer();

        sb.append("UPDATE calendar ");
        sb.append("SET ");
        sb.append("task = '" + task + "', ");
        sb.append("company_name = '" + companyName + "', ");
        if (completedDate != null) {
            sb.append("completed_date = '" + completedDate + "', ");
        }
        sb.append("start_date = '" + startDate + "' ");
        sb.append("WHERE task_id = " + taskID + ";");

        try {
            Statement st = teaConnection.createStatement();
            st.executeUpdate(sb.toString());
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static List<CalendarTask> getAllTasksFromCalendar() {
        String query = "SELECT * FROM calendar;";
        try {
            Statement st = teaConnection.createStatement();
            ResultSet rs = st.executeQuery(query);

            List<CalendarTask> tasks = new ArrayList<CalendarTask>();
            while (rs.next()) {
                CalendarTask t = new CalendarTask();
                t.setTask_id(rs.getInt("task_id"));
                t.setTask(rs.getString("task"));
                t.setCompany_name(rs.getString("company_name"));
                t.setStart_date(rs.getString("start_date"));
                t.setCompleted_date(rs.getString("completed_date"));
                tasks.add(t);
            }
            return tasks;
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

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
        sb.append("select file_label,file_name ");
        sb.append("from files ");
        sb.append("where company_id = " + companyID + " ;");
        List<String> tmp = TeaDatabase.getQueryResults(sb.toString());
        /* Get the labels and the names of files and store them in separate lists */
        List<String> labels = new ArrayList<String>();
        List<String> fileNames = new ArrayList<String>();
        for (int i = 0; i < tmp.size(); ++i) {
            if (i % 2 == 0) {
                labels.add(tmp.get(i));
            } else {
                fileNames.add(tmp.get(i));
            }
        }
        tmp = null; //gc
        List<List<String>> l = new ArrayList<List<String>>();
        l.add(labels);
        l.add(fileNames);
        return l;

    }

    public static void insertFilesIntoDatabase(int companyID, String label, String fileName) {
        if ((label.trim().equals("")) || (fileName.trim().equals(""))) {
            return;
        }
        String query1 = "Select count(*) from files where company_id = " + companyID + " AND file_name = '" + fileName + "' ;";
        int count = 0;
        try {
            Statement st = teaConnection.createStatement();
            ResultSet rs = st.executeQuery(query1);
            while (rs.next()) {
                count = rs.getInt("count(*)");
                break;
            }
            String updateQuery = "UPDATE files set file_name = '" + fileName + "' , file_label = '" + label + "' Where company_id = " + companyID + " AND file_name =  '" + fileName + "' ;";
            String insertQuery = "INSERT INTO files(company_id,file_label, file_name) VALUES (" + companyID + ", '" + label + "' , '" + fileName + "');";
            if (count == 0) {
                st.executeUpdate(insertQuery);
            } else {
                st.executeUpdate(updateQuery);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void removeFileFromDatabase(int companyID, String fileName) {
        String delete = "delete from files where file_name='" + fileName + "' AND company_id = '" + companyID + "';";
        try {
            Statement st = teaConnection.createStatement();
            st.executeUpdate(delete);
        } catch (SQLException ex) {
            Logger.getLogger(TeaDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
