import { Component } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { StoreService } from '../../service/store.service';
import { User } from '../../model/user';
import { UserUpdateDto } from '../../dtos/user/update.dto';
import { UserService } from '../../service/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './user.component.html',
  styleUrl: './user.component.scss',
})
export class UserComponent {
  userForm: FormGroup;
  user: User;
  userUpdate: UserUpdateDto = {
    id: 0,
    fullname: '',
    password: '',
    retype_password: '',
    address: '',
    date_of_birth: new Date(),
  };
  constructor(
    private storeService: StoreService,
    private fb: FormBuilder,
    private router: Router,
    private userService: UserService
  ) {
    this.user = this.storeService.getUser();
    this.userForm = this.fb.group(
      {
        fullname: [this.user.full_name, Validators.minLength(3)],
        password: ['', Validators.minLength(3)],
        retype_password: ['', Validators.minLength(3)],
        address: [this.user.address, Validators.minLength(5)],
        date_of_birth: [this.user.date_of_birth.toString()]
      },
      {
        validator: this.passwordMathValidator,
      }
    );
  }
  update() {
    if (this.userForm.valid) {
      this.userUpdate = {
        ...this.userUpdate,
        ...this.userForm.value,
      };
      this.userUpdate.id = this.user.id;
      debugger;
      this.userService
        .update(this.userUpdate, this.storeService.getToken() ?? '')
        .subscribe({
          next: (response) => {
            this.storeService.removeToken();
            this.storeService.removeUser();
            this.router.navigate(['/login']);
          },error(err) {
              alert(err.error.message);
          },
        });
    }else{
      if(this.userForm.hasError("passwordMismatch")){
        alert("mật khẩu và xác nhận mật  chưa chính xác");
      }
    }
  }
  passwordMathValidator(): ValidatorFn {
    return (formGoup: AbstractControl): ValidationErrors | null => {
      const password = formGoup.get('password')?.value;
      const retypassword = formGoup.get('retypassword')?.value;
      if (password !== retypassword) {
        return { passwordMismatch: true };
      }
      return null;
    };
  }
}
