import {Routes} from '@angular/router';
import {ProductList} from './product-list/product-list';
import {Cart} from './cart/cart';
import {OrderConfirmation} from './order-confirmation/order-confirmation';

export const SHOP_ROUTES: Routes = [
  {
    path: '',
    component: ProductList
  },
  {
    path: 'cart',
    component: Cart
  },
  {
    path: 'order-confirmation/:id',
    component: OrderConfirmation
  }
];
