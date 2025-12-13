import {Injectable} from '@angular/core';
import {jwtDecode} from 'jwt-decode';

@Injectable({
  providedIn: 'root',
})
export class JwtService {
  getRoleFromToken(): string | null {
    const token = localStorage.getItem('token');
    if (!token) return null;

    try {
      const decoded: any = jwtDecode(token);
      return decoded.role || null;
    } catch (error) {
      console.error('Error decoding JWT token:', error);
      return null;
    }
  }
}
