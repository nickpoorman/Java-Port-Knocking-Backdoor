package teadatabasegui;

import java.util.HashMap;
import javax.swing.JComboBox;

/**
 *
 * @author nick
 */
public class CompanyID {

    private int companyID;
    private RatesPanel ratesPanel;
    private RatesArchivePanel ratesArchivePanel;
    private ContactPanel contactPanel;
    private PlansPanel plansPanel;
    private HashMap<Integer, JComboBox> dropDowns;

    public CompanyID(String companyID) {
        this.companyID = Integer.parseInt(companyID);
        this.dropDowns = new HashMap<Integer, JComboBox>();

    }

    public CompanyID(int companyID) {
        this.companyID = companyID;
    }

    public JComboBox getDropDownByID(Integer id) {
        return dropDowns.get(id);
    }

    public void addDropDown(Integer id, JComboBox dropDown) {
        this.dropDowns.put(id, dropDown);
    }

    /**
     * @return the companyID
     */
    public int getCompanyID() {
        return companyID;
    }

    /**
     * @param companyID the companyID to set
     */
    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public String getCompanyIDString() {
        return companyID + "";
    }

    public void setCompanyID(String companyID) {
        this.companyID = Integer.parseInt(companyID);
    }

    public boolean isNewCompany() {
        return companyID == -1;
    }

    /**
     * @return the ratesPanel
     */
    public RatesPanel getRatesPanel() {
        return ratesPanel;
    }

    /**
     * @param ratesPanel the ratesPanel to set
     */
    public void setRatesPanel(RatesPanel ratesPanel) {
        this.ratesPanel = ratesPanel;
    }

    /**
     * @return the ratesArchivePanel
     */
    public RatesArchivePanel getRatesArchivePanel() {
        return ratesArchivePanel;
    }

    /**
     * @param ratesArchivePanel the ratesArchivePanel to set
     */
    public void setRatesArchivePanel(RatesArchivePanel ratesArchivePanel) {
        this.ratesArchivePanel = ratesArchivePanel;
    }

    /**
     * @return the contactPanel
     */
    public ContactPanel getContactPanel() {
        return contactPanel;
    }

    /**
     * @param contactPanel the contactPanel to set
     */
    public void setContactPanel(ContactPanel contactPanel) {
        this.contactPanel = contactPanel;
    }

    /**
     * @return the plansPanel
     */
    public PlansPanel getPlansPanel() {
        return plansPanel;
    }

    /**
     * @param plansPanel the plansPanel to set
     */
    public void setPlansPanel(PlansPanel plansPanel) {
        this.plansPanel = plansPanel;
    }
}
