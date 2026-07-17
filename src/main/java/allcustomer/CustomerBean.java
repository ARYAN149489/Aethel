package allcustomer;

public class CustomerBean {
    String mobileNumber, name, address, city, email, type;
    byte[] profilePic;

    public CustomerBean(String mobileNumber, String name, String address, String city, String email, String type, byte[] profilePic) {
        this.mobileNumber = mobileNumber;
        this.name = name;
        this.address = address;
        this.city = city;
        this.email = email;
        this.type = type;
        this.profilePic = profilePic;
    }

    public byte[] getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
