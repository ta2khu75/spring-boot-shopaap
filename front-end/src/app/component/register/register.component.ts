import { Component, ViewChild } from '@angular/core';
import { FooterComponent } from '../footer/footer.component';
import { FormsModule, NgForm } from '@angular/forms';
import { NgIf } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';
import { UserService } from '../../service/user.service';
import { RegisterDto } from '../../dtos/user/register.dto';
@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FooterComponent, FormsModule, NgIf, RouterModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
})
export class RegisterComponent {
  @ViewChild('registerForm') registerForm!: NgForm;
  phone: string;
  password: string;
  retypePassword: string;
  fullname: string;
  address: string;
  isAccepted: boolean;
  birthday: Date;
  constructor(private userService: UserService, private router: Router) {
    this.phone = `0986753241`;
    this.password = `123123`;
    this.retypePassword = '123123';
    this.fullname = 'minh';
    this.address = 'vap nga';
    this.isAccepted = false;
    this.birthday = new Date();
    this.birthday.setFullYear(this.birthday.getFullYear() - 18);
  }
  onPhoneChange() {
    console.log(this.phone);
  }
  register() {
    const registerDto: RegisterDto = {
      fullname: this.fullname,
      phone_number: this.phone,
      address: this.address,
      password: this.password,
      retype_password: this.retypePassword,
      date_of_birth: this.birthday,
      facebook_account_id: 0,
      google_account_id: 0,
      role_id: 1,
    };
    this.userService.register(registerDto).subscribe({
      next: (response: any) => {
        debugger;
        this.router.navigate(['/login']);
      },
      complete() {
        debugger;
      },
      error(error: any) {
        debugger;
        alert(`Register fail:${error.error}`);
      },
    });
  }
  checkPasswordMatch() {
    if (this.password !== this.retypePassword) {
      this.registerForm.form.controls['retypePassword'].setErrors({
        passwordMisMath: true,
      });
    } else {
      this.registerForm.form.controls['retypePassword'].setErrors(null);
    }
  }
  chechAge() {
    if (this.birthday) {
      const today = new Date();
      const birthday = new Date(this.birthday);
      let age = today.getFullYear() - birthday.getFullYear();
      const monthDiff = today.getMonth() - birthday.getMonth();
      if (
        monthDiff < 0 ||
        (monthDiff === 0 && today.getDate() < birthday.getDate())
      ) {
        age--;
      }
      if (age < 18) {
        this.registerForm.form.controls['birthday'].setErrors({
          invalidAge: true,
        });
      } else {
        this.registerForm.form.controls['birthday'].setErrors(null);
      }
    }
  }
}
