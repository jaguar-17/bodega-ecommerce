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

export interface CartItem {
  product: Product;
  quantity: number;
}
