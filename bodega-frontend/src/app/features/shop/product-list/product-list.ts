import {Component, inject, signal} from '@angular/core';
import {ProductService} from '../../../core/services/product.service';
import {Product} from '../../../core/models/product.model';
import {CurrencyPipe} from '@angular/common';
import {CartService} from '../../../core/services/cart.service';

@Component({
  selector: 'app-product-list',
  imports: [
    CurrencyPipe
  ],
  templateUrl: './product-list.html',
  styleUrl: './product-list.css',
})
export class ProductList {
  private productService = inject(ProductService);
  private cartService = inject(CartService);

  products = signal<Product[]>([]);
  loading = signal<boolean>(true);

  constructor() {
    this.loadProducts();
  }

  loadProducts() {
    this.productService.getProducts().subscribe({
      next: (data) => {
        this.products.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Error loading products: ', err);
        this.loading.set(false);
      }
    });
  }

  addToCart(product: Product) {
    this.cartService.addToCart(product);
  }
}
