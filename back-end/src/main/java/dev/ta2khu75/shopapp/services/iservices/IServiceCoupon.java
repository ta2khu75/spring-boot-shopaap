package dev.ta2khu75.shopapp.services.iservices;

import dev.ta2khu75.shopapp.exceptions.DataNotFoundException;

public interface IServiceCoupon {
  double calculateCouponValue(String couponCode, double totalAmount) throws DataNotFoundException;
}
