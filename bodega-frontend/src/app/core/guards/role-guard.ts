import {CanActivateFn, Router} from '@angular/router';
import {JwtService} from '../services/jwt.service';
import {inject} from '@angular/core';
import {AuthService} from '../services/auth.service';

export const roleGuard: CanActivateFn = (route, state) => {
  const jwtService = inject(JwtService);
  const authService = inject(AuthService);
  const router = inject(Router);

  const requiredRole = 'ADMIN';

  if (!authService.isAuthenticated()) {
    router.navigate(['/auth/login']);
    return false;
  }

  const userRole = jwtService.getRoleFromToken()

  console.log("Rol del usuario: ", userRole);

  if (userRole === 'ADMIN') {
    return true;
  } else {
    alert('Acceso denegado. No tienes los permisos necesarios para acceder a esta página.');
    router.navigate(['/']);
    return false;
  }
};
