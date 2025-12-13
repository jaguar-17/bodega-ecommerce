import {Component, inject, signal} from '@angular/core';
import {OrderService} from '../../../core/services/order.service';
import {CurrencyPipe} from '@angular/common';
import {Order} from '../../../core/models/order.model';

@Component({
  selector: 'app-dashboard',
  imports: [
    CurrencyPipe
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {
  private orderService = inject(OrderService);

  orders = signal<Order[]>([]);
  private loading = signal<boolean>(true);

  statusColors: any = {
    'PENDING_PAYMENT': 'bg-gray-100 text-gray-800',
    'VERIFYING_PAYMENT': 'bg-yellow-100 text-yellow-800',
    'IN_PREPARATION': 'bg-blue-100 text-blue-800',
    'ON_WAY': 'bg-purple-100 text-purple-800',
    'READY_FOR_PICKUP': 'bg-indigo-100 text-indigo-800',
    'DELIVERED': 'bg-green-100 text-green-800',
    'CANCELLED': 'bg-red-100 text-red-800'
  }

  constructor() {
    this.loadOrders();
  }

  loadOrders(): void {
    this.loading.set(true);
    this.orderService.getAllOrders().subscribe({
      next: (data) => {
        this.orders.set(data);
        this.loading.set(false);
      },
      error: (err) => console.log(err)
    });
  }

  approvePayment(order: Order) {
    if (!confirm(`¿Comfirmas que recibiste el pago de ${order.totalAmount}?`)) return;
    this.orderService.updateStatus(order.id, 'IN_PREPARATION').subscribe(() => {
      this.loadOrders();
    });
  }

  nextStatus(order: Order) {
    let next = '';
    if (order.deliveryMethod === 'DELIVERY') {
      if (order.status === 'IN_PREPARATION') next = 'ON_WAY';
      else if (order.status === 'ON_WAY') next = 'DELIVERED';
    } else {
      if (order.status === 'IN_PREPARATION') next = 'READY_FOR_PICKUP';
      else if (order.status === 'READY_FOR_PICKUP') next = 'DELIVERED';
    }

    if (next) {
      this.orderService.updateStatus(order.id, next).subscribe(() => this.loadOrders());
    }
  }
}
