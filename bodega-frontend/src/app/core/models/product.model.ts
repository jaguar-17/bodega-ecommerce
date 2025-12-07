export interface Product {
  id: number;
  code: string;
  name: string;
  description: string;
  price: number;
  imageUrl: string;
  categoryId: number;
  active: boolean;
}
