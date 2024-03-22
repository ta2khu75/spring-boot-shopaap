import { Component, OnInit } from '@angular/core';
import { StoreService } from '../../service/store.service';
import { Router, RouterOutlet } from '@angular/router';
import { User } from '../../model/user';
import { AdminOrderComponent } from './order/order.component';
import { AdminCategoryComponent } from './category/category.component';
import { AdminProductComponent } from './product/product.component';
@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [AdminOrderComponent, AdminCategoryComponent, AdminProductComponent, RouterOutlet],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.scss',
})
export class AdminComponent implements OnInit {
  //adminComponent: string = 'order';
  user!: User;
  constructor(private storeService: StoreService, private router: Router) {}
  ngOnInit(): void {
    this.user = this.storeService.getUser();
  }
  logout() {
    debugger;
    this.storeService.removeUser();
    this.storeService.removeToken();
    this.router.navigate(['/login']);
  }
  showAdminComponent(componentName:string) {
    this.router.navigate([`/admin/${componentName}`])
  }
  
}
