import { Role } from "./role";

export interface User{
  id:number;
  full_name:string;
  phone_number:string;
  address:string;
  date_of_birth:Date;
  facebook_account_id:number;
  google_account_id:number;
  role_id:number;
}
