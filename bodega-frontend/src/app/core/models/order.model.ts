export interface Order {
  id: number;
  code: string;
  user: string;
  createdAt: string;
  status: string;
  deliveryMethod: string;
  totalAmount: number;
  paymentProofUrl?: string;
}
