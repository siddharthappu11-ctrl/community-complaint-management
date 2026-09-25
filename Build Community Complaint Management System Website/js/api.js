/**
 * API Client for WardConnect Community Complaint Management System
 * Handles API requests, JWT token management, error handling, and session storage.
 */
const API_BASE_URL = window.location.port === '8080' 
  ? '/api' 
  : 'http://localhost:8080/api';

const TOKEN_KEY = 'ccms_jwt_token';
const SESSION_KEY = 'ccms_user_session';

const API = {
  // Session & Token Helpers
  getToken() {
    return localStorage.getItem(TOKEN_KEY);
  },

  getSession() {
    try {
      return JSON.parse(localStorage.getItem(SESSION_KEY));
    } catch (e) {
      return null;
    }
  },

  setSession(data) {
    if (data.token) {
      localStorage.setItem(TOKEN_KEY, data.token);
    }
    const sessionInfo = {
      role: data.role ? data.role.toLowerCase() : '',
      name: data.name || '',
      email: data.email || ''
    };
    localStorage.setItem(SESSION_KEY, JSON.stringify(sessionInfo));
  },

  clearSession() {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(SESSION_KEY);
  },

  // Central fetch wrapper with error handling & JWT Authorization
  async request(endpoint, options = {}) {
    const url = `${API_BASE_URL}${endpoint}`;
    const headers = {
      'Content-Type': 'application/json',
      ...options.headers
    };

    const token = this.getToken();
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    try {
      const response = await fetch(url, {
        ...options,
        headers
      });

      const contentType = response.headers.get('content-type');
      let data = null;
      if (contentType && contentType.includes('application/json')) {
        data = await response.json();
      }

      if (!response.ok) {
        if (response.status === 401 || response.status === 403) {
          // Check if error is from auth attempt or session expiry
          if (!endpoint.startsWith('/auth/')) {
            this.clearSession();
            window.location.href = 'index.html';
          }
        }
        let message = (data && data.message) 
          ? data.message 
          : `Server error (${response.status})`;
        
        if (data && data.fieldErrors) {
          const firstField = Object.keys(data.fieldErrors)[0];
          if (firstField) {
            message = data.fieldErrors[firstField];
          }
        }
        throw new Error(message);
      }

      return data;
    } catch (err) {
      if (err.name === 'TypeError' && err.message.includes('fetch')) {
        const errorMsg = 'Unable to connect to server. Please check if the Java Spring Boot backend is running at http://localhost:8080.';
        if (window.showAppToast) window.showAppToast(errorMsg, 'error');
        throw new Error(errorMsg);
      }
      throw err;
    }
  },

  // Auth endpoints
  async registerResident(name, email, password) {
    const res = await this.request('/auth/resident/register', {
      method: 'POST',
      body: JSON.stringify({ name, email, password })
    });
    this.setSession(res);
    return res;
  },

  async loginResident(email, password) {
    const res = await this.request('/auth/resident/login', {
      method: 'POST',
      body: JSON.stringify({ email, password })
    });
    this.setSession(res);
    return res;
  },

  async loginAdmin(email, password) {
    const res = await this.request('/auth/admin/login', {
      method: 'POST',
      body: JSON.stringify({ email, password })
    });
    this.setSession(res);
    return res;
  },

  // Public endpoints
  async getPublicStats() {
    return await this.request('/public/stats');
  },

  async trackComplaint(trackingId) {
    return await this.request(`/public/track/${encodeURIComponent(trackingId)}`);
  },

  // Resident endpoints
  async createComplaint(complaintData) {
    return await this.request('/complaints', {
      method: 'POST',
      body: JSON.stringify(complaintData)
    });
  },

  async getMyComplaints() {
    return await this.request('/complaints/my');
  },

  // Admin endpoints
  async getAdminComplaints(status, category, query) {
    const params = new URLSearchParams();
    if (status && status !== 'All') params.append('status', status);
    if (category && category !== 'All') params.append('category', category);
    if (query && query.trim()) params.append('q', query.trim());
    
    const queryString = params.toString() ? `?${params.toString()}` : '';
    return await this.request(`/admin/complaints${queryString}`);
  },

  async updateAdminComplaint(id, status, adminResponse) {
    return await this.request(`/admin/complaints/${id}`, {
      method: 'PUT',
      body: JSON.stringify({ status, adminResponse })
    });
  },

  async getAdminStats() {
    return await this.request('/admin/stats');
  }
};
