import { Component, Input, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { ProductService } from '../../service/product.service';
import { Product } from '../../model/product';
import { environment } from '../../environments/environment';
import { CategoryService } from '../../service/category.service';
import { Category } from '../../model/category';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { StoreService } from '../../service/store.service';
import { User } from '../../model/user';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [HeaderComponent, FooterComponent, FormsModule, RouterModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent implements OnInit {
  keyword: string = '';
  category_id: number = 0;
  products: Product[] = [];
  currentPage: number = 0;
  itemsPerPage: number = 9;
  pages: number[] = [];
  totalPages: number = 0;
  visiblePage: number[] = [];
  categories: Category[] = [];
  constructor(
    private productService: ProductService,
    private categoryService: CategoryService,
    private storeService: StoreService,
    private router:Router
  ) {

  }
  ngOnInit(): void {
    this.getProduct(this.currentPage, this.itemsPerPage);
    this.getCategory();
  }
  getProduct(page: number, limit: number) {
    this.productService
      .getProduct(page, limit, this.keyword, this.category_id)
      .subscribe({
        next: (response: any) => {
          debugger;
          response.products.forEach((element: Product) => {
            element.url = `${environment.apiBaseUrl}/products/images/${element.thumbnail}`;
          });
          this.products = response.products;
          this.totalPages = response.totalPages;
          this.visiblePage = this.generateVisiblePageArray(
            this.currentPage,
            this.totalPages
          );
        },
        complete: () => {
          debugger;
        },
        error: (error: any) => {
          console.log(error);
        },
      });
  }
  getCategory() {
    this.categoryService.getCagetory().subscribe({
      next: (response: Category[]) => {
        this.categories = response;
      },
      error: (error: any) => {
        console.log('Co loi ' + JSON.stringify(error.error));
      },
    });
  }
  onPageChange(page: number) {
    debugger;
    this.currentPage = page;
    this.getProduct(this.currentPage, this.itemsPerPage);
  }
  generateVisiblePageArray(currentPage: number, totalPage: number): number[] {
    const maxVisiblePages = 5;
    const halfVisiblePages = Math.floor(maxVisiblePages / 2);
    let startPage = Math.max(currentPage - halfVisiblePages, 1);
    let endPage = Math.min(currentPage + maxVisiblePages, totalPage);
    if (endPage - startPage + 1 < maxVisiblePages) {
      startPage = Math.max(endPage - maxVisiblePages + 1, 1);
    }
    return new Array(endPage - startPage + 1)
      .fill(0)
      .map((_, index) => startPage + index);
  }
  searchProduct() {
    this.getProduct(this.currentPage, this.itemsPerPage);
  }
  addToCart(id:number){
    alert("thêm vào giỏ hàng thành công")
    this.storeService.addToCart(id, 1);
  }
  buyNow(id:number){
    this.router.navigate([`/order/${id}`])
  }
}
