package alldeals;

public class DealBean {
    String buyerMobile, buyerName, sellerMobile, sellerName, propertyName, finalAmount, myCommission, amountLeft, commissionLeft, registryDate, advGivenDate, otherInfo, dealStatus;

    public DealBean(String buyerMobile, String buyerName, String sellerMobile, String sellerName, String propertyName, String finalAmount, String myCommission, String amountLeft, String commissionLeft, String registryDate, String advGivenDate, String otherInfo, String dealStatus) {
        this.buyerMobile = buyerMobile;
        this.buyerName = buyerName;
        this.sellerMobile = sellerMobile;
        this.sellerName = sellerName;
        this.propertyName = propertyName;
        this.finalAmount = finalAmount;
        this.myCommission = myCommission;
        this.amountLeft = amountLeft;
        this.commissionLeft = commissionLeft;
        this.registryDate = registryDate;
        this.advGivenDate = advGivenDate;
        this.otherInfo = otherInfo;
        this.dealStatus = dealStatus;
    }

    public String getBuyerMobile() {
        return buyerMobile;
    }

    public void setBuyerMobile(String buyerMobile) {
        this.buyerMobile = buyerMobile;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getSellerMobile() {
        return sellerMobile;
    }

    public void setSellerMobile(String sellerMobile) {
        this.sellerMobile = sellerMobile;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(String finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getMyCommission() {
        return myCommission;
    }

    public void setMyCommission(String myCommission) {
        this.myCommission = myCommission;
    }

    public String getAmountLeft() {
        return amountLeft;
    }

    public void setAmountLeft(String amountLeft) {
        this.amountLeft = amountLeft;
    }

    public String getCommissionLeft() {
        return commissionLeft;
    }

    public void setCommissionLeft(String commissionLeft) {
        this.commissionLeft = commissionLeft;
    }

    public String getRegistryDate() {
        return registryDate;
    }

    public void setRegistryDate(String registryDate) {
        this.registryDate = registryDate;
    }

    public String getAdvGivenDate() {
        return advGivenDate;
    }

    public void setAdvGivenDate(String advGivenDate) {
        this.advGivenDate = advGivenDate;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public String getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(String dealStatus) {
        this.dealStatus = dealStatus;
    }
}
