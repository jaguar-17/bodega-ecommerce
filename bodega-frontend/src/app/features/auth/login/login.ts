import {Component, inject} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {AuthService} from '../../../core/services/auth.service';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  url: string = "http://localhost:8080/oauth2/authorization/google"

  loginForm: FormGroup = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]],
  });

  constructor() {
    this.route.queryParams.subscribe(params => {
      const token = params['token'];
      if (token) {
        console.log("Token recibido de Google: ", token);
        localStorage.setItem('token', token);
        this.router.navigate(['/']);
      }
    });
  }

  errorMessage: string = '';

  onSubmit() {
    if (this.loginForm.invalid) return;

    this.authService.login(this.loginForm.value).subscribe({
      next: (token) => {
        console.log('Login exitoso: ', token);
        this.router.navigate(['/']);
      },
      error: (err) => {
        console.error('Error en login: ', err);
        this.errorMessage = 'Credenciales inválidas. Por favor, inténtalo de nuevo.';
      }
    });
  }
}
