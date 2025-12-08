import {Component, inject} from '@angular/core';
import {CartService} from '../../../core/services/cart.service';
import {CurrencyPipe} from '@angular/common';
import {Router, RouterLink} from '@angular/router';
import {OrderService} from '../../../core/services/order.service';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-cart',
  imports: [
    CurrencyPipe,
    RouterLink,
    FormsModule
  ],
  templateUrl: './cart.html',
  styleUrl: './cart.css',
})
export class Cart {
  cartService = inject(CartService);
  orderService = inject(OrderService);
  router = inject(Router);

  items = this.cartService.items;
  totalPrice = this.cartService.totalPrice;

  selectedDeliveryMethod: string = 'PICKUP';
  isProcessingOrder: boolean = false;

  checkout(): void {
    if (this.items().length === 0) return;
    this.isProcessingOrder = true;

    const orderRequest = {
      deliveryMethod: this.selectedDeliveryMethod,
      items: this.items().map(item => ({
        productId: item.product.id,
        quantity: item.quantity
      }))
    };

    this.orderService.createOrder(orderRequest).subscribe({
      next: (order: any) => {
        console.log("Orden creada: ", order);
        this.cartService.clearCart();
        this.isProcessingOrder = false;
        this.router.navigate(['/order-confirmation', order.id]);
      },
      error: (err) => {
        console.error("Error al crear la orden: ", err);
        alert("Hubo un error al procesar tu orden. Por favor, inténtalo de nuevo.");
        this.isProcessingOrder = false;
      }
    });
  }

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
