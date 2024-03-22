import { ProductImage } from "./product.image";

export interface Product{
  id:number;
  name:string;
  price:number;
  thumbnail:string;
  url:string;
  description:string;
  category_id:number;
  product_images:ProductImage[];
}
