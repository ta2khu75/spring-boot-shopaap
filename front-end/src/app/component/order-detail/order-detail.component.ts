import { Component, OnInit } from '@angular/core';
import { FooterComponent } from '../footer/footer.component';
import { HeaderComponent } from '../header/header.component';
import { ProductService } from '../../service/product.service';
import { StoreService } from '../../service/store.service';
import { Product } from '../../model/product';
import { environment } from '../../environments/environment';
import { OrderResponse } from '../../response/order.response';
import { OrderService } from '../../service/order.service';
import { OrderDetail } from '../../model/order.detail';
import { ActivatedRoute } from '@angular/router';
@Component({
  selector: 'app-order-detail',
  standalone: true,
  imports: [HeaderComponent, FooterComponent],
  templateUrl: './order-detail.component.html',
  styleUrl: './order-detail.component.scss',
})
export class OrderDetailComponent implements OnInit {
  orderId: number = 0;
  orderResponse: OrderResponse = {
    id: 0,
    user_id: 0,
    fullname: '',
    phone_number: '',
    email: '',
    address: '',
    note: '',
    order_date: new Date(),
    status: '',
    total_money: 0,
    shipping_method: '',
    shipping_address: '',
    shipping_date: new Date(),
    payment_method: '',
    order_details: [],
  };
  totalAmount: number = 0;
  ngOnInit(): void {
    this.orderId=this.active.snapshot.params["id"];
    this.getOrderDetails();
  }
  constructor(
    private orderService: OrderService,
    private active:ActivatedRoute
  ) {}
  getOrderDetails() {
    this.orderService.getOrder(this.orderId).subscribe({
      next: (response: any) => {
        this.orderResponse.id = response.id;
        this.orderResponse.user_id = response.user_id;
        this.orderResponse.fullname = response.fullname;
        this.orderResponse.email = response.email;
        this.orderResponse.phone_number = response.phone_number;
        this.orderResponse.address = response.address;
        this.orderResponse.note = response.note;
        this.orderResponse.order_date = new Date(
          response.order_date[0],
          response.order_date[1] - 1,
          response.order_date[2]
        );
        debugger;
        this.orderResponse.order_details = response.order_details.map(
          (order_detail: OrderDetail) => {
            order_detail.thumbnail = `${environment.apiBaseUrl}/products/images/${order_detail.thumbnail}`;
            return order_detail;
          }
        );
        this.orderResponse.payment_method = response.payment_method;
        this.orderResponse.shipping_date = new Date(
          response.shipping_date[0],
          response.shipping_date[1] - 1,
          response.shipping_date[2]
        );
        this.orderResponse.shipping_method = response.shipping_method;
        this.orderResponse.status = response.status;
        this.orderResponse.total_money = response.total_money;
      },
      error: (error: any) => {
        console.log(error);
      },
      complete:()=>{
        this.calcAmount();
      }
    });
  }
  calcAmount(){
    this.totalAmount=this.orderResponse.order_details.reduce((total,item)=>total+item.price*item.number_of_products,0);
  }
}
