import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { FormsModule } from '@angular/forms';
import { LoginDto } from '../../dtos/user/login.dto';
import { UserService } from '../../service/user.service';
import { Router, RouterModule } from '@angular/router';
import { RoleService } from '../../service/role.service';
import { Role } from '../../model/role';
import { User } from '../../model/user';
import { StoreService } from '../../service/store.service';
import { LoginResponse } from '../../response/login.response';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [HeaderComponent, FooterComponent, FormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent implements OnInit {
  phone: string;
  password: string;
  selectedRole: Role | undefined;
  roles: Role[] = [];
  userResponse?: User;
  rememberMe: boolean = true;
  constructor(
    private userService: UserService,
    private roleService: RoleService,
    private router: Router,
    private storeService: StoreService
  ) {
    this.phone = '';
    this.password = '';
  }
  ngOnInit(): void {
    debugger;
    this.roleService.getRoles().subscribe({
      next: (roles: Role[]) => {
        debugger;
        this.roles = roles;
        this.selectedRole = roles.length > 0 ? roles[0] : undefined;
      },
      error: (error: any) => {
        debugger;
        console.log(error);
      },
    });
  }
  login() {
    debugger;
    const loginDto: LoginDto = {
      phone_number: this.phone,
      password: this.password,
      role_id: this.selectedRole?.id ?? 1,
    };
    this.userService.login(loginDto).subscribe({
      next: (response: LoginResponse) => {
        if (this.rememberMe) {
          this.storeService.setToken(response.token);
          debugger;
          this.userService.getUserDetails(response.token).subscribe({
            next: (userResponse: any) => {
              debugger;
              this.userResponse = {
                id: userResponse.id,
                full_name: userResponse.full_name,
                phone_number: userResponse.phone_number,
                address: userResponse.address,
                date_of_birth: new Date(userResponse.date_of_birth),
                facebook_account_id: userResponse.facebook_account_id,
                google_account_id: userResponse.google_account_id,
                role_id: userResponse.role.id
              };
              this.storeService.setUser(this.userResponse);
              debugger
              if(userResponse.role.name==="admin"){
                this.router.navigate(["/admin"]);
              }else{
                this.router.navigate(['/']);
              }
            },
          });
        }
      },
      complete() {
        debugger;
      },
      error(error: any) {
        debugger;
        alert(`Login failed error:${JSON.stringify(error.error.message)}`);
      },
    });
  }
}
