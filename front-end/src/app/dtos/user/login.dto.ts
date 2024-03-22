import { IsString, IsNotEmpty, IsPhoneNumber, IsDate, IsNumberString } from 'class-validator';
export class LoginDto {
  @IsPhoneNumber()
  phone_number:string;
  @IsString()
  password:string;
  @IsNumberString()
  role_id:number;
  constructor(data:any){
    this.phone_number=data.phone_number;
    this.password=data.password;
    this.role_id=data.role_id;
  }
}
