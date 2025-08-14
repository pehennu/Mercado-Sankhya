import { OrderItem } from "./order-item.model";

export interface Order {
  id: number;
  created_at: string;
  total: number;
  items: OrderItem[];
}