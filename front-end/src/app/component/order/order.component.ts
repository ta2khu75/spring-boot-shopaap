import { Component } from '@angular/core';
import { FooterComponent } from '../footer/footer.component';
import { HeaderComponent } from '../header/header.component';
import { ProductService } from '../../service/product.service';
import { StoreService } from '../../service/store.service';
import { environment } from '../../environments/environment';
import { Product } from '../../model/product';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { OrderDto } from '../../dtos/order.dto';
import { OrderService } from '../../service/order.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-order',
  standalone: true,
  imports: [HeaderComponent, FooterComponent, ReactiveFormsModule],

  templateUrl: './order.component.html',
  styleUrl: './order.component.scss',
})
export class OrderComponent {
  orderProduct: number = 0;
  orderForm: FormGroup;
  cartItems: { product: Product; quantity: number }[] = [];
  couponCode: string = '';
  totalAmount: number = 0;
  orderDto: OrderDto = {
    user_id: 0,
    fullname: '',
    email: '',
    phone_number: '',
    address: '',
    note: '',
    total_money: 0,
    shipping_method: 'express',
    payment_method: 'cod',
    coupon_code: '',
    cart_items: [],
  };
  ngOnInit(): void {
    this.orderProduct = this.active.snapshot.params['id']===undefined?0:this.active.snapshot.params['id'];
    if (this.orderProduct == 0) {
      this.getProductByIds();
      this.calcAmount();
    }else{
      this.getProductById();
    }
  }
  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
   // private StoreService: StoreService,
    private orderService: OrderService,
    private router: Router,
    private storeService: StoreService,
    private active: ActivatedRoute
  ) {
    this.orderDto.user_id = 13;
    this.orderForm = this.fb.group({
      fullname: ['', Validators.required],
      email: ['', Validators.email],
      phone_number: ['', [Validators.required, Validators.minLength(10)]],
      address: ['', [Validators.required, Validators.minLength(5)]],
      note: [''],
      shipping_method: ['Thường'],
      payment_method: ['Khi nhận hàng'],
    });
  }
  getProductByIds() {
    const cart = this.storeService.getCart();
    const productIds = Array.from(cart.keys());
    if(productIds.length===0){
      return;
    }
    debugger;
    this.productService.getProductIds(productIds).subscribe({
      next: (products) => {
        debugger;
        this.cartItems = productIds.map((productId) => {
          debugger;
          const product = products.find((p) => p.id === productId);
          if (product) {
            product.thumbnail = `${environment.apiBaseUrl}/products/images/${product.thumbnail}`;
          }
          return {
            product: product!,
            quantity: cart.get(productId)!,
          };
        });
      },
      error(error) {
        console.log(error);
      },
      complete: () => {
        this.calcAmount();
      },
    });
  }
  getProductById() {
    this.productService.getProductId(this.orderProduct).subscribe({
      next: (products) => {
        debugger;
        const product = products[0];
        product.thumbnail = `${environment.apiBaseUrl}/products/images/${product.thumbnail}`;
        this.cartItems = [
          {
            product: product,
            quantity: 1,
          },
        ];
      },
      error(error) {
        console.log(error);
      },
      complete: () => {
        this.calcAmount();
      },
    });
  }
  calcAmount() {
    this.totalAmount = this.cartItems.reduce(
      (total, item) => total + item.product.price * item.quantity,
      0
    );
  }
  orderPlace() {
    // if (this.orderDto.user_id == 0) {
    //   this.router.navigate(['/login']);
    //   return;
    // }
    debugger;
    if (this.orderForm.valid) {
      this.orderDto = {
        ...this.orderDto,
        ...this.orderForm.value,
      };
      this.orderDto.cart_items = this.cartItems.map((arg) => ({
        product_id: arg.product.id,
        quantity: arg.quantity,
      }));
      this.orderDto.total_money=this.totalAmount;
      this.orderService.order(this.orderDto).subscribe({
        next: (response: any) => {
          debugger;
          alert(
            `Bạn đã dat thành công với ${response.order_details.length} sản phẩm`
          );
          localStorage.removeItem('cart');
          debugger;
          this.router.navigate([`/order-detail/${response.id}`]);
        },
        error: (error: any) => {
          debugger;
          console.log('Lỗi khi đặt hàng:' + error.error);
        },
        complete: () => {
          this.calcAmount();
        },
      });
    } else {
      alert('Du lieu ko hop le');
    }
  }
}
