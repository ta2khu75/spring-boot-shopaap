import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { ProductService } from '../../service/product.service';
import { Product } from '../../model/product';
import { ProductImage } from '../../model/product.image';
import { environment } from '../../environments/environment';
import { StoreService } from '../../service/store.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-detail-product',
  standalone: true,
  imports: [HeaderComponent, FooterComponent],
  templateUrl: './detail-product.component.html',
  styleUrl: './detail-product.component.scss',
})
export class DetailProductComponent implements OnInit {
  quantity:number=1;
  idProduct: number = 6;
  product?: Product;
  currentImage: number = 0;
  constructor(private serviceProduct: ProductService, private serviceCart:StoreService, private router:Router, private active:ActivatedRoute) {}
  ngOnInit(): void {
    this.idProduct=this.active.snapshot.params["id"];
    this.getProductDetail()
  }
  getProductDetail() {
    this.serviceProduct.getDetailProduct(this.idProduct).subscribe({
      next: (response: any) => {
        debugger;
        if (response.product_images && response.product_images.length > 0) {
          response.product_images.forEach((image: ProductImage) => {
            image.image_url = `${environment.apiBaseUrl}/products/images/${image.image_url}`;
          });
        }
        debugger;
        this.product = response;
        this.showImage(0);
      },
      error(error) {
        console.log(error);
      },
    });
  }
  showImage(index: number) {
    debugger;
    if (
      this.product &&
      this.product.product_images &&
      this.product.product_images.length > 0
    ) {
      if (index < 0) {
        index = 0;
      } else if (index >= this.product.product_images.length) {
        index = this.product.product_images.length - 1;
      }
    }
    this.currentImage=index;
  }
  onThumbernailClick(index:number){
    debugger
    this.showImage(index);
  }
  addToCart(){
    this.serviceCart.addToCart(this.product!.id, this.quantity);
  }
  buyNow(){
    this.router.navigate([`/order/${this.idProduct}`])
  }
}
