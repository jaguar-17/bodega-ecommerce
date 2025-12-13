import {Routes} from '@angular/router';
import {authGuard, publicGuard} from './core/guards/auth-guard';
import {roleGuard} from './core/guards/role-guard';

export const routes: Routes = [
  {
    path: 'auth',
    canActivate: [publicGuard],
    loadChildren: () =>
      import('./features/auth/auth.routes').then((m) => m.AUTH_ROUTES),
  },
  {
    path: 'admin',
    canActivate: [roleGuard],
    loadComponent: () =>
      import('./features/admin/dashboard/dashboard').then(m => m.Dashboard)
  },
  {
    path: '',
    canActivate: [authGuard],
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
