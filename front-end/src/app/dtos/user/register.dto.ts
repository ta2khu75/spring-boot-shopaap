import { IsString, IsNotEmpty, IsPhoneNumber, IsDate } from 'class-validator';
export class RegisterDto {
  @IsString()
  @IsNotEmpty()
  fullname: string;

  @IsPhoneNumber()
  phone_number: string;
  
  @IsNotEmpty()
  @IsString()
  address: string;
  
  @IsNotEmpty()
  @IsString()
  password: string;
  
  @IsString()
  @IsNotEmpty()
  retype_password: string;
  
  @IsDate()
  date_of_birth: Date;
  facebook_account_id: number = 0;
  google_account_id: number = 0;
  role_id: number = 1;
  constructor(data: any) {
    this.fullname = '';
    this.phone_number = '';
    this.address = '';
    this.password = '';
    this.retype_password = '';
    this.date_of_birth = new Date();
    this.facebook_account_id = data.facebook_id || 0;
    this.google_account_id = data.google_id || 0;
    this.role_id = 1;
  }
}
