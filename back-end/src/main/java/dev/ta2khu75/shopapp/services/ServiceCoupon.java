package dev.ta2khu75.shopapp.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import dev.ta2khu75.shopapp.exceptions.DataNotFoundException;
import dev.ta2khu75.shopapp.models.Coupon;
import dev.ta2khu75.shopapp.models.CouponCondition;
import dev.ta2khu75.shopapp.repositories.RepositoryCoupon;
import dev.ta2khu75.shopapp.repositories.RepositoryCouponCondition;
import dev.ta2khu75.shopapp.services.iservices.IServiceCoupon;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ServiceCoupon implements IServiceCoupon {
  private final RepositoryCoupon repositoryCoupon;
  private final RepositoryCouponCondition repositoryCouponCondition;
  @Override
  public double calculateCouponValue(String couponCode, double totalAmount) throws DataNotFoundException {
    // TODO Auto-generated method stub
    Coupon coupon=repositoryCoupon.findByCode(couponCode).orElseThrow(()-> new DataNotFoundException("Could not found coupon by code: "+couponCode));
    if(!coupon.isActive()){
      throw new IllegalArgumentException("Coupon is not active");
    }
    double discount=calculateDiscount(coupon, totalAmount);
    double finalAmount =totalAmount-discount;
    return finalAmount;
  }
   
  private double calculateDiscount(Coupon coupon, double totalAmount){
    List<CouponCondition> couponConditions=repositoryCouponCondition.findByCouponId(coupon.getId());
    double discount=0.0;
    double updateTotalAmount=totalAmount;
    for(CouponCondition condition:couponConditions){
      String attribute=condition.getAttribute();
      String operator=condition.getOperator();
      String value=condition.getValue();
      double percentDiscount=Double.valueOf(String.valueOf(condition.getDiscountAmount()));
      if(attribute.equals("minimum_amount")){
        if(operator.equals(">") && updateTotalAmount>Double.parseDouble(value)){
          discount+=updateTotalAmount*percentDiscount/100;
        }
      }else if(attribute.equals("applicable_date")){
        LocalDate applicableDate=LocalDate.parse(value);
        LocalDate currentDate=LocalDate.now();
        if(operator.equalsIgnoreCase("BETWEEN") && currentDate.isEqual(applicableDate)){
          discount+=updateTotalAmount*percentDiscount/100;
        }
    }
    updateTotalAmount=updateTotalAmount-discount;
  }return discount;
}
}
