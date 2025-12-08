import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('token');

  const apiUrl = 'http://localhost:8080';

  const isApiRequest = req.url.startsWith(apiUrl);

  // Solo agregar el token a las solicitudes dirigidas a la API

  if (token && isApiRequest) {
    const clonedRequest = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(clonedRequest);
  }

  return next(req);
};
