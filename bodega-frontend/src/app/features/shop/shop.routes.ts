import {Routes} from '@angular/router';
import {ProductList} from './product-list/product-list';
import {Cart} from './cart/cart';

export const SHOP_ROUTES: Routes = [
  {
    path: '',
    component: ProductList
  },
  {
    path: 'cart',
    component: Cart
  }
];
