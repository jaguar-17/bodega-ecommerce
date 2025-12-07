import {Component, inject} from '@angular/core';
import {CartService} from '../../../core/services/cart.service';
import {CurrencyPipe} from '@angular/common';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-cart',
  imports: [
    CurrencyPipe,
    RouterLink
  ],
  templateUrl: './cart.html',
  styleUrl: './cart.css',
})
export class Cart {
  private cartService = inject(CartService);

  items = this.cartService.items;
  totalPrice = this.cartService.totalPrice;

  increase(id: number, quantity: number): void {
    this.cartService.updateQuantity(id, quantity + 1);
  }

  decrease(id: number, quantity: number): void {
    this.cartService.updateQuantity(id, quantity - 1);
  }

  remove(id: number): void {
    this.cartService.removeFromCart(id);
  }
}
