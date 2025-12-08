import {Component, inject, Input, signal} from '@angular/core';
import {OrderService} from '../../../core/services/order.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-order-confirmation',
  imports: [],
  templateUrl: './order-confirmation.html',
  styleUrl: './order-confirmation.css',
})
export class OrderConfirmation {
  @Input() id!: number;

  orderService = inject(OrderService);
  router = inject(Router);

  selectedFile: File | null = null;
  uploading = signal(false);
  success = signal(false);

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  uploadProof() {
    if (!this.selectedFile || !this.id) return;

    this.uploading.set(true);

    this.orderService.uploadPaymentProof(this.id, this.selectedFile).subscribe({
      next: () => {
        this.uploading.set(false);
        this.success.set(true);
        setTimeout(() => this.router.navigate(['/']), 3000);
      },
      error: err => {
        console.error('Error al subir payment proof: ', err);
        this.uploading.set(false);
        alert('Hubo un error al subir el comprobante de pago. Por favor, inténtalo de nuevo.');
      }
    });
  }
}
