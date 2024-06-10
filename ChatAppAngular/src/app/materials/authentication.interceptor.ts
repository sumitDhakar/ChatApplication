import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HTTP_INTERCEPTORS
} from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { AuthenticationService } from '../user/services/authentication.service';
import Swal from 'sweetalert2';

@Injectable()
export class AuthenticationInterceptor implements HttpInterceptor {

  constructor(private authService: AuthenticationService) { }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {

    const token = this.authService.getToken();
    if (token !== null) {
      request = request.clone({
        setHeaders: { Authorization: `${token}` }
      })
    }
    console.log(request);
    

    return next.handle(request).pipe(catchError((error: any) => {
      const Toast = Swal.mixin({
        toast: true,
        position: "top-left",
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,

      });
      if (error.status === 401)
        Toast.fire({
          icon: 'error',
          title: 'Unauthorized'
        });
      return throwError(error);
    }));
  }
}

export const AuthInterceptor = [{
  provide: HTTP_INTERCEPTORS,
  useClass: AuthenticationInterceptor,
  multi: true
}]