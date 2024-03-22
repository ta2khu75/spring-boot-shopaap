import { Routes } from '@angular/router';
import { LoginComponent } from './component/login/login.component';
import { RegisterComponent } from './component/register/register.component';
import { HomeComponent } from './component/home/home.component';
import { DetailProductComponent } from './component/detail-product/detail-product.component';
import { OrderComponent } from './component/order/order.component';
import { OrderDetailComponent } from './component/order-detail/order-detail.component';
import { UserComponent } from './component/user/user.component';
import { authGuard } from './guards/auth.guard';
import { AdminComponent } from './component/admin/admin.component';
import { adminGuard } from './guards/admin.guard';
import { AdminOrderComponent } from './component/admin/order/order.component';
import { AdminProductComponent } from './component/admin/product/product.component';
import { AdminCategoryComponent } from './component/admin/category/category.component';
import { AdminOrderDetailsComponent } from './component/admin/order-details/order-details.component';

export const routes: Routes = [
  {
    path: 'register',
    component: RegisterComponent,
  },
  {
    path: '',
    component: HomeComponent,
  },
  {
    path: 'order/:id',
    component: OrderComponent,
    canActivate: [authGuard],
  },
  {
    path: 'order',
    component: OrderComponent,
    canActivate: [authGuard],
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'order-detail/:id',
    component: OrderDetailComponent,
    canActivate: [authGuard],
  },
  {
    path: 'product/:id',
    component: DetailProductComponent,
  },
  {
    path: 'user-detail',
    component: UserComponent,
    canActivate: [authGuard],
  },
  {
    path: 'admin',
    component: AdminComponent,
    children: [
      {
        path: '',
        component: AdminOrderComponent,
      },
      {
        path: 'order-details/:id',
        component: AdminOrderDetailsComponent,
      },
      {
        path: 'order',
        component: AdminOrderComponent,
      },
      {
        path: 'product',
        component: AdminProductComponent,
      },
      {
        path: 'category',
        component: AdminCategoryComponent,
      },
    ],
    canActivate: [adminGuard],
  },
  {
    path: 'admin/order',
    component: AdminOrderComponent,
    canActivate: [adminGuard],
  },
];
