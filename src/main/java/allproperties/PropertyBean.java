package allproperties;

public class PropertyBean {
    String mobileNumber, prop_name, address, size_dim, approved_by, price_demanded, other_info;
    byte[] pic1;

    public PropertyBean(String mobileNumber, String prop_name, String address, String size_dim, String approved_by, String price_demanded, String other_info, byte[] pic1) {
        this.mobileNumber = mobileNumber;
        this.prop_name = prop_name;
        this.address = address;
        this.size_dim = size_dim;
        this.approved_by = approved_by;
        this.price_demanded = price_demanded;
        this.other_info = other_info;
        this.pic1 = pic1;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getProp_name() {
        return prop_name;
    }

    public void setProp_name(String prop_name) {
        this.prop_name = prop_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSize_dim() {
        return size_dim;
    }

    public void setSize_dim(String size_dim) {
        this.size_dim = size_dim;
    }

    public String getApproved_by() {
        return approved_by;
    }

    public void setApproved_by(String approved_by) {
        this.approved_by = approved_by;
    }

    public String getPrice_demanded() {
        return price_demanded;
    }

    public void setPrice_demanded(String price_demanded) {
        this.price_demanded = price_demanded;
    }

    public String getOther_info() {
        return other_info;
    }

    public void setOther_info(String other_info) {
        this.other_info = other_info;
    }

    public byte[] getPic1() {
        return pic1;
    }

    public void setPic1(byte[] pic1) {
        this.pic1 = pic1;
    }
}
