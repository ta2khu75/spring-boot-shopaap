import { Component, OnInit } from '@angular/core';
import { OrderResponse } from '../../../response/order.response';
import { OrderService } from '../../../service/order.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-order',
  standalone: true,
  imports: [],
  templateUrl: './order.component.html',
  styleUrl: './order.component.scss',
})
export class AdminOrderComponent implements OnInit {
  orders: OrderResponse[] = [];
  currentPage: number = 0;
  itemsPerPage: number = 12;
  pages: number[] = [];
  visiblePages: number[] = [];
  totalPages: number = 0;
  keyword: string = '';
  constructor(private orderService: OrderService, private router: Router) {}
  getAllOrders(keyword: string, page: number, limit: number) {
    this.orderService.getAllOrders(this.keyword, page, limit).subscribe({
      next: (response: any) => {
        debugger;
        this.orders = response.orders;
        this.totalPages = response.totalPages;
        this.visiblePages = this.generateVisiblePageArray(
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
  ngOnInit(): void {
    this.getAllOrders(this.keyword, this.currentPage, this.itemsPerPage);
  }
  onPageChange(page: number) {
    debugger;
    this.currentPage = page;
    this.getAllOrders(this.keyword, this.currentPage, this.itemsPerPage);
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
  viewOrderDetails(id: number) {
    this.router.navigate([`/admin/order-details/${id}`]);
  }
  removeOrder(id:number){
    const confirmation=window.confirm("Are you sure you want to delete this order?");
    if(!confirmation){
      return;
    }
    this.orderService.deleteOrder(id).subscribe({
      next:(response:any)=>{
        debugger
        location.reload();
      },error:(error)=>{
        debugger
        console.log(error)
      }
    })
  }
}
