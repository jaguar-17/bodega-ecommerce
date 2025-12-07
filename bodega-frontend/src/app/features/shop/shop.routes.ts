import {Routes} from '@angular/router';
import {ProductList} from './product-list/product-list';

export const SHOP_ROUTES: Routes = [
  {
    path: '',
    component: ProductList
  }
];
