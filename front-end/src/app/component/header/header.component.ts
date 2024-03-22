import { Component, Input, OnInit } from '@angular/core';
import { LogoComponent } from '../logo/logo.component';
import { Router, RouterModule } from '@angular/router';
import { StoreService } from '../../service/store.service';
import { User } from '../../model/user';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [LogoComponent, RouterModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit{
  select:number=0;
  user?: User;
  constructor(private router:Router, private storeService:StoreService){
  }
  ngOnInit(): void {
    this.user=this.storeService.getUser();
  }
  logout(){
    debugger
    this.storeService.removeUser();
    this.storeService.removeToken();
    this.router.navigate(["/login"]);
  }
  onSelect(index:number){
    this.select=index
  }
}
