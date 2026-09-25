/**
 * Loader & UX Enhancements for WardConnect CCMS
 * Handles red/black splash screen overlay, sticky shrink navbar, scroll reveal, hero parallax, number count-up, and toasts.
 */

(function () {
  'use strict';

  // 1. Splash Screen Loader Overlay (Minimum 0.8s display time)
  function createSplashOverlay() {
    if (document.getElementById('splash-overlay')) return;

    const overlay = document.createElement('div');
    overlay.id = 'splash-overlay';
    overlay.setAttribute('aria-hidden', 'true');
    overlay.innerHTML = `
      <div class="splash-content">
        <div class="splash-spinner"></div>
        <div class="splash-title">
          <span class="brand-mark">⌂</span>
          <span>Ward<span style="color:#e11d2e">Connect</span></span>
        </div>
        <div class="splash-progress-track">
          <div class="splash-progress-bar"></div>
        </div>
      </div>
    `;

    if (document.body) {
      document.body.prepend(overlay);
    } else {
      document.addEventListener('DOMContentLoaded', () => document.body.prepend(overlay));
    }

    const startTime = performance.now();
    const minShowTime = 800; // 0.8 seconds minimum for clean feedback

    function hideSplash() {
      const elapsed = performance.now() - startTime;
      const remaining = Math.max(0, minShowTime - elapsed);

      setTimeout(() => {
        const el = document.getElementById('splash-overlay');
        if (el) {
          el.classList.add('fade-out');
          setTimeout(() => el.remove(), 500);
        }
      }, remaining);
    }

    if (document.readyState === 'complete') {
      hideSplash();
    } else {
      window.addEventListener('load', hideSplash);
      setTimeout(hideSplash, 3500); // Safety fallback
    }
  }

  // 2. Scroll Shrink Navbar, Active Nav Links & Scroll Reveal Observers
  function initScrollEnhancements() {
    const navbar = document.querySelector('.navbar');
    if (navbar) {
      window.addEventListener('scroll', () => {
        if (window.scrollY > 20) {
          navbar.classList.add('shrunk');
        } else {
          navbar.classList.remove('shrunk');
        }
      }, { passive: true });

      // Persistent underline on active nav link
      const currentPage = document.body.dataset.page;
      document.querySelectorAll('.nav-links a').forEach(link => {
        const href = link.getAttribute('href') || '';
        if (currentPage === 'home' && href === 'index.html') {
          link.classList.add('active');
        } else if (currentPage === 'resident-login' && href === 'resident-login.html') {
          link.classList.add('active');
        } else if (currentPage === 'admin-login' && href === 'admin-login.html') {
          link.classList.add('active');
        }
      });
    }

    // Hero parallax background subtle motion
    const heroArt = document.querySelector('.hero-art');
    const isPrefersReduced = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
    if (heroArt && !isPrefersReduced) {
      window.addEventListener('scroll', () => {
        const scrolled = window.scrollY;
        if (scrolled < 700) {
          heroArt.style.transform = `translateY(${scrolled * 0.08}px)`;
        }
      }, { passive: true });
    }

    // Scroll reveal with stagger indices
    const revealTargets = document.querySelectorAll('.step-card, .category-card, .quote-card, .panel, .stat, .hero-grid > div');
    revealTargets.forEach((el, index) => {
      el.classList.add('reveal-on-scroll');
      el.style.setProperty('--stagger-index', index % 6);
    });

    if ('IntersectionObserver' in window) {
      const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
          if (entry.isIntersecting) {
            entry.target.classList.add('revealed');
            observer.unobserve(entry.target);
          }
        });
      }, { threshold: 0.1 });

      document.querySelectorAll('.reveal-on-scroll').forEach(el => observer.observe(el));
    } else {
      document.querySelectorAll('.reveal-on-scroll').forEach(el => el.classList.add('revealed'));
    }
  }

  // 3. Count Up Animation for Stats
  window.animateCountUp = function (el, targetNum, duration = 1200) {
    if (!el || isNaN(targetNum)) return;
    const isPrefersReduced = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
    if (isPrefersReduced) {
      el.textContent = targetNum;
      return;
    }

    let start = 0;
    const startTime = performance.now();
    function update(now) {
      const elapsed = now - startTime;
      const progress = Math.min(elapsed / duration, 1);
      const eased = 1 - Math.pow(1 - progress, 3);
      el.textContent = Math.floor(eased * targetNum);
      if (progress < 1) {
        requestAnimationFrame(update);
      } else {
        el.textContent = targetNum;
      }
    }
    requestAnimationFrame(update);
  };

  // Observe Stats Grid for Count-up
  function initStatsCountUp() {
    const statElements = document.querySelectorAll('.stats-band .stat strong');
    if (!statElements.length) return;

    if ('IntersectionObserver' in window) {
      const statsObserver = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
          if (entry.isIntersecting) {
            const strVal = entry.target.textContent.trim();
            const num = parseInt(strVal, 10);
            if (!isNaN(num)) {
              window.animateCountUp(entry.target, num);
            }
            statsObserver.unobserve(entry.target);
          }
        });
      }, { threshold: 0.3 });

      statElements.forEach(el => statsObserver.observe(el));
    }
  }

  // 4. Global Toast Notification System
  function ensureToastContainer() {
    let container = document.querySelector('.app-toast-container');
    if (!container) {
      container = document.createElement('div');
      container.className = 'app-toast-container';
      document.body.appendChild(container);
    }
    return container;
  }

  window.showAppToast = function (message, type = 'info', duration = 3000) {
    const container = ensureToastContainer();
    const toast = document.createElement('div');
    toast.className = `app-toast ${type}`;

    let icon = 'ℹ️';
    if (type === 'success') icon = '✓';
    if (type === 'error') icon = '⚠️';

    toast.innerHTML = `<span>${icon}</span> <span>${escapeHtml(message)}</span>`;
    container.appendChild(toast);

    requestAnimationFrame(() => {
      toast.classList.add('show');
    });

    setTimeout(() => {
      toast.classList.remove('show');
      setTimeout(() => toast.remove(), 300);
    }, duration);
  };

  function escapeHtml(str) {
    return String(str || '').replace(/[&<>'"]/g, c => ({
      '&': '&amp;',
      '<': '&lt;',
      '>': '&gt;',
      "'": '&#39;',
      '"': '&quot;'
    }[c]));
  }

  // 5. Skeleton Loaders Helper Functions
  window.renderSkeletonComplaints = function (containerSelector, count = 3) {
    const container = document.querySelector(containerSelector);
    if (!container) return;

    let html = '';
    for (let i = 0; i < count; i++) {
      html += `
        <div class="skeleton-card">
          <div class="skeleton-box skeleton-line title"></div>
          <div class="skeleton-box skeleton-line medium"></div>
          <div class="skeleton-box skeleton-line long"></div>
        </div>
      `;
    }
    container.innerHTML = html;
  };

  window.renderSkeletonStats = function () {
    const statCards = document.querySelectorAll('.summary-card strong');
    statCards.forEach(el => {
      el.innerHTML = '<span class="skeleton-box skeleton-line short" style="display:inline-block;width:40px;height:24px;"></span>';
    });
  };

  // Immediate Execution of Splash
  createSplashOverlay();

  document.addEventListener('DOMContentLoaded', () => {
    initScrollEnhancements();
    initStatsCountUp();
  });

})();
