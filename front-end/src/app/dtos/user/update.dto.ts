export interface UserUpdateDto {
  id: number;
  fullname: string;
  password: string;
  retype_password: string;
  address: string;
  date_of_birth: Date;
}
