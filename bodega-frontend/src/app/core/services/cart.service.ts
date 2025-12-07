import {computed, effect, Injectable, signal} from '@angular/core';
import {CartItem, Product} from '../models/product.model';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  items = signal<CartItem[]>(JSON.parse(localStorage.getItem('cart_items') || '[]'));

  totalItems = computed(() => this.items().reduce((acc, item) => acc + item.quantity, 0));

  totalPrice = computed(() => this.items().reduce((acc, item) => acc + item.product.price * item.quantity, 0));

  constructor() {
    effect(() => {
      localStorage.setItem('cart_items', JSON.stringify(this.items()));
    });
  }

  addToCart(product: Product) {
    const currentItems = this.items();
    const existingItem = currentItems.find(item => item.product.id === product.id);

    if (existingItem) {
      const updatedItems = currentItems.map(item =>
        item.product.id === product.id ? {...item, quantity: item.quantity + 1} : item
      );
      this.items.set(updatedItems);
    } else {
      this.items.set([...currentItems, {product, quantity: 1}]);
    }
  }

  removeFromCart(productId: number) {
    this.items.update(items => items.filter(item => item.product.id !== productId));
  }

  updateQuantity(productId: number, quantity: number) {
    if (quantity <= 0) {
      this.removeFromCart(productId);
      return;
    }
    this.items.update(items =>
      items.map(item =>
        item.product.id === productId ? {...item, quantity} : item
      )
    );
  }

  clearCart() {
    this.items.set([]);
  }
}
