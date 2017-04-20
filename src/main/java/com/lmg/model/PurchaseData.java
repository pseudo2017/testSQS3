package com.lmg.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;


/*
 * 
 * voir exemple sur
 * https://github.com/phorest/phorest-events/blob/master/model/src/main/java/com/phorest/events/model/PurchaseItemData.java
 */


@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class PurchaseData implements EventData {
    private String branchId;
    private String businessId;
    private ClientData client;
    private String description;
    private BigDecimal onlineDeposit;
    private BigDecimal onlineDiscount;
//    private List<PaymentData> payments;
    private DateTime purchaseDateTime;
//    private List<PurchaseItemData> purchaseItems;
//    private StaffData staff;
    private String transactionId;
//    private DiscountData voucherDiscount;

    @JsonIgnore
    public String getClientId() {
        return client.getId();
    }

    @JsonIgnore
    public String getClientFirstName() {
        return client.getFirstName();
    }

    @JsonIgnore
    public String getClientLastName() {
        return client.getLastName();
    }

/*    @JsonIgnore
    public String getStaffId() {
        return staff.getId();
    }    
    */
}
