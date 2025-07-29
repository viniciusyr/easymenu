import { NgOptimizedImage } from '@angular/common';
import { Component, signal, effect, viewChild, ElementRef, AfterViewInit } from '@angular/core';
import { scroll, animate } from 'motion';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [NgOptimizedImage],
  templateUrl: './header.html',
  styleUrl: './header.css'
})
export class Header {

  navbar = viewChild.required<ElementRef>('navbar');

  navbarVisible = signal(true);

  ngOnInit() {
    scroll((scrollInfo: any) => {
      const position = scrollInfo;
      if (position > 200) {
        this.navbarVisible.set(false);
      } else {
        this.navbarVisible.set(true);
      }
    });

  }

    animateNav = effect(() => {
      if(this.navbarVisible()){
        animate(this.navbar().nativeElement, { y: '0%' }, { duration: 0.2 });
      } else {
        animate(this.navbar().nativeElement, { y: '-100%' }, { duration: 0.2 });
      }
      });
  }
