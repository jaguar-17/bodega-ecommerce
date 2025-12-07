import {Routes} from '@angular/router';

export const routes: Routes = [
  {
    path: 'auth',
    loadChildren: () =>
      import('./features/auth/auth.routes').then((m) => m.AUTH_ROUTES),
  },
  {
    path: '',
    loadComponent: () => import('./layout/main-layout/main-layout').then(m => m.MainLayout),
    children: [
      {
        path: '',
        // Lazy Loading del catálogo
        loadChildren: () => import('./features/shop/shop.routes').then(m => m.SHOP_ROUTES)
      }
    ]
  },
  // Redirección por defecto
  {path: '**', redirectTo: ''}
];
