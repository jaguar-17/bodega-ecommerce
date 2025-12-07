import {Component, inject} from '@angular/core';
import {AuthService} from '../../core/services/auth.service';
import {Router, RouterLink, RouterOutlet} from '@angular/router';
import {CartService} from '../../core/services/cart.service';

@Component({
  selector: 'app-main-layout',
  imports: [
    RouterOutlet,
    RouterLink
  ],
  templateUrl: './main-layout.html',
  styleUrl: './main-layout.css',
})
export class MainLayout {
  private authService = inject(AuthService);
  cartService = inject(CartService);
  private router = inject(Router);

  isAuthenticated(): boolean {
    return this.authService.isAuthenticated();
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/auth/login']);
  }
}
